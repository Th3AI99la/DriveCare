package com.drivecare.project.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.drivecare.project.model.Vehicle;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    
    @Query("SELECT v.marca, COUNT(v) FROM Vehicle v GROUP BY v.marca")
    List<Object[]> countVehiclesByMarca();

    @Query("SELECT v FROM Vehicle v WHERE " +
           "LOWER(v.marca) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(v.modelo) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(v.placa) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Vehicle> search(@Param("keyword") String keyword, Pageable pageable);

}