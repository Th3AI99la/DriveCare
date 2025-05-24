package com.drivecare.project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.drivecare.project.model.Maintenance;

@Repository
public interface MaintenanceRepository extends JpaRepository<Maintenance, Long> {

    List<Maintenance> findByVehicleId(Long vehicleId);
    
    @Query("SELECT m FROM Maintenance m ORDER BY m.date DESC LIMIT 5")
    List<Maintenance> findRecentMaintenances();
    
    @Query("SELECT m.type, COUNT(m) FROM Maintenance m GROUP BY m.type")
    List<Object[]> countByMaintenanceType();
    
    @Query("SELECT SUM(m.cost) FROM Maintenance m WHERE YEAR(m.date) = YEAR(CURRENT_DATE) AND MONTH(m.date) = MONTH(CURRENT_DATE)")
    Double sumMonthlyExpenses();
    
    @Query("SELECT m FROM Maintenance m WHERE m.nextDate BETWEEN CURRENT_DATE AND CURRENT_DATE + 30")
    List<Maintenance> findUpcomingMaintenances();
}