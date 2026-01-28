package it.unisalento.music_virus_project.billing_service.messaging.dto;


import it.unisalento.music_virus_project.billing_service.domain.entity.Role;

public class UserApprovalChangedEventDTO {

    private String userId;
    private boolean approved;
    private Role role;

    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public boolean isApproved() {
        return approved;
    }
    public void setApproved(boolean approved) {
        this.approved = approved;
    }
    public Role getRole() {
        return role;
    }
    public void setRole(Role role) {
        this.role = role;
    }
}
