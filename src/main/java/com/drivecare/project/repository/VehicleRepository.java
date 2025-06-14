package com.drivecare.project.repository;

import com.drivecare.project.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    // Consulta para contar veículos por marca
    @Query("SELECT v.marca, COUNT(v) FROM Vehicle v GROUP BY v.marca ORDER BY COUNT(v) DESC")
    List<Object[]> countVehiclesByMarca();

    // Consulta para buscar veículo por placa
    Vehicle findByPlaca(String placa);
}
