package it.unisalento.music_virus_project.billing_service.controllers;

import it.unisalento.music_virus_project.billing_service.dto.fee.FeeListResponseDTO;
import it.unisalento.music_virus_project.billing_service.service.IFeeService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/billing/fee")
@Validated
public class FeeController {

    private final IFeeService feeService;

    public FeeController(IFeeService feeService) {
        this.feeService = feeService;
    }

    @GetMapping("/artists")
    public ResponseEntity<FeeListResponseDTO> getArtistsFees() {
        var response = feeService.getArtistFees();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/venues")
    public ResponseEntity<FeeListResponseDTO> getVenuesFees() {
        var response = feeService.getVenuesFees();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/fans")
    public ResponseEntity<FeeListResponseDTO> getFansFees() {
        var response = feeService.getFansFees();
        return ResponseEntity.ok(response);
    }

}
