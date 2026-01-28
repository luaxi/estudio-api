package com.example.estudio_api.sala;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.estudio_api.equipamento.EquipamentoService;
import com.example.estudio_api.sala.dto.SalaRequestDTO;
import com.example.estudio_api.shared.errors.CampoInvalidoException;
import com.example.estudio_api.shared.errors.NotFoundException;
import com.example.estudio_api.shared.errors.SalaComEquipamentosException;

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

    public Sala atualizarParcial(Long id, Map<String, Object> updates){
        
        Sala sala = repository.findById(id)
            .orElseThrow(() -> new NotFoundException("Sala não encontrada!"));
        
        // Itera sobre os campos enviados no map
        updates.forEach((campo, valor) -> {
            switch(campo) {
                case "nome" -> {
                    if(valor != null && !valor.toString().isBlank()) {
                        sala.setNome(valor.toString());
                    }
                }
                case "precoPorHora" -> {
                    if(valor != null) {
                        try {
                            BigDecimal preco = new BigDecimal(valor.toString());
                            sala.setPrecoPorHora(preco);
                        } catch (NumberFormatException e) {
                            throw new CampoInvalidoException("Valor inválido para precoPorHora: " + valor);
                        }
                    }
                }
                default -> throw new CampoInvalidoException("Campo inválido: " + campo);
            }
        });

        return repository.save(sala);
    }

    public void deletar(Long id){

        // Verifica se a sala existe
        if(!repository.existsById(id)){
            throw new NotFoundException("Sala não encontrada!");
        }
        
        // Verifica se existem equipamentos vinculados à sala
        if(equipamentoService.existeEquipamentoNaSala(id)){
            throw new SalaComEquipamentosException("A sala ainda possui equipamentos vinculados!");
        }

        repository.deleteById(id);
    }

    public boolean existeSala(Long salaId){
        return repository.existsById(salaId);
    }
    
}
