package com.example.estudio_api.sala.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record SalaRequestDTO(

    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 1, max = 100)
    String nome,

    @NotNull(message = "Preço por hora é obrigatório")
    @DecimalMin(value = "0.00", inclusive = false, message = "Preço por hora deve ser maior que zero")
    @Digits(integer = 6, fraction = 2, message = "Preço por hora não é válido")
    BigDecimal precoPorHora

) {}
