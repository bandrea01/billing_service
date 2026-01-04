package it.unisalento.music_virus_project.billing_service.exceptions;

public class AlreadyExistingFeePlanException extends RuntimeException {
    public AlreadyExistingFeePlanException(String message) {
        super(message);
    }
}
