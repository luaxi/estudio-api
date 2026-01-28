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

}
