package com.example.estudio_api.reserva;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.estudio_api.reserva.dto.ReservaRequestDTO;
import com.example.estudio_api.reserva.dto.ReservaResponseDTO;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;




@RestController
@RequestMapping("/reserva")
@RequiredArgsConstructor
public class ReservaController {
    
    private final ReservaService service;

    @PostMapping("/")
    public ResponseEntity<ReservaResponseDTO> criar(@RequestBody ReservaRequestDTO dto) {
        Reserva reserva = service.criar(dto);
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(new ReservaResponseDTO(reserva));
    }
    
    @GetMapping("/")
    public ResponseEntity<List<ReservaResponseDTO>> listar() {
        List<ReservaResponseDTO> lista = service.listar()
            .stream()
            .map(reserva -> new ReservaResponseDTO(reserva))
            .toList();
        
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservaResponseDTO> buscarPorId(@PathVariable Long id) {
        Reserva reserva = service.buscarPorId(id);
        return ResponseEntity.ok(new ReservaResponseDTO(reserva));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReservaResponseDTO> atualizar(@PathVariable Long id, @RequestBody ReservaRequestDTO dto) {
        Reserva reserva = service.atualizar(id, dto);
        return ResponseEntity.ok(new ReservaResponseDTO(reserva));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id){
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
    

    
}