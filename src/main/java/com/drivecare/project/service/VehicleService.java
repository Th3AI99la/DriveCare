package com.drivecare.project.service;

import java.util.List;

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

    public List<Vehicle> findAllVehicles() {
        return vehicleRepository.findAll();
    }

    public List<Vehicle> search(String keyword) {
        if (keyword != null && !keyword.trim().isEmpty()) {
            return vehicleRepository.search(keyword);
        }
        return vehicleRepository.findAll(); 
    }

}