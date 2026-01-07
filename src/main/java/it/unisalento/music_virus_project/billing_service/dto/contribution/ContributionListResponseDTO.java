package it.unisalento.music_virus_project.billing_service.dto.contribution;

import java.util.ArrayList;
import java.util.List;

public class ContributionListResponseDTO {
    private List<ContributionResponseDTO> contributions;

    public ContributionListResponseDTO(List<ContributionResponseDTO> contributions) {
        this.contributions = contributions;
    }
    public ContributionListResponseDTO() {
        this.contributions = new ArrayList<>();
    }

    public List<ContributionResponseDTO> getContributions() {
        return contributions;
    }

    public void setContributions(List<ContributionResponseDTO> contributions) {
        this.contributions = contributions;
    }

    public void addContribution(ContributionResponseDTO contribution) {
        this.contributions.add(contribution);
    }
}
