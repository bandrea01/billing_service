package it.unisalento.music_virus_project.billing_service.exceptions;

public class ForbiddenActionException extends RuntimeException {
    public ForbiddenActionException(String message) { super(message); }
}
