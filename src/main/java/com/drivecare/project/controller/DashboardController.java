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
        model.addAttribute("totalVehicles", dashboardService.getTotalVehicles());
        model.addAttribute("okMaintenances", dashboardService.getOkMaintenances());
        model.addAttribute("pendingMaintenances", dashboardService.getPendingMaintenances());
        model.addAttribute("criticalAlerts", dashboardService.getCriticalAlerts());
        model.addAttribute("monthlyExpenses", dashboardService.getMonthlyExpenses());
        model.addAttribute("recentMaintenances", dashboardService.getRecentMaintenances());
        model.addAttribute("vehicles", dashboardService.getAllVehicles());
        model.addAttribute("proximasManutencoes", dashboardService.getUpcomingMaintenances());
        model.addAttribute("chartData", dashboardService.getChartData());
        
        return "index";
    }
}