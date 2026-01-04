package it.unisalento.music_virus_project.billing_service.service.implementation;

import it.unisalento.music_virus_project.billing_service.domain.entity.FeePlan;
import it.unisalento.music_virus_project.billing_service.domain.entity.Role;
import it.unisalento.music_virus_project.billing_service.domain.enums.FeeType;
import it.unisalento.music_virus_project.billing_service.dto.fee.FeeCreateRequestDTO;
import it.unisalento.music_virus_project.billing_service.dto.fee.FeeListResponseDTO;
import it.unisalento.music_virus_project.billing_service.dto.fee.FeeResponseDTO;
import it.unisalento.music_virus_project.billing_service.dto.fee.FeeUpdateRequestDTO;
import it.unisalento.music_virus_project.billing_service.exceptions.AlreadyExistingFeePlanException;
import it.unisalento.music_virus_project.billing_service.exceptions.NotFoundException;
import it.unisalento.music_virus_project.billing_service.repositories.IFeeRepository;
import it.unisalento.music_virus_project.billing_service.service.IFeeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Service
public class FeeService implements IFeeService {

    private final IFeeRepository feeRepository;

    public FeeService(IFeeRepository feeRepository) {
        this.feeRepository = feeRepository;
    }

    @Override
    public FeeListResponseDTO getFeesList() {
        List<FeePlan> fees = feeRepository.findAll();
        return mapToDTOList(fees);
    }

    @GetMapping
    public FeeResponseDTO getEventTaxFee() {
        FeePlan fee = feeRepository.findFeeByFeeType(FeeType.EVENT_TAX);
        return mapToDTO(fee);
    }

    @Override
    public FeeResponseDTO getArtistFees() {
        FeePlan fee = feeRepository.findFeePlanByIsApplicatedToContains(Role.ARTIST);
        return mapToDTO(fee);
    }

    @Override
    public FeeResponseDTO getVenuesFees() {
        FeePlan fee = feeRepository.findFeePlanByIsApplicatedToContains(Role.VENUE);
        return mapToDTO(fee);
    }

    @Override
    public FeeResponseDTO getFansFees() {
        FeePlan fee = feeRepository.findFeePlanByIsApplicatedToContains(Role.FAN);
        return mapToDTO(fee);
    }

    @Override
    @Transactional
    public FeeResponseDTO createFee(FeeCreateRequestDTO feeCreateRequestDTO) {
        // Any user type should have only one fee plan
        FeePlan existingFee = feeRepository.findFeePlanByIsApplicatedToContains(feeCreateRequestDTO.getIsApplicatedTo().iterator().next());
        if (existingFee != null) {
            throw new AlreadyExistingFeePlanException("Errore: esiste già un piano tariffario per uno dei ruoli specificati.");
        }

        FeePlan fee = new FeePlan();
        fee.setFeeType(feeCreateRequestDTO.getFeeType());
        fee.setIsApplicatedTo(new ArrayList<>(feeCreateRequestDTO.getIsApplicatedTo()));
        fee.setAmount(feeCreateRequestDTO.getAmount());
        fee.setFeePeriod(feeCreateRequestDTO.getFeePeriod());
        fee.setActiveSince(feeCreateRequestDTO.getActiveSince());

        fee = feeRepository.save(fee);

        return mapToDTO(fee);
    }

    @Override
    @Transactional
    public FeeResponseDTO updateFee(String feePlanId, FeeUpdateRequestDTO feeUpdateRequestDTO) {
        FeePlan fee = feeRepository.findFeeByFeePlanId(feePlanId);
        if (fee == null) {
            throw new NotFoundException("Errore: piano tariffario non trovato.");
        }
        if (feeUpdateRequestDTO.getIsApplicatedTo() != null) {
            fee.setIsApplicatedTo(new ArrayList<>(feeUpdateRequestDTO.getIsApplicatedTo()));
        }
        if (feeUpdateRequestDTO.getAmount() != null) {
            fee.setAmount(feeUpdateRequestDTO.getAmount());
        }
        if (feeUpdateRequestDTO.getFeePeriod() != null) {
            fee.setFeePeriod(feeUpdateRequestDTO.getFeePeriod());
        }
        if (feeUpdateRequestDTO.getActiveSince() != null) {
            fee.setActiveSince(feeUpdateRequestDTO.getActiveSince());
        }
        feeRepository.save(fee);
        return mapToDTO(fee);
    }

    //utils
    private FeeResponseDTO mapToDTO(FeePlan feePlan) {
        if (feePlan == null) {
            return null;
        }
        FeeResponseDTO dto = new FeeResponseDTO();
        dto.setFeeType(feePlan.getFeeType());
        dto.setFeePlanId(feePlan.getFeePlanId());
        dto.setIsApplicatedTo(new ArrayList<>(feePlan.getIsApplicatedTo()));
        dto.setAmount(feePlan.getAmount());
        dto.setFeePeriod(feePlan.getFeePeriod());
        dto.setActiveSince(feePlan.getActiveSince());
        return dto;
    }

    private FeeListResponseDTO mapToDTOList(Iterable<FeePlan> feePlans) {
        FeeListResponseDTO dtoList = new FeeListResponseDTO();
        for (FeePlan feePlan : feePlans) {
            dtoList.getFees().add(mapToDTO(feePlan));
        }
        return dtoList;
    }

}
