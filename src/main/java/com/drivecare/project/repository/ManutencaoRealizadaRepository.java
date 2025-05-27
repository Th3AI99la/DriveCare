package com.drivecare.project.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.drivecare.project.model.ManutencaoRealizada;

@Repository
public interface ManutencaoRealizadaRepository extends JpaRepository<ManutencaoRealizada, Long> {

    @Query("SELECT mr FROM ManutencaoRealizada mr ORDER BY mr.dataExecucao DESC")
    List<ManutencaoRealizada> findRecentManutencoesRealizadas(Pageable pageable);

    @Query("SELECT SUM(mr.custoReal) FROM ManutencaoRealizada mr WHERE YEAR(mr.dataExecucao) = YEAR(CURRENT_DATE) AND MONTH(mr.dataExecucao) = MONTH(CURRENT_DATE)")
    Double sumCurrentMonthRealizedExpenses();

    @Query("SELECT mr.dataExecucao, SUM(mr.custoReal) as totalCusto, COUNT(mr.id) as quantidade " +
           "FROM ManutencaoRealizada mr " +
           "WHERE mr.dataExecucao BETWEEN :startDate AND :endDate " +
           "GROUP BY mr.dataExecucao " +
           "ORDER BY mr.dataExecucao ASC")
    List<Object[]> findGanhoEQuantidadePorDiaAgrupado(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    // CORREÇÃO APLICADA AQUI:
    @Query("SELECT YEAR(mr.dataExecucao) as ano, MONTH(mr.dataExecucao) as mes, " +
           "SUM(mr.custoReal) as totalCusto, COUNT(mr.id) as quantidade " +
           "FROM ManutencaoRealizada mr " +
           "WHERE mr.dataExecucao >= :startDate AND mr.dataExecucao <= :endDate " +
           "GROUP BY YEAR(mr.dataExecucao), MONTH(mr.dataExecucao) " +
           "ORDER BY ano ASC, mes ASC")
    List<Object[]> findGanhoEQuantidadePorMesAgrupado(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}