package com.drivecare.project.service;

import com.drivecare.project.model.Vehicle;
import com.drivecare.project.repository.VehicleRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VehicleService {

    private final VehicleRepository vehicleRepository;

    public VehicleService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    // Método para obter uma página de veículos, com paginação e ordenação
    public Page<Vehicle> getVeiculosPaginados(int pagina, int itensPorPagina) {
        return vehicleRepository.findAll(PageRequest.of(pagina, itensPorPagina, Sort.by("id").descending()));
    }

    // Método para salvar um novo veículo
    public void salvarVeiculo(Vehicle vehicle) {
        vehicleRepository.save(vehicle);
    }

    // Método para buscar um veículo pelo ID
    public Vehicle buscarVeiculoPorId(Long id) {
        Optional<Vehicle> optionalVeiculo = vehicleRepository.findById(id);
        if (optionalVeiculo.isPresent()) {
            return optionalVeiculo.get();
        } else {
            throw new RuntimeException("Veículo não encontrado com o ID: " + id);
        }
    }

    // Método para buscar todos os veículos
    public List<Vehicle> getAllVehicles() {
        return vehicleRepository.findAll();
    }

    // Método para atualizar um veículo (caso já exista no banco)
    public Vehicle atualizarVeiculo(Long id, Vehicle vehicle) {
        if (vehicleRepository.existsById(id)) {
            vehicle.setId(id); // Garantir que o ID seja mantido
            return vehicleRepository.save(vehicle);
        } else {
            throw new RuntimeException("Veículo não encontrado para atualização com o ID: " + id);
        }
    }

    // Método para excluir um veículo
    public void excluirVeiculo(Long id) {
        if (vehicleRepository.existsById(id)) {
            vehicleRepository.deleteById(id);
        } else {
            throw new RuntimeException("Veículo não encontrado para exclusão com o ID: " + id);
        }
    }
}
