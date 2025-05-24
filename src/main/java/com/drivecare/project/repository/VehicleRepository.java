package com.drivecare.project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.drivecare.project.model.Vehicle;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    List<Vehicle> findByStatus(String status);
    
    @Query("SELECT COUNT(v) FROM Vehicle v WHERE v.status = :status")
    Long countByStatus(String status);
    
    @Query("SELECT v FROM Vehicle v WHERE v.proximaManutencao BETWEEN CURRENT_DATE AND CURRENT_DATE + 7")
    List<Vehicle> findVehiclesWithUpcomingMaintenance();
    
    Vehicle findByPlate(String placa);
}