package com.example.estudio_api.reserva.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

public record ReservaRequestDTO(

    @NotNull(message = "ID do cliente é obrigatório")
    Long clienteId,

    @NotNull(message = "ID da sala é obrigatório")
    Long salaId,

    @NotNull(message = "Data de início é obrigatória")
    @FutureOrPresent
    LocalDateTime dataInicio,

    @NotNull(message = "Data fim é obrigatória")
    @Future
    LocalDateTime dataFim

) {}
