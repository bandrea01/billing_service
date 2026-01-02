package it.unisalento.music_virus_project.billing_service.domain.entity;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {

    ADMIN(Code.ADMIN),
    ARTIST(Code.ARTIST),
    VENUE(Code.VENUE),
    FAN(Code.FAN);

    private final String authority;

    Role(String authority) {
        this.authority = authority;
    }

    @Override
    public String getAuthority() {
        return authority;
    }

    public static class Code {
        public static final String ADMIN = "ROLE_ADMIN";
        public static final String ARTIST = "ROLE_ARTIST";
        public static final String VENUE = "ROLE_VENUE";
        public static final String FAN = "ROLE_FAN";
    }
}
