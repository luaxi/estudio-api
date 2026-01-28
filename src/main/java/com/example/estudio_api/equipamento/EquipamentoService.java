package com.example.estudio_api.equipamento;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.estudio_api.NotFoundException;
import com.example.estudio_api.equipamento.dto.EquipamentoRequestDTO;
import com.example.estudio_api.sala.Sala;
import com.example.estudio_api.sala.SalaService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EquipamentoService {
    
    private final EquipamentoRepository repository;
    private final SalaService salaService;

    public Equipamento criar(EquipamentoRequestDTO dto){
        Sala sala = salaService.buscarPorId(dto.salaId());

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
        return repository.findBySalaId(salaId);
    }

    public Equipamento buscarPorId(Long id){
        return repository.findById(id)
            .orElseThrow(() -> new NotFoundException("Equipamento não encontrado!"));
    }

    public Equipamento atualizar(Long id, EquipamentoRequestDTO dto){
        Equipamento equipamento = repository.findById(id)
            .orElseThrow(() -> new NotFoundException("Equipamento não encontrado!"));
        
        Sala sala = salaService.buscarPorId(dto.salaId());
        
        equipamento.setNome(dto.nome());
        equipamento.setTipo(equipamento.getTipo());
        equipamento.setSala(sala);

        return repository.save(equipamento);
    }

    public void deletar(Long id){
        if(!repository.existsById(id)){
            throw new NotFoundException("Equipamento não encontrado!");
        }

        repository.deleteById(id);
    }
    
}
