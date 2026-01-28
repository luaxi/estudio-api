package com.example.estudio_api.shared.errors;

public class CampoInvalidoException extends RuntimeException {
    
    public CampoInvalidoException(String message){
        super(message);
    }
    
}
