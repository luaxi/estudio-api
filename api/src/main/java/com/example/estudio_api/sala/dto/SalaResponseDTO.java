package com.example.estudio_api.sala.dto;

import java.math.BigDecimal;

import com.example.estudio_api.sala.Sala;

public record SalaResponseDTO(
    Long id,
    String nome,
    BigDecimal precoPorHora
) {
    public SalaResponseDTO(Sala sala){
        this(sala.getId(), sala.getNome(), sala.getPrecoPorHora());
    }
}
