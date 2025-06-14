package com.drivecare.project.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    public String listVehicles(@RequestParam(value = "keyword", required = false) String keyword, Model model) {
        List<Vehicle> vehicles = vehicleService.search(keyword);
        model.addAttribute("vehicles", vehicles);
        model.addAttribute("keyword", keyword);
        return "vehicles";
    }


    @GetMapping("/new")
    public String showNewVehicleForm(Model model) {
        Vehicle vehicle = new Vehicle();
        model.addAttribute("vehicle", vehicle);
        model.addAttribute("pageTitle", "Cadastrar Novo Veículo"); 
        return "vehicle-form"; 
    }


    @PostMapping("/save")
    public String saveVehicle(@ModelAttribute("vehicle") Vehicle vehicle) {
        vehicleService.saveVehicle(vehicle);
        return "redirect:/vehicles"; // Redireciona para a lista de veículos após salvar
    }
}