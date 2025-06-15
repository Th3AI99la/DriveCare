package com.drivecare.project.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.drivecare.project.model.Vehicle;
import com.drivecare.project.repository.VehicleRepository;

@Service
public class VehicleService {

    private final VehicleRepository vehicleRepository;

    @Autowired
    public VehicleService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    //Lista os veiculos
    public List<Vehicle> findAllVehicles() {
        return vehicleRepository.findAll();
    }

    // Busca um veiculo
    public List<Vehicle> search(String keyword) {
        if (keyword != null && !keyword.trim().isEmpty()) {
            return vehicleRepository.search(keyword);
        }
        return vehicleRepository.findAll(); 
    }
    // Salva um novo veículo ou atualiza um existente no banco de dados
        public void saveVehicle(Vehicle vehicle) {
        vehicleRepository.save(vehicle);
    }
        //Busca um único veículo pelo seu ID.
        public Optional<Vehicle> findVehicleById(Long id) {
        return vehicleRepository.findById(id);
    }
    

}