package com.example.estudio_api.cliente.dto;

import com.example.estudio_api.cliente.Cliente;

public record ClienteResponseDTO(
    Long id,
    String nome,
    String telefone
) {
    public ClienteResponseDTO(Cliente cliente) {
        this(
            cliente.getId(),
            cliente.getNome(),
            cliente.getTelefone()
        );
    }
}
