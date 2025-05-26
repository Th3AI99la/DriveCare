package com.drivecare.project.repository;

import java.util.List;

import org.springframework.data.domain.Pageable; // IMPORTAR Pageable
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query; // IMPORTAR Query
import org.springframework.stereotype.Repository;

import com.drivecare.project.model.ManutencaoRealizada;

@Repository
public interface ManutencaoRealizadaRepository extends JpaRepository<ManutencaoRealizada, Long> {


    @Query("SELECT mr FROM ManutencaoRealizada mr ORDER BY mr.dataExecucao DESC")
    List<ManutencaoRealizada> findRecentManutencoesRealizadas(Pageable pageable);
       
    @Query("SELECT SUM(mr.custoReal) FROM ManutencaoRealizada mr WHERE YEAR(mr.dataExecucao) = YEAR(CURRENT_DATE) AND MONTH(mr.dataExecucao) = MONTH(CURRENT_DATE)")
    Double sumCurrentMonthRealizedExpenses();


}