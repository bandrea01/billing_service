package it.unisalento.music_virus_project.billing_service.exceptions;

public class InsufficentBalanceException extends RuntimeException {
    public InsufficentBalanceException(String message) {
        super(message);
    }
}
