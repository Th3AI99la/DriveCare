package com.drivecare.project.repository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List; // Importar Collection

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.drivecare.project.model.Maintenance;
import com.drivecare.project.model.enums.StatusAgendamentoManutencao; // Importar o Enum

@Repository
public interface MaintenanceRepository extends JpaRepository<Maintenance, Long> {

    List<Maintenance> findByVeiculoId(Long idVeiculo);

    @Query("SELECT m FROM Maintenance m ORDER BY m.dataCriacao DESC")
    List<Maintenance> findRecentCreatedMaintenances(org.springframework.data.domain.Pageable pageable); // Exemplo com Pageable para limitar

    // Vamos filtrar por status no Service ou adicionar status à query.
    @Query("SELECT m FROM Maintenance m WHERE m.proximaData >= CURRENT_DATE AND m.proximaData <= :endDate ORDER BY m.proximaData ASC")
    List<Maintenance> findUpcomingMaintenancesByDateRange(@Param("endDate") LocalDate endDate);


    // MÉTODO para buscar agendamentos por uma lista de status e ordenados pela próxima data
    @Query("SELECT m FROM Maintenance m WHERE m.statusAgendamento IN :statuses ORDER BY m.proximaData ASC")
    List<Maintenance> findByStatusAgendamentoInOrderByProximaDataAsc(@Param("statuses") Collection<StatusAgendamentoManutencao> statuses);


    @Query("SELECT m.tipo, COUNT(m) FROM Maintenance m GROUP BY m.tipo")
    List<Object[]> countByMaintenanceType();

    @Query("SELECT SUM(m.custo) FROM Maintenance m WHERE YEAR(m.dataCriacao) = YEAR(CURRENT_DATE) AND MONTH(m.dataCriacao) = MONTH(CURRENT_DATE)")
    Double sumMonthlyExpenses(); 
}