package com.example.estudio_api.sala;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.estudio_api.NotFoundException;
import com.example.estudio_api.sala.dto.SalaRequestDTO;
import com.example.estudio_api.sala.dto.SalaResponseDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SalaService {

    private final SalaRepository repository;

    public SalaResponseDTO criar(SalaRequestDTO dto){
        Sala sala = Sala.builder()
            .nome(dto.nome())
            .precoPorHora(dto.precoPorHora())
            .build();
        
        repository.save(sala);

        return new SalaResponseDTO(sala.getId(), sala.getNome(), sala.getPrecoPorHora());
    }

    public List<SalaResponseDTO> listar(){
        return repository.findAll()
            .stream()
            .map(s -> new SalaResponseDTO(s.getId(), s.getNome(), s.getPrecoPorHora()))
            .toList();
    }

    public SalaResponseDTO buscarPorId(Long id){
        Sala sala = repository.findById(id)
            .orElseThrow(() -> new NotFoundException("Sala não encontrada!"));
        
        return new SalaResponseDTO(sala.getId(), sala.getNome(), sala.getPrecoPorHora());
    }

    public SalaResponseDTO atualizar(Long id, SalaRequestDTO dto){
        Sala sala = repository.findById(id)
            .orElseThrow(() -> new NotFoundException("Sala não encontrada!"));
        
        sala.setNome(dto.nome());
        sala.setPrecoPorHora(dto.precoPorHora());

        repository.save(sala);

        return new SalaResponseDTO(sala.getId(), sala.getNome(), sala.getPrecoPorHora());
    }

    public void deletar(Long id){
        if(!repository.existsById(id)){
            throw new NotFoundException("Sala não encontrada!");
        }

        repository.deleteById(id);
    }
    
}
