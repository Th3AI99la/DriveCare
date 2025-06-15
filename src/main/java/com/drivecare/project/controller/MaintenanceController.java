package com.drivecare.project.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import com.drivecare.project.dto.MaintenanceDTO;
import com.drivecare.project.model.AgendamentoManutencao;
import com.drivecare.project.model.ManutencaoRealizada;
import com.drivecare.project.model.Vehicle;
import com.drivecare.project.service.MaintenanceService;
import com.drivecare.project.service.VehicleService;

@Controller
@RequestMapping("/maintenances")
public class MaintenanceController {

    private static final Logger logger = LoggerFactory.getLogger(MaintenanceController.class);
    private final MaintenanceService maintenanceService;
    private final VehicleService vehicleService;
    private static final int ITENS_POR_PAGINA = 30;

    @Autowired
    public MaintenanceController(MaintenanceService maintenanceService, VehicleService vehicleService) {
        this.maintenanceService = maintenanceService;
        this.vehicleService = vehicleService;
    }

    // Método GET para listar manutenções com paginação e pesquisa

    @GetMapping
    public String listMaintenances(@RequestParam(value = "keyword", required = false) String keyword,
                                @RequestParam(name = "pagina", defaultValue = "1") int paginaAtual,
                                Model model) {

        Page<ManutencaoRealizada> maintenancePage = maintenanceService.search(keyword, paginaAtual - 1, ITENS_POR_PAGINA);

        List<MaintenanceDTO> maintenanceDtos = maintenancePage.map(MaintenanceDTO::new).getContent();

        model.addAttribute("maintenanceDtos", maintenanceDtos);
        model.addAttribute("paginaAtual", paginaAtual);
        model.addAttribute("totalPaginas", maintenancePage.getTotalPages());
        model.addAttribute("totalManutencao", maintenancePage.getTotalElements());
        model.addAttribute("keyword", keyword);

        return "maintenances";
    }

    // Método GET para mostrar o formulário de cadastro de agendamento de manutenção
    @GetMapping("/new")
    public String showNewMaintenanceForm(Model model) {
        logger.debug("Acessando formulário de nova manutenção");
        AgendamentoManutencao agendamento = new AgendamentoManutencao();
        List<Vehicle> vehicles = vehicleService.findAllVehicles();
        
        model.addAttribute("agendamento", agendamento);
        model.addAttribute("vehicles", vehicles);
        model.addAttribute("pageTitle", "Cadastrar Novo Agendamento de Manutenção");
        logger.debug("Retornando template maintenances-form");
        return "maintenances-form";
    }

    // Método POST para salvar um novo agendamento de manutenção
    @PostMapping("/save")
    public String saveMaintenance(@ModelAttribute("agendamento") AgendamentoManutencao agendamento,
                                @RequestParam("veiculo.id") Long veiculoId) { // Pega o ID do veículo separadamente
        
        // Busca o veículo completo no banco de dados
        Vehicle veiculo = vehicleService.findVehicleById(veiculoId)
                .orElseThrow(() -> new RuntimeException("Veículo não encontrado"));
        
        // Associa o veículo completo ao agendamento
        agendamento.setVeiculo(veiculo);
        
        // Salva o agendamento
        maintenanceService.salvarAgendamento(agendamento);
        return "redirect:/maintenances";
    }

    // Método GET para exibir o formulário de edição de agendamento de manutenção
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        logger.debug("Acessando formulário de edição de manutenção para ID: {}", id);
        AgendamentoManutencao agendamento = maintenanceService.findAgendamentoById(id);
        List<Vehicle> vehicles = vehicleService.findAllVehicles();
        
        model.addAttribute("agendamento", agendamento);
        model.addAttribute("vehicles", vehicles);
        model.addAttribute("pageTitle", "Editar Agendamento de Manutenção");
        logger.debug("Retornando template maintenances-form");
        return "maintenances-form";
    }

    // Método GET para exibir os detalhes de um agendamento de manutenção
    @GetMapping("/{id}")
    public String showMaintenanceDetails(@PathVariable("id") Long id, Model model) {
        AgendamentoManutencao agendamento = maintenanceService.findAgendamentoById(id);
        List<ManutencaoRealizada> maintenanceHistory = maintenanceService.findMaintenanceHistoryByScheduleId(id);

        List<MaintenanceDTO> maintenanceDtos = maintenanceHistory.stream()
            .map(MaintenanceDTO::new)
            .toList();

        model.addAttribute("agendamento", agendamento);
        model.addAttribute("maintenanceHistory", maintenanceDtos);

        return "maintenances-details";
    }

    // Método POST para excluir um agendamento de manutenção
    @PostMapping("/delete/{id}")
    public String deleteMaintenance(@PathVariable("id") Long id) {
        maintenanceService.deleteAgendamentoById(id);
        return "redirect:/maintenances"; // Redireciona de volta para a lista de agendamentos de manutenção
    }
}
