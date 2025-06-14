package com.drivecare.project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.drivecare.project.model.Vehicle;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    @Query("SELECT v.marca, COUNT(v) FROM Vehicle v GROUP BY v.marca")
    List<Object[]> countVehiclesByMarca();

}