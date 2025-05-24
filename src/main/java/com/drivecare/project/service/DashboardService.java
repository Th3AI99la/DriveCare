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
    Map<String, Object> statusVeiculos = new HashMap<>();
    statusVeiculos.put("valores", List.of(getOkMaintenances(), getPendingMaintenances(), getCriticalAlerts()));
    dadosGrafico.put("dadosStatusVeiculos", statusVeiculos);

    // Dados para gráfico de tipos de manutenção
    dadosGrafico.put("tiposManutencao", repositorioManutencoes.countByMaintenanceType());

    // Dados para gráfico de saúde (exemplo - ajuste conforme sua lógica)
    Map<String, Object> saudeVeiculos = new HashMap<>();
    saudeVeiculos.put("rotulos", List.of("Bom", "Regular", "Ruim"));
    saudeVeiculos.put("valores", List.of(70, 20, 10));
    dadosGrafico.put("dadosSaudeVeiculos", saudeVeiculos);

    return dadosGrafico;
}
}