package com.drivecare.project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.drivecare.project.model.Maintenance;

@Repository
public interface MaintenanceRepository extends JpaRepository<Maintenance, Long> {

    List<Maintenance> findByVeiculoId(Long idVeiculo);

    @Query("SELECT m FROM Maintenance m ORDER BY m.data DESC LIMIT 5")
    List<Maintenance> findRecentMaintenances();

    @Query("SELECT m.tipo, COUNT(m) FROM Maintenance m GROUP BY m.tipo")
    List<Object[]> countByMaintenanceType();

    @Query("SELECT SUM(m.custo) FROM Maintenance m WHERE YEAR(m.data) = YEAR(CURRENT_DATE) AND MONTH(m.data) = MONTH(CURRENT_DATE)")
    Double sumMonthlyExpenses();

    @Query("SELECT m FROM Maintenance m WHERE m.proximaData BETWEEN CURRENT_DATE AND CURRENT_DATE + 30")
    List<Maintenance> findUpcomingMaintenances();
}