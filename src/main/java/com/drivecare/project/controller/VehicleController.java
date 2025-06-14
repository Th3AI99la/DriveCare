package com.drivecare.project.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.drivecare.project.model.Vehicle;
import com.drivecare.project.service.VehicleService;

@Controller
@RequestMapping("/vehicles")
public class VehicleController {

    private final VehicleService vehicleService;

    @Autowired
    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @GetMapping
    public String listVehicles(Model model) {
        List<Vehicle> vehicles = vehicleService.findAllVehicles();
        model.addAttribute("vehicles", vehicles);
        return "vehicles"; // Renderiza o arquivo templates/vehicles.html
    }
}