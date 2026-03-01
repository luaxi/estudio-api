package com.example.estudio_api.cliente;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.estudio_api.cliente.dto.ClienteRequestDTO;
import com.example.estudio_api.cliente.dto.ClienteResponseDTO;
import com.example.estudio_api.reserva.ReservaService;
import com.example.estudio_api.reserva.dto.ReservaResponseDTO;

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
    private final ReservaService reservaService;

    @PostMapping
    public ResponseEntity<ClienteResponseDTO> criar(@Valid @RequestBody ClienteRequestDTO dto) {
        Cliente cliente = service.criar(dto);
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(new ClienteResponseDTO(cliente));
    }

    @GetMapping
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

    @PatchMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> atualizarParcial(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        Cliente cliente = service.atualizarParcial(id, updates);
        return ResponseEntity.ok(new ClienteResponseDTO(cliente));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id){
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/reservas")
    public ResponseEntity<List<ReservaResponseDTO>> listarReservasPorCliente(@PathVariable Long id) {

        List<ReservaResponseDTO> lista = reservaService.listarPorCliente(id)
            .stream()
            .map(reserva -> new ReservaResponseDTO(reserva))
            .toList();

        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}/reservas/ativas")
    public ResponseEntity<List<ReservaResponseDTO>> listarReservasAtivasPorCliente(@PathVariable Long id) {

        List<ReservaResponseDTO> lista = reservaService.listarAtivasPorCliente(id)
            .stream()
            .map(reserva -> new ReservaResponseDTO(reserva))
            .toList();

        return ResponseEntity.ok(lista);
    }
    
    @GetMapping("/{id}/reservas/passadas")
    public ResponseEntity<List<ReservaResponseDTO>> listarReservasPassadasPorCliente(@PathVariable Long id) {

        List<ReservaResponseDTO> lista = reservaService.listarPassadasPorCliente(id)
            .stream()
            .map(reserva -> new ReservaResponseDTO(reserva))
            .toList();

        return ResponseEntity.ok(lista);
    }
    
}
