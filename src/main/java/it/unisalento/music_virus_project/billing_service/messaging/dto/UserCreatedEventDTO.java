package it.unisalento.music_virus_project.billing_service.messaging.dto;

import it.unisalento.music_virus_project.billing_service.domain.entity.Role;

public class UserCreatedEventDTO {
    private String userId;
    private String email;
    private Role role;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

}
