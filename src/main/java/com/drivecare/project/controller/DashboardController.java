package com.drivecare.project.controller;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.drivecare.project.service.DashboardService;

@Controller
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/")
    public String dashboard(Model model) {
        model.addAttribute("totalVeiculos", dashboardService.getTotalVehicles());
        model.addAttribute("veiculosOk", dashboardService.getOkMaintenances()); // Alterado
        model.addAttribute("veiculosPendentes", dashboardService.getPendingMaintenances()); // Alterado
        model.addAttribute("veiculosAtrasados", dashboardService.getCriticalAlerts()); // Alterado
        model.addAttribute("despesasMensais", dashboardService.getMonthlyExpenses());
        model.addAttribute("manutencoesRecentes", dashboardService.getRecentMaintenances());
        model.addAttribute("veiculos", dashboardService.getAllVehicles());
        model.addAttribute("proximasManutencoes", dashboardService.getUpcomingMaintenances());

        // Adicionar os dados dos gráficos diretamente
        Map<String, Object> dadosGrafico = dashboardService.getChartData();

        model.addAttribute("statusVeiculosData", dadosGrafico.get("dadosStatusVeiculos"));
        model.addAttribute("tiposManutencaoLabels",
                ((Map<String, Object>) dadosGrafico.get("tiposManutencao")).get("rotulos"));
        model.addAttribute("tiposManutencaoData",
                ((Map<String, Object>) dadosGrafico.get("tiposManutencao")).get("valores"));
        model.addAttribute("saudeVeiculosLabels",
                ((Map<String, Object>) dadosGrafico.get("dadosSaudeVeiculos")).get("rotulos"));
        model.addAttribute("saudeVeiculosData",
                ((Map<String, Object>) dadosGrafico.get("dadosSaudeVeiculos")).get("valores"));
        return "index";
    }
}