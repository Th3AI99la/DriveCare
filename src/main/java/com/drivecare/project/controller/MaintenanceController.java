package com.drivecare.project.controller;

import com.drivecare.project.model.ManutencaoRealizada;
import com.drivecare.project.model.Vehicle;
import com.drivecare.project.service.MaintenanceService;
import com.drivecare.project.service.VehicleService;
import org.springframework.data.domain.Page; // Correção da importação
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MaintenanceController {

    private final MaintenanceService maintenanceService;
    private final VehicleService vehicleService;
    private static final int ITENS_POR_PAGINA = 5;

    public MaintenanceController(MaintenanceService maintenanceService, VehicleService vehicleService) {
        this.maintenanceService = maintenanceService;
        this.vehicleService = vehicleService;
    }

    // Método GET para listar as manutenções com paginação
    @GetMapping("/maintenances")
    public String listarManutencao(@RequestParam(name = "pagina", defaultValue = "1") int paginaAtualParam, Model model) {
        // Lógica de paginação
        int paginaAtual = Math.max(1, paginaAtualParam);
        Page<ManutencaoRealizada> paginaDeManutencao = maintenanceService.getManutencaoPaginada(paginaAtual - 1, ITENS_POR_PAGINA);

        // Passando os dados para o modelo
        model.addAttribute("manutencao", paginaDeManutencao.getContent());
        model.addAttribute("totalContagem", paginaDeManutencao.getTotalElements());
        model.addAttribute("contagemAtual", paginaDeManutencao.getNumberOfElements());
        model.addAttribute("paginaAtual", paginaAtual);
        model.addAttribute("totalPaginas", paginaDeManutencao.getTotalPages() == 0 ? 1 : paginaDeManutencao.getTotalPages());

        return "maintenances"; // Nome da página de manutenções
    }

    // Método GET para mostrar o formulário de cadastro de manutenção
    @GetMapping("/maintenances/cadastrar")
    public String mostrarFormularioCadastroManutencao(Model model) {
        // Carregar veículos para o formulário de cadastro
        model.addAttribute("veiculos", vehicleService.getAllVehicles()); // Correção do método getAllVehicles
        model.addAttribute("manutencao", new ManutencaoRealizada()); // Novo objeto de manutenção
        return "maintenances/cadastrar"; // Página de formulário de cadastro
    }

    // Método POST para cadastrar a manutenção
    @PostMapping("/maintenances/cadastrar")
    public String cadastrarManutencao(ManutencaoRealizada manutencao, Model model) {
        try {
            maintenanceService.salvarManutencao(manutencao); // Salva no banco de dados
            model.addAttribute("message", "Manutenção cadastrada com sucesso!");
        } catch (Exception e) {
            model.addAttribute("message", "Erro ao cadastrar a manutenção.");
        }
        return "redirect:/maintenances"; // Redireciona para a lista de manutenções
    }
}
