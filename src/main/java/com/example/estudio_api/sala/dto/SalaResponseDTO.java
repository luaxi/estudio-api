package com.example.estudio_api.sala.dto;

import java.math.BigDecimal;

public record SalaResponseDTO(
    Long id,
    String nome,
    BigDecimal precoPorHora
) {}
