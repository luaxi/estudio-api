package com.example.estudio_api.equipamento.dto;

import com.example.estudio_api.equipamento.Equipamento;
import com.example.estudio_api.equipamento.enums.TipoEquipamento;

public record EquipamentoResponseDTO(
    Long id,
    String nome,
    TipoEquipamento tipo,
    Long salaId
) {
    public EquipamentoResponseDTO(Equipamento equipamento){
        this(equipamento.getId(), equipamento.getNome(), equipamento.getTipo(), equipamento.getSala().getId());
    }
}
