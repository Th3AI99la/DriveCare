package com.drivecare.project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.drivecare.project.model.Vehicle;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    // Ajuste para função SQL comum ou específica do DB se necessário
    @Query("SELECT v FROM Vehicle v WHERE v.proximaManutencao BETWEEN CURRENT_DATE AND FUNCTION('ADDDATE', CURRENT_DATE, 7)")
    List<Vehicle> findVehiclesWithUpcomingMaintenance();

    Vehicle findByPlaca(String placa);
}