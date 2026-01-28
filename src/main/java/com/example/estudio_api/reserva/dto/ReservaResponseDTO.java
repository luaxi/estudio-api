package com.example.estudio_api.reserva.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.example.estudio_api.reserva.Reserva;

public record ReservaResponseDTO(
    Long id,
    Long clienteId,
    Long salaId,
    LocalDateTime dataInicio,
    LocalDateTime dataFim,
    BigDecimal valorTotal,
    LocalDateTime criadoEm,
    LocalDateTime modificadoEm
) {
    public ReservaResponseDTO(Reserva reserva) {
        this(
            reserva.getId(),
            reserva.getCliente().getId(),
            reserva.getSala().getId(),
            reserva.getDataInicio(),
            reserva.getDataFim(),
            reserva.getValorTotal(),
            reserva.getCriadoEm(),
            reserva.getModificadoEm()
        );
    }
}
