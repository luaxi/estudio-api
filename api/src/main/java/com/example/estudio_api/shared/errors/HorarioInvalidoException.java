package com.example.estudio_api.shared.errors;

public class HorarioInvalidoException extends RuntimeException {
    
    public HorarioInvalidoException(String message) {
        super(message);
    }
}
