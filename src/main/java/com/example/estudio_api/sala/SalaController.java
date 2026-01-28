package com.example.estudio_api.sala;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.estudio_api.equipamento.EquipamentoService;
import com.example.estudio_api.equipamento.dto.EquipamentoResponseDTO;
import com.example.estudio_api.sala.dto.SalaRequestDTO;
import com.example.estudio_api.sala.dto.SalaResponseDTO;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/sala")
@RequiredArgsConstructor
public class SalaController {

    private final SalaService service;
    private final EquipamentoService equipamentoService;

    @PostMapping("/")
    public ResponseEntity<SalaResponseDTO> criar(@Valid @RequestBody SalaRequestDTO dto) {
        Sala sala = service.criar(dto);
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(new SalaResponseDTO(sala));
    }
    
    @GetMapping("/")
    public ResponseEntity<List<SalaResponseDTO>> listar() {
        List<SalaResponseDTO> lista = service.listar()
            .stream()
            .map(sala -> new SalaResponseDTO(sala))
            .toList();

        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SalaResponseDTO> buscarPorId(@PathVariable Long id) {
        Sala sala = service.buscarPorId(id);
        return ResponseEntity.ok(new SalaResponseDTO(sala));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SalaResponseDTO> atualizar(@PathVariable Long id, @RequestBody SalaRequestDTO dto) {
        Sala sala = service.atualizar(id, dto);
        return ResponseEntity.ok(new SalaResponseDTO(sala));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id){
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/equipamentos")
    public ResponseEntity<List<EquipamentoResponseDTO>> listarEquipamentos(@PathVariable Long id) {
        List<EquipamentoResponseDTO> equipamentos = equipamentoService.listarPorSala(id)
            .stream()
            .map(e -> new EquipamentoResponseDTO(e))
            .toList();

        return ResponseEntity.ok(equipamentos);
    }

}
