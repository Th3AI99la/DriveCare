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

import com.drivecare.project.model.Maintenance;
import com.drivecare.project.model.enums.StatusAgendamentoManutencao;

@Repository
public interface MaintenanceRepository extends JpaRepository<Maintenance, Long> {

    // Método para buscar manutenções por ID do veículo
    List<Maintenance> findByVeiculoId(Long idVeiculo);

    // Método para buscar manutenções por ordem de data de criação
    @Query("SELECT m FROM Maintenance m ORDER BY m.dataCriacao DESC")
    List<Maintenance> findRecentCreatedMaintenances(Pageable pageable);
   
    // Metodo para buscar data mais próxima de manutenção e ordená-las pela próxima data
    @Query("SELECT m FROM Maintenance m WHERE m.proximaData >= CURRENT_DATE AND m.proximaData <= :endDate ORDER BY m.proximaData ASC")
    List<Maintenance> findUpcomingMaintenancesByDateRange(@Param("endDate") LocalDate endDate);

    // Método para buscar manutenções por status de agendamento
    @Query("SELECT m FROM Maintenance m WHERE m.statusAgendamento IN :statuses ORDER BY m.proximaData ASC")
    Page<Maintenance> findByStatusAgendamentoInOrderByProximaDataAsc(
            @Param("statuses") Collection<StatusAgendamentoManutencao> statuses,
            Pageable pageable
    );

    // Método para buscar manutenções por status de agendamento e ordená-las pela próxima data
    @Query("SELECT m FROM Maintenance m WHERE m.statusAgendamento IN :statuses ORDER BY m.proximaData ASC")
    List<Maintenance> findAllByStatusAgendamentoInOrderByProximaDataAsc(
            @Param("statuses") Collection<StatusAgendamentoManutencao> statuses
    );

    // Método para buscar manutenções por status de agendamento e ID do veículo
    @Query("SELECT m.tipo, COUNT(m) FROM Maintenance m GROUP BY m.tipo")
    List<Object[]> countByMaintenanceType();

    // Método para buscar manutenções por tipo
    @Query("SELECT SUM(m.custo) FROM Maintenance m WHERE YEAR(m.dataCriacao) = YEAR(CURRENT_DATE) AND MONTH(m.dataCriacao) = MONTH(CURRENT_DATE)")
    Double sumMonthlyExpenses(); 
}