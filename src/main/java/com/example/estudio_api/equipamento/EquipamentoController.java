package com.example.estudio_api.equipamento;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.estudio_api.equipamento.dto.EquipamentoRequestDTO;
import com.example.estudio_api.equipamento.dto.EquipamentoResponseDTO;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/equipamento")
@RequiredArgsConstructor
public class EquipamentoController {
    
    private final EquipamentoService service;

    @PostMapping("/")
    public ResponseEntity<EquipamentoResponseDTO> criar(@Valid @RequestBody EquipamentoRequestDTO dto) {
        Equipamento equipamento = service.criar(dto);
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(new EquipamentoResponseDTO(equipamento));
    }
    
    @GetMapping("/")
    public ResponseEntity<List<EquipamentoResponseDTO>> listar() {
        List<EquipamentoResponseDTO> lista = service.listar()
            .stream()
            .map(e -> new EquipamentoResponseDTO(e))
            .toList();

        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EquipamentoResponseDTO> buscarPorId(@PathVariable Long id) {
        Equipamento equipamento = service.buscarPorId(id);
        return ResponseEntity.ok(new EquipamentoResponseDTO(equipamento));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EquipamentoResponseDTO> atualizar(@PathVariable Long id, @RequestBody EquipamentoRequestDTO dto) {
        Equipamento equipamento = service.atualizar(id, dto);
        return ResponseEntity.ok(new EquipamentoResponseDTO(equipamento));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id){
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }

}
