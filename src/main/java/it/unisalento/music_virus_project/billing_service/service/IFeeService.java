package it.unisalento.music_virus_project.billing_service.service;

import it.unisalento.music_virus_project.billing_service.domain.entity.Role;
import it.unisalento.music_virus_project.billing_service.dto.fee.FeeCreateRequestDTO;
import it.unisalento.music_virus_project.billing_service.dto.fee.FeeListResponseDTO;
import it.unisalento.music_virus_project.billing_service.dto.fee.FeeResponseDTO;
import it.unisalento.music_virus_project.billing_service.dto.fee.FeeUpdateRequestDTO;

public interface IFeeService {
    FeeListResponseDTO getFeesList();
    FeeListResponseDTO getArtistFees();
    FeeListResponseDTO getVenuesFees();
    FeeListResponseDTO getFansFees();
    FeeResponseDTO createFee(FeeCreateRequestDTO feeCreateRequestDTO);
    FeeResponseDTO updateFee(String feePlanId, FeeUpdateRequestDTO feeUpdateRequestDTO);
}
