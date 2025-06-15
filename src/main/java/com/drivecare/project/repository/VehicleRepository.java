package com.drivecare.project.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.drivecare.project.dto.VehicleCardDTO;
import com.drivecare.project.model.Vehicle;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    @Query("SELECT v.marca, COUNT(v) FROM Vehicle v GROUP BY v.marca")
    List<Object[]> countVehiclesByMarca();

    @Query("""
            SELECT new com.drivecare.project.dto.VehicleCardDTO(
                v.id,
                v.marca,
                v.modelo,
                v.placa,
                v.cor,
                v.quilometragem,
                (SELECT COUNT(am.id) FROM AgendamentoManutencao am WHERE am.veiculo.id = v.id AND am.statusAgendamento IN ('AGENDADA', 'CANCELADA')),
                (SELECT COUNT(mr.id) FROM ManutencaoRealizada mr WHERE mr.veiculo.id = v.id)
            )
            FROM Vehicle v
            WHERE (:keyword IS NULL OR :keyword = '' OR 
                   LOWER(v.marca) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                   LOWER(v.modelo) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                   LOWER(v.placa) LIKE LOWER(CONCAT('%', :keyword, '%')))
            """)
    Page<VehicleCardDTO> searchWithCounts(@Param("keyword") String keyword, Pageable pageable);
}