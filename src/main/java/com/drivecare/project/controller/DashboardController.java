package com.drivecare.project.controller;

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
        model.addAttribute("totalVeiculos", dashboardService.getTotalVehicles()); // Total de veículos
        model.addAttribute("manutencoesOk", dashboardService.getOkMaintenances()); // Manutenções OK
        model.addAttribute("manutencoesPendentes", dashboardService.getPendingMaintenances()); // Manutenções pendentes
        model.addAttribute("alertasCriticos", dashboardService.getCriticalAlerts()); // Alertas críticos
        model.addAttribute("despesasMensais", dashboardService.getMonthlyExpenses()); // Despesas mensais
        model.addAttribute("manutencoesRecentes", dashboardService.getRecentMaintenances()); // Manutenções recentes
        model.addAttribute("veiculos", dashboardService.getAllVehicles()); // Lista de veículos
        model.addAttribute("proximasManutencoes", dashboardService.getUpcomingMaintenances()); // Próximas manutenções
        model.addAttribute("dadosGrafico", dashboardService.getChartData()); // Dados do gráfico

        return "index";
    }
}