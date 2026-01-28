package com.example.estudio_api.reserva;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.estudio_api.reserva.dto.ReservaRequestDTO;
import com.example.estudio_api.reserva.dto.ReservaResponseDTO;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/reserva")
@RequiredArgsConstructor
public class ReservaController {
    
    private final ReservaService service;

    @PostMapping("/")
    public ResponseEntity<ReservaResponseDTO> criar(@RequestBody ReservaRequestDTO dto) {
        Reserva reserva = service.criar(dto);
        return ResponseEntity.ok(new ReservaResponseDTO(reserva));
    }
    

}
