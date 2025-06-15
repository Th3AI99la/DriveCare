package com.drivecare.project.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.drivecare.project.model.ManutencaoRealizada;
import com.drivecare.project.model.Vehicle;
import com.drivecare.project.repository.ManutencaoRealizadaRepository;
import com.drivecare.project.repository.VehicleRepository;

@Service
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final ManutencaoRealizadaRepository manutencaoRealizadaRepository;

    @Autowired
    public VehicleService(VehicleRepository vehicleRepository,
            ManutencaoRealizadaRepository manutencaoRealizadaRepository) {
        this.vehicleRepository = vehicleRepository;
        this.manutencaoRealizadaRepository = manutencaoRealizadaRepository;
    }

    // Lista os veiculos
    public List<Vehicle> findAllVehicles() {
        return vehicleRepository.findAll();
    }

    // Busca um veiculo

    public Page<Vehicle> search(String keyword, int pageNumber, int pageSize) {
        // Cria um objeto de paginação. A página no Spring Data começa em 0.
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);

        if (keyword != null && !keyword.trim().isEmpty()) {
            return vehicleRepository.search(keyword, pageable);
        }
        return vehicleRepository.findAll(pageable);
    }

    // Salva um novo veículo ou atualiza um existente no banco de dados
    public void saveVehicle(Vehicle vehicle) {
        vehicleRepository.save(vehicle);
    }

    // Busca um único veículo pelo seu ID.
    public Optional<Vehicle> findVehicleById(Long id) {
        return vehicleRepository.findById(id);
    }

    // Busca o histórico de manutenções de um veículo.

    public List<ManutencaoRealizada> findMaintenanceHistoryByVehicleId(Long vehicleId) {
        return manutencaoRealizadaRepository.findByVeiculoIdOrderByDataExecucaoDesc(vehicleId);
    }

    // Deletar veiculo
    public void deleteVehicleById(Long id) {
    vehicleRepository.deleteById(id);
    }

}