package it.unisalento.music_virus_project.billing_service.dto.contribution;

public class TopContributorAggregationDTO{
    private String userId;
    private Integer allAnonymous;

    public TopContributorAggregationDTO() {}

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public Integer getAllAnonymous() { return allAnonymous; }
    public void setAllAnonymous(Integer allAnonymous) { this.allAnonymous = allAnonymous; }
}
