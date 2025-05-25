package com.drivecare.project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.drivecare.project.model.ManutencaoRealizada;

@Repository
public interface ManutencaoRealizadaRepository extends JpaRepository<ManutencaoRealizada, Long> {

    // Método para buscar as 5 manutenções mais recentes
    List<ManutencaoRealizada> findTop5ByOrderByDataExecucaoDesc();

}