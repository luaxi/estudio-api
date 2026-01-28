package com.example.estudio_api.sala;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.estudio_api.equipamento.EquipamentoService;
import com.example.estudio_api.exceptions.NotFoundException;
import com.example.estudio_api.exceptions.SalaComEquipamentosException;
import com.example.estudio_api.sala.dto.SalaRequestDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SalaService {
    
    private final SalaRepository repository;
    private final EquipamentoService equipamentoService;

    public Sala criar(SalaRequestDTO dto){
        Sala sala = Sala.builder()
            .nome(dto.nome())
            .precoPorHora(dto.precoPorHora())
            .build();
        
        return repository.save(sala);
    }

    public List<Sala> listar(){
        return repository.findAll();
    }

    public Sala buscarPorId(Long id){
        return repository.findById(id)
            .orElseThrow(() -> new NotFoundException("Sala não encontrada!"));
    }

    public Sala atualizar(Long id, SalaRequestDTO dto){
        Sala sala = repository.findById(id)
            .orElseThrow(() -> new NotFoundException("Sala não encontrada!"));
        
        sala.setNome(dto.nome());
        sala.setPrecoPorHora(dto.precoPorHora());

        return repository.save(sala);
    }

    public void deletar(Long id){
        if(!repository.existsById(id)){
            throw new NotFoundException("Sala não encontrada!");
        }
        
        // Verifica se existem equipamentos vinculados à sala
        if(equipamentoService.existeEquipamentoNaSala(id)){
            throw new SalaComEquipamentosException("A sala ainda possui equipamentos vinculados!");
        }

        repository.deleteById(id);
    }
    
}
