package it.unisalento.music_virus_project.billing_service.controllers;

import it.unisalento.music_virus_project.billing_service.dto.fee.FeeCreateRequestDTO;
import it.unisalento.music_virus_project.billing_service.dto.fee.FeeListResponseDTO;
import it.unisalento.music_virus_project.billing_service.dto.fee.FeeResponseDTO;
import it.unisalento.music_virus_project.billing_service.dto.fee.FeeUpdateRequestDTO;
import it.unisalento.music_virus_project.billing_service.service.IFeeService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/billing/fee")
@Validated
public class FeeController {

    private final IFeeService feeService;

    public FeeController(IFeeService feeService) {
        this.feeService = feeService;
    }

    @GetMapping()
    public ResponseEntity<FeeListResponseDTO> getFeesList() {
        var response = feeService.getFeesList();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/artists")
    public ResponseEntity<FeeResponseDTO> getArtistsFees() {
        var response = feeService.getArtistFees();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/venues")
    public ResponseEntity<FeeResponseDTO> getVenuesFees() {
        var response = feeService.getVenuesFees();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/fans")
    public ResponseEntity<FeeResponseDTO> getFansFees() {
        var response = feeService.getFansFees();
        return ResponseEntity.ok(response);
    }

    @PostMapping()
    public ResponseEntity<FeeResponseDTO> createFee(@RequestBody FeeCreateRequestDTO feeCreateRequestDTO) {
        var response = feeService.createFee(feeCreateRequestDTO);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{feePlanId}")
    public ResponseEntity<FeeResponseDTO> updateFee(@PathVariable String feePlanId, @RequestBody FeeUpdateRequestDTO feeUpdateRequestDTO) {
        var response = feeService.updateFee(feePlanId, feeUpdateRequestDTO);
        return ResponseEntity.ok(response);
    }

}
