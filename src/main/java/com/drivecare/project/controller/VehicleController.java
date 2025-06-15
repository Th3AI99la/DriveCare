package com.drivecare.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.drivecare.project.model.Vehicle;
import com.drivecare.project.service.VehicleService;

import java.util.List;
import com.drivecare.project.model.ManutencaoRealizada;

@Controller
@RequestMapping("/vehicles")
public class VehicleController {

    private final VehicleService vehicleService;
    private static final int ITENS_POR_PAGINA = 30;
    

    @Autowired
    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }
    
    @GetMapping
    public String listVehicles(@RequestParam(value = "keyword", required = false) String keyword,
                               @RequestParam(name = "pagina", defaultValue = "1") int paginaAtual,
                               Model model) {

        Page<Vehicle> vehiclePage = vehicleService.search(keyword, paginaAtual, ITENS_POR_PAGINA);

        model.addAttribute("vehicles", vehiclePage.getContent());
        model.addAttribute("paginaAtual", paginaAtual);
        model.addAttribute("totalPaginas", vehiclePage.getTotalPages());
        model.addAttribute("totalVeiculos", vehiclePage.getTotalElements());
        model.addAttribute("keyword", keyword); // Mantém o keyword para os links de paginação

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


    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        // Busca o veículo no banco de dados
        Vehicle vehicle = vehicleService.findVehicleById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID do Veículo inválido:" + id));
        
        model.addAttribute("vehicle", vehicle);
        model.addAttribute("pageTitle", "Editar Veículo"); // Muda o título da página
        return "vehicle-form"; // Reutiliza o mesmo formulário
    }

    @GetMapping("/{id}")
    public String showVehicleDetails(@PathVariable("id") Long id, Model model) {
        Vehicle vehicle = vehicleService.findVehicleById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID do Veículo inválido:" + id));
        List<ManutencaoRealizada> history = vehicleService.findMaintenanceHistoryByVehicleId(id);

        model.addAttribute("vehicle", vehicle);
        model.addAttribute("maintenanceHistory", history);

        return "vehicle-details"; // Nome do novo arquivo HTML
    }


     @PostMapping("/delete/{id}")
    public String deleteVehicle(@PathVariable("id") Long id) {
        // lógica para verificar se o veículo existe antes de deletar
        vehicleService.deleteVehicleById(id); 
        return "redirect:/vehicles";
    }
}