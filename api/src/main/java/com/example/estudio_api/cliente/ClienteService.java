package com.example.estudio_api.cliente;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.estudio_api.cliente.dto.ClienteRequestDTO;
import com.example.estudio_api.shared.errors.CampoInvalidoException;
import com.example.estudio_api.shared.errors.NotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClienteService {
    
    public final ClienteRepository repository;

    public Cliente criar(ClienteRequestDTO dto){
        Cliente cliente = Cliente.builder()
            .nome(dto.nome())
            .telefone(dto.telefone())
            .build();
        
        return repository.save(cliente);
    }

    public List<Cliente> listar(){
        return repository.findAll();
    }

    public Cliente buscarPorId(Long id){
        return repository.findById(id)
            .orElseThrow(() -> new NotFoundException("Cliente não encontrado!"));
    }

    public Cliente atualizar(Long id, ClienteRequestDTO dto){
        
        Cliente cliente = repository.findById(id)
            .orElseThrow(() -> new NotFoundException("Cliente não encontrado!"));
        
        cliente.setNome(dto.nome());
        cliente.setTelefone(dto.telefone());

        return repository.save(cliente);
    }

    public Cliente atualizarParcial(Long id, Map<String, Object> updates){
        
        Cliente cliente = repository.findById(id)
            .orElseThrow(() -> new NotFoundException("Cliente não encontrado!"));
        
        // Itera sobre os campos enviados no map
        updates.forEach((campo, valor) -> {
            switch(campo) {
                case "nome" -> {
                    if(valor != null && !valor.toString().isBlank()) {
                        cliente.setNome(valor.toString());
                    }
                }
                case "telefone" -> {
                    if(valor != null && !valor.toString().isBlank()) {
                        cliente.setTelefone(valor.toString());
                    }
                }
                default -> throw new CampoInvalidoException("Campo inválido: " + campo);
            }
        });

        return repository.save(cliente);
    }

    public void deletar(Long id){
        
        // Verifica se o cliente existe
        if(!repository.existsById(id)){
            throw new NotFoundException("Cliente não encontrado!");
        }
        
        repository.deleteById(id);
    }

    public boolean existeCliente(Long clienteId){
        return repository.existsById(clienteId);
    }

}
