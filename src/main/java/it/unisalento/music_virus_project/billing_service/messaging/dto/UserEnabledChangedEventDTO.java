package it.unisalento.music_virus_project.billing_service.messaging.dto;


import it.unisalento.music_virus_project.billing_service.domain.entity.Role;

public class UserEnabledChangedEventDTO {

    private String userId;
    private boolean enabled;
    private Role role;

    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public boolean isEnabled() {
        return enabled;
    }
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    public Role getRole() {
        return role;
    }
    public void setRole(Role role) {
        this.role = role;
    }

}
