package com.example.estudio_api.equipamento;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.estudio_api.exceptions.NotFoundException;
import com.example.estudio_api.equipamento.dto.EquipamentoRequestDTO;
import com.example.estudio_api.sala.Sala;
import com.example.estudio_api.sala.SalaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EquipamentoService {
    
    private final EquipamentoRepository repository;
    private final SalaRepository salaRepository;

    public Equipamento criar(EquipamentoRequestDTO dto){
        Sala sala = salaRepository.findById(dto.salaId())
            .orElseThrow(() -> new NotFoundException("Sala não encontrada!"));

        Equipamento equipamento = Equipamento.builder()
            .nome(dto.nome())
            .tipo(dto.tipo())
            .sala(sala)
            .build();
        
        return repository.save(equipamento);
    }

    public List<Equipamento> listar(){
        return repository.findAll();
    }

    public List<Equipamento> listarPorSala(Long salaId){
        if(!salaRepository.existsById(salaId)){
            throw new NotFoundException("Sala não encontrada!");
        }
        return repository.findBySalaId(salaId);
    }

    public Equipamento buscarPorId(Long id){
        return repository.findById(id)
            .orElseThrow(() -> new NotFoundException("Equipamento não encontrado!"));
    }

    public Equipamento atualizar(Long id, EquipamentoRequestDTO dto){
        Equipamento equipamento = repository.findById(id)
            .orElseThrow(() -> new NotFoundException("Equipamento não encontrado!"));
        
        Sala sala = salaRepository.findById(dto.salaId())
            .orElseThrow(() -> new NotFoundException("Sala não encontrada!"));
        
        equipamento.setNome(dto.nome());
        equipamento.setTipo(dto.tipo());
        equipamento.setSala(sala);

        return repository.save(equipamento);
    }

    public void deletar(Long id){
        if(!repository.existsById(id)){
            throw new NotFoundException("Equipamento não encontrado!");
        }

        repository.deleteById(id);
    }

    public boolean existeEquipamentoNaSala(Long salaId){
        return repository.existsBySalaId(salaId);
    }
    
}
