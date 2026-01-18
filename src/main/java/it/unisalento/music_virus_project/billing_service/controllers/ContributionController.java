package it.unisalento.music_virus_project.billing_service.controllers;

import it.unisalento.music_virus_project.billing_service.dto.contribution.ContributionCreateRequestDTO;
import it.unisalento.music_virus_project.billing_service.dto.contribution.ContributionListResponseDTO;
import it.unisalento.music_virus_project.billing_service.dto.contribution.ContributionResponseDTO;
import it.unisalento.music_virus_project.billing_service.service.IContributionService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/billing/contribution")
@Validated
public class ContributionController {

    private final IContributionService contributionService;

    public ContributionController(IContributionService contributionService) {
        this.contributionService = contributionService;
    }

    @PostMapping()
    public ResponseEntity<ContributionResponseDTO> createContribution(@RequestBody ContributionCreateRequestDTO contributionCreateRequestDTO) {
        var response = contributionService.createContribution(contributionCreateRequestDTO);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{contributionId}")
    public ResponseEntity<ContributionResponseDTO> getContributionById(@PathVariable String contributionId) {
        var response = contributionService.getContributionById(contributionId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ContributionListResponseDTO> getAllContributionsByUserId(@PathVariable String userId) {
        var response = contributionService.getContributionsByUserId(userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping()
    public ResponseEntity<ContributionListResponseDTO> getAllContributions() {
        var response = contributionService.getAllContributions();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/visible")
    public ResponseEntity<ContributionListResponseDTO> getAllVisibleContributions() {
        var response = contributionService.getAllVisibleContributions();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/captured")
    public ResponseEntity<ContributionListResponseDTO> getAllCapturedContributions() {
        var response = contributionService.getAllConfirmedContributions();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/refunded")
    public ResponseEntity<ContributionListResponseDTO> getAllRefundedContributions() {
        var response = contributionService.getAllRefundedContributions();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}/visible")
    public ResponseEntity<ContributionListResponseDTO> getAllVisibleContributionsByUserId(@PathVariable String userId) {
        var response = contributionService.getVisibleContributionsByUserId(userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}/captured")
    public ResponseEntity<ContributionListResponseDTO> getAllCapturedContributionsByUserId(@PathVariable String userId) {
        var response = contributionService.getConfirmedContributionsByUserId(userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}/refunded")
    public ResponseEntity<ContributionListResponseDTO> getAllRefundedContributionsByUserId(@PathVariable String userId) {
        var response = contributionService.getRefundedContributionsByUserId(userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/fundraising/{fundraisingId}")
    public ResponseEntity<ContributionListResponseDTO> getAllContributionsByFundraisingId(@PathVariable String fundraisingId) {
        var response = contributionService.getContributionsByFundraisingId(fundraisingId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/fundraising/{fundraisingId}/visible")
    public ResponseEntity<ContributionListResponseDTO> getAllVisibleContributionsByFundraisingId(@PathVariable String fundraisingId) {
        var response = contributionService.getVisibleContributionsByFundraisingId(fundraisingId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/fundraising/{fundraisingId}/captured")
    public ResponseEntity<ContributionListResponseDTO> getAllCapturedContributionsByFundraisingId(@PathVariable String fundraisingId) {
        var response = contributionService.getConfirmedContributionsByFundraisingId(fundraisingId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/fundraising/{fundraisingId}/refunded")
    public ResponseEntity<ContributionListResponseDTO> getAllRefundedContributionsByFundraisingId(@PathVariable String fundraisingId) {
        var response = contributionService.getRefundedContributionsByFundraisingId(fundraisingId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{contributionId}")
    public ResponseEntity<ContributionResponseDTO> deleteContribution(@PathVariable String contributionId) {
        var response = contributionService.deleteContribution(contributionId);
        return ResponseEntity.ok(response);
    }
}
