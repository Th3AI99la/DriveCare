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
        model.addAttribute("totalVehicles", dashboardService.getTotalVehicles()); // Total de veículos
        model.addAttribute("okMaintenances", dashboardService.getOkMaintenances()); // Manutenções OK
        model.addAttribute("pendingMaintenances", dashboardService.getPendingMaintenances()); // Manutenções pendentes
        model.addAttribute("criticalAlerts", dashboardService.getCriticalAlerts()); // Alertas críticos
        model.addAttribute("monthlyExpenses", dashboardService.getMonthlyExpenses()); // Despesas mensais
        model.addAttribute("recentMaintenances", dashboardService.getRecentMaintenances()); // Manutenções recentes
        model.addAttribute("vehicles", dashboardService.getAllVehicles()); // Lista de veículos
        model.addAttribute("proximasManutencoes", dashboardService.getUpcomingMaintenances()); // Próximas manutenções
        model.addAttribute("chartData", dashboardService.getChartData()); // Dados do gráfico

        return "index";
    }
}