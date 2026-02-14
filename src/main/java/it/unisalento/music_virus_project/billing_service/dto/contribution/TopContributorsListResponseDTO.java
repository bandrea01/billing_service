package it.unisalento.music_virus_project.billing_service.dto.contribution;

import java.util.ArrayList;
import java.util.List;

public class TopContributorsListResponseDTO {
    private String fundraisingId;
    private List<TopContributorResponseDTO> topContributors;

    public TopContributorsListResponseDTO() {
        this.topContributors = new ArrayList<>();
    }

    public String getFundraisingId() {
        return fundraisingId;
    }
    public void setFundraisingId(String fundraisingId) {
        this.fundraisingId = fundraisingId;
    }
    public List<TopContributorResponseDTO> getTopContributors() {
        return topContributors;
    }
    public void setTopContributors(List<TopContributorResponseDTO> topContributors) {
        this.topContributors = topContributors;
    }
    public void addTopContributor(TopContributorResponseDTO contributor) {
        this.topContributors.add(contributor);
    }

}
