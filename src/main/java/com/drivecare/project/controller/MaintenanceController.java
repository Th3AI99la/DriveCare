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
import com.drivecare.project.model.enums.StatusAgendamentoManutencao;
import com.drivecare.project.service.MaintenanceService;
import com.drivecare.project.service.VehicleService;

@Controller
@RequestMapping("/maintenances")
public class MaintenanceController {

    private static final Logger logger = LoggerFactory.getLogger(MaintenanceController.class);
    private final MaintenanceService maintenanceService;
    private final VehicleService vehicleService;
    private static final int ITENS_POR_PAGINA = 15;

    @Autowired
    public MaintenanceController(MaintenanceService maintenanceService, VehicleService vehicleService) {
        this.maintenanceService = maintenanceService;
        this.vehicleService = vehicleService;
    }

    // Método GET para listar manutenções com paginação e pesquisa AGENDADA
    @GetMapping
    public String listMaintenances(@RequestParam(value = "keyword", required = false) String keyword,
                                @RequestParam(name = "pagina", defaultValue = "1") int paginaAtual,
                                Model model) {

        // Chama o novo método do serviço que busca AGENDAMENTOS
        Page<AgendamentoManutencao> agendamentosPage = maintenanceService.searchScheduled(keyword, paginaAtual - 1, ITENS_POR_PAGINA);

        // Envia a lista de agendamentos diretamente para a página
        model.addAttribute("agendamentos", agendamentosPage.getContent());
        model.addAttribute("paginaAtual", paginaAtual);
        model.addAttribute("totalPaginas", agendamentosPage.getTotalPages());
        model.addAttribute("totalAgendamentos", agendamentosPage.getTotalElements());
        model.addAttribute("keyword", keyword);

        return "maintenances";
    }

    // Método GET para mostrar o formulário de cadastro de agendamento de manutenção
    @GetMapping("/new")
    public String showNewMaintenanceForm(Model model) {
        AgendamentoManutencao agendamento = new AgendamentoManutencao();
        
        // Define o status padrão aqui
        agendamento.setStatusAgendamento(StatusAgendamentoManutencao.AGENDADA);

        List<Vehicle> vehicles = vehicleService.findAllVehicles();
        
        model.addAttribute("agendamento", agendamento);
        model.addAttribute("vehicles", vehicles);
        model.addAttribute("pageTitle", "Agendar Nova Manutenção");
        return "maintenances-form";
    }

    // Método POST para salvar um novo agendamento de manutenção
    @PostMapping("/save")
    public String saveMaintenance(@ModelAttribute("agendamento") AgendamentoManutencao agendamento,
                                  @RequestParam("veiculoId") Long veiculoId) {
        
        Vehicle veiculo = vehicleService.findVehicleById(veiculoId)
                .orElseThrow(() -> new RuntimeException("Veículo não encontrado com ID: " + veiculoId));
        
        agendamento.setVeiculo(veiculo);

        //  Garante que o status seja 'AGENDADA' antes de salvar
        agendamento.setStatusAgendamento(StatusAgendamentoManutencao.AGENDADA);
        
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

    // Método POST - para excluir um agendamento de manutenção
    @PostMapping("/delete/{id}")
    public String deleteMaintenance(@PathVariable("id") Long id) {
        maintenanceService.deleteAgendamentoById(id);
        return "redirect:/maintenances"; // Redireciona de volta para a lista de agendamentos de manutenção
    }

    //Método POST - Processa a finalização da manutenção
    @PostMapping("/finalize/{id}")
    public String finalizeMaintenance(@PathVariable("id") Long id,
                                      @RequestParam("descricaoFinal") String descricaoFinal,
                                      @RequestParam("custoFinal") Double custoFinal) {
        
        maintenanceService.finalizarManutencao(id, descricaoFinal, custoFinal);
        
        // Redireciona de volta para a mesma página de detalhes, que agora mostrará o status atualizado
        return "redirect:/maintenances/" + id;
    }
}
