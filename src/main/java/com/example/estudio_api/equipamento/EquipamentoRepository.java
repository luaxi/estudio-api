package com.example.estudio_api.equipamento;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EquipamentoRepository extends JpaRepository<Equipamento, Long> {
    
    List<Equipamento> findBySalaId(Long salaId);

}
