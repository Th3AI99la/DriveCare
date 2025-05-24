package com.drivecare.project.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.drivecare.project.model.Maintenance;
import com.drivecare.project.model.Vehicle;
import com.drivecare.project.repository.MaintenanceRepository;
import com.drivecare.project.repository.VehicleRepository;

@Service
public class DashboardService {

    private final VehicleRepository repositorioVeiculos;
    private final MaintenanceRepository repositorioManutencoes;

    public DashboardService(VehicleRepository repositorioVeiculos,
            MaintenanceRepository repositorioManutencoes) {
        this.repositorioVeiculos = repositorioVeiculos;
        this.repositorioManutencoes = repositorioManutencoes;
    }

    public long getTotalVehicles() {
        return repositorioVeiculos.count();
    }

    public long getOkMaintenances() {
        return repositorioVeiculos.countByStatus("OK");
    }

    public long getPendingMaintenances() {
        return repositorioVeiculos.countByStatus("PENDING");
    }

    public long getCriticalAlerts() {
        return repositorioVeiculos.countByStatus("LATE");
    }

    public double getMonthlyExpenses() {
        Double despesas = repositorioManutencoes.sumMonthlyExpenses();
        return despesas != null ? despesas : 0.0;
    }

    public List<Maintenance> getRecentMaintenances() {
        return repositorioManutencoes.findRecentMaintenances();
    }

    public List<Vehicle> getAllVehicles() {
        return repositorioVeiculos.findAll();
    }

    public List<Maintenance> getUpcomingMaintenances() {
        return repositorioManutencoes.findUpcomingMaintenances();
    }

    public Map<String, Object> getChartData() {
        Map<String, Object> dadosGrafico = new HashMap<>();

        // Dados para gráfico de status dos veículos
        dadosGrafico.put("totalVehicles", getTotalVehicles()); // Total de veículos
        dadosGrafico.put("okVehicles", getOkMaintenances()); // Veículos com manutenção OK
        dadosGrafico.put("pendingVehicles", getPendingMaintenances()); // Veículos com manutenção pendente
        dadosGrafico.put("lateVehicles", getCriticalAlerts()); // Veículos com manutenção atrasada

        // Dados para gráfico de tipos de manutenção
        dadosGrafico.put("maintenanceTypes", repositorioManutencoes.countByMaintenanceType()); // Contagem de tipos de manutenção

        return dadosGrafico;
    }
}