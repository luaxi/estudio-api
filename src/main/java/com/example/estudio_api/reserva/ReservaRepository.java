package com.example.estudio_api.reserva;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    
    @Query("""
        SELECT r FROM Reserva r
        WHERE r.sala.id = :salaId
          AND r.dataInicio < :dataFim
          AND :dataInicio < r.dataFim
    """)
    List<Reserva> findConflitos(Long salaId, LocalDateTime dataInicio, LocalDateTime dataFim);

    List<Reserva> findBySalaId(Long id);

    List<Reserva> findByClienteId(Long id);

    @Query("""
        SELECT r FROM Reserva r
        WHERE r.cliente.id = :clienteId
          AND r.dataFim >= CURRENT_TIMESTAMP
    """)
    List<Reserva> findAtivasByClienteId(Long clienteId);

    @Query("""
        SELECT r FROM Reserva r
        WHERE r.cliente.id = :clienteId
          AND r.dataFim < CURRENT_TIMESTAMP
    """)
    List<Reserva> findPassadasByClienteId(Long clienteId);

}
