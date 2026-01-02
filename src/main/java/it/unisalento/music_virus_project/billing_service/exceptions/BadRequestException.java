package it.unisalento.music_virus_project.billing_service.exceptions;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) { super(message); }
}
