package com.drivecare.project.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.drivecare.project.model.ManutencaoRealizada;

@Repository
public interface ManutencaoRealizadaRepository extends JpaRepository<ManutencaoRealizada, Long> {

   // Metodo por buscar e ordenar por data de execução
    @Query("SELECT mr FROM ManutencaoRealizada mr WHERE mr.manutencaoAgendada.id = :id ORDER BY mr.dataExecucao DESC")
    List<ManutencaoRealizada> findByManutencaoAgendadaId(@Param("id") Long id);

   // Metodo por buscar e ordenar por ano e mês de execução
    @Query("SELECT SUM(mr.custoReal) FROM ManutencaoRealizada mr WHERE YEAR(mr.dataExecucao) = YEAR(CURRENT_DATE) AND MONTH(mr.dataExecucao) = MONTH(CURRENT_DATE)")
    Double sumCurrentMonthRealizedExpenses();
   
   // Método para buscar e ordenar por custo, quantidade e data de execução
    @Query("SELECT mr.dataExecucao, SUM(mr.custoReal) as totalCusto, COUNT(mr.id) as quantidade " +
           "FROM ManutencaoRealizada mr " +
           "WHERE mr.dataExecucao BETWEEN :startDate AND :endDate " +
           "GROUP BY mr.dataExecucao " +
           "ORDER BY mr.dataExecucao ASC")
    List<Object[]> findGanhoEQuantidadePorDiaAgrupado(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    // Método por buscar e ordenar por custo, quantidade e data de execução agrupado por mês
    @Query("SELECT YEAR(mr.dataExecucao) as ano, MONTH(mr.dataExecucao) as mes, " +
           "SUM(mr.custoReal) as totalCusto, COUNT(mr.id) as quantidade " +
           "FROM ManutencaoRealizada mr " +
           "WHERE mr.dataExecucao >= :startDate AND mr.dataExecucao <= :endDate " +
           "GROUP BY YEAR(mr.dataExecucao), MONTH(mr.dataExecucao) " +
           "ORDER BY ano ASC, mes ASC")
    List<Object[]> findGanhoEQuantidadePorMesAgrupado(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    // Método que encontra todas as manutenções realizadas de um veículo (ID)
    List<ManutencaoRealizada> findByVeiculoIdOrderByDataExecucaoDesc(Long veiculoId);

    @Query("SELECT mr FROM ManutencaoRealizada mr ORDER BY mr.dataExecucao DESC")
    List<ManutencaoRealizada> findRecentManutencoesRealizadas(Pageable pageable);

     // Método para a busca por palavra-chave
    @Query("SELECT mr FROM ManutencaoRealizada mr WHERE " +
           "LOWER(mr.tipoManutencao) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(mr.descricaoServicoRealizado) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(mr.veiculo.marca) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(mr.veiculo.modelo) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<ManutencaoRealizada> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);
       
}
