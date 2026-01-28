package com.example.estudio_api.cliente;

import org.springframework.web.bind.annotation.*;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.estudio_api.cliente.dto.ClienteRequestDTO;
import com.example.estudio_api.cliente.dto.ClienteResponseDTO;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/cliente")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService service;

    @PostMapping("/")
    public ResponseEntity<ClienteResponseDTO> criar(@Valid @RequestBody ClienteRequestDTO dto) {
        Cliente cliente = service.criar(dto);
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(new ClienteResponseDTO(cliente));
    }

    @GetMapping("/")
    public ResponseEntity<List<ClienteResponseDTO>> listar() {
        List<ClienteResponseDTO> lista = service.listar()
            .stream()
            .map(cliente -> new ClienteResponseDTO(cliente))
            .toList();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> buscarPorId(@PathVariable Long id) {
        Cliente cliente = service.buscarPorId(id);
        return ResponseEntity.ok(new ClienteResponseDTO(cliente));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> atualizar(@PathVariable Long id, @Valid @RequestBody ClienteRequestDTO dto) {
        Cliente cliente = service.atualizar(id, dto);
        return ResponseEntity.ok(new ClienteResponseDTO(cliente));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id){
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
    
}
