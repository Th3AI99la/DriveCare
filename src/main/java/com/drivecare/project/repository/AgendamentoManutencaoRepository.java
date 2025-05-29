package com.drivecare.project.repository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.drivecare.project.model.AgendamentoManutencao;
import com.drivecare.project.model.enums.StatusAgendamentoManutencao;

@Repository
public interface AgendamentoManutencaoRepository extends JpaRepository<AgendamentoManutencao, Long> {

    // Método para buscar agendamentos por ID do veículo.
    @Query("SELECT am FROM AgendamentoManutencao am WHERE am.veiculo.id = :veiculoId")
    List<AgendamentoManutencao> findByVeiculoId(@Param("veiculoId") Long veiculoId);

    // Método para buscar agendamentos por ordem de data de criação.
    @Query("SELECT am FROM AgendamentoManutencao am ORDER BY am.dataCriacao DESC")
    List<AgendamentoManutencao> findRecentCreatedMaintenances(Pageable pageable);
   
    // Metodo para buscar agendamentos com data futura dentro de um intervalo e ordená-los.
    @Query("SELECT am FROM AgendamentoManutencao am WHERE am.dataAgendamento >= CURRENT_DATE AND am.dataAgendamento <= :endDate ORDER BY am.dataAgendamento ASC")
    List<AgendamentoManutencao> findUpcomingMaintenancesByDateRange(@Param("endDate") LocalDate endDate);

    // Método para buscar agendamentos por uma lista de status e ordenados pela data de agendamento.
    @Query("SELECT am FROM AgendamentoManutencao am WHERE am.statusAgendamento IN :statuses ORDER BY am.dataAgendamento ASC")
    Page<AgendamentoManutencao> findByStatusAgendamentoInOrderByProximaDataAsc(
        @Param("statuses") Collection<StatusAgendamentoManutencao> statuses, Pageable pageable);

    // Método para buscar TODOS os agendamentos por uma lista de status, ordenados pela data de agendamento
    @Query("SELECT am FROM AgendamentoManutencao am WHERE am.statusAgendamento IN :statuses ORDER BY am.dataAgendamento ASC")
    List<AgendamentoManutencao> findAllByStatusAgendamentoInOrderByProximaDataAsc(
        @Param("statuses") Collection<StatusAgendamentoManutencao> statuses);

    // Método para contar agendamentos por tipo (CategoriaManutencao)
    @Query("SELECT am.tipo, COUNT(am) FROM AgendamentoManutencao am GROUP BY am.tipo")
    List<Object[]> countByMaintenanceType(); 

}