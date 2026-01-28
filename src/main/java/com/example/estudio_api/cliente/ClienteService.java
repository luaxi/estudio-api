package com.example.estudio_api.cliente;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.estudio_api.exceptions.NotFoundException;
import com.example.estudio_api.cliente.dto.ClienteRequestDTO;
import com.example.estudio_api.cliente.dto.ClienteResponseDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClienteService {
    
    public final ClienteRepository repository;

    public ClienteResponseDTO criar(ClienteRequestDTO dto){
        Cliente cliente = Cliente.builder()
            .nome(dto.nome())
            .telefone(dto.telefone())
            .build();
        
        repository.save(cliente);

        return new ClienteResponseDTO(cliente.getId(), cliente.getNome(), cliente.getTelefone());
    }

    public List<ClienteResponseDTO> listar(){
        return repository.findAll()
            .stream()
            .map(c -> new ClienteResponseDTO(c.getId(), c.getNome(), c.getTelefone()))
            .toList();
    }

    public ClienteResponseDTO buscarPorId(Long id){
        Cliente cliente = repository.findById(id)
            .orElseThrow(() -> new NotFoundException("Cliente não encontrado!"));
        
        return new ClienteResponseDTO(cliente.getId(), cliente.getNome(), cliente.getTelefone());
    }

    public ClienteResponseDTO atualizar(Long id, ClienteRequestDTO dto){
        
        Cliente cliente = repository.findById(id)
            .orElseThrow(() -> new NotFoundException("Cliente não encontrado!"));
        
        cliente.setNome(dto.nome());
        cliente.setTelefone(dto.telefone());

        repository.save(cliente);

        return new ClienteResponseDTO(cliente.getId(), cliente.getNome(), cliente.getTelefone());
    }

    public void deletar(Long id){
        
        if(!repository.existsById(id)){
            throw new NotFoundException("Cliente não encontrado!");
        }
        
        repository.deleteById(id);
    }

}
