package com.example.estudio_api.cliente.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ClienteRequestDTO(

    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 2, max = 100)
    String nome,

    @NotBlank(message = "Telefone é obrigatório")
    @Pattern(
        regexp = "\\d{10,11}",
        message = "O telefone deve possuir apenas números e ter entre 10 e 11 caracteres"
    )
    String telefone
    
) {}