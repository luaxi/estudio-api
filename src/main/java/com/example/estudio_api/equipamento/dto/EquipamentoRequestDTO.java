package com.example.estudio_api.equipamento.dto;

import com.example.estudio_api.equipamento.enums.TipoEquipamento;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record EquipamentoRequestDTO(

    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 1, max = 100)
    String nome,

    @NotNull(message = "Tipo é obrigatório")
    TipoEquipamento tipo,

    @NotNull(message = "ID da sala é obrigatório")
    Long salaId

) {}
