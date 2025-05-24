package com.drivecare.project.service;

import java.time.LocalDate;
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
        // O status para alertas críticos/atrasados no seu código é "LATE"
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

    // Método corrigido
    public List<Maintenance> getUpcomingMaintenances() {
        LocalDate today = LocalDate.now();
        LocalDate futureDate = today.plusDays(30); // Calcula a data 30 dias no futuro
        // Assumindo que MaintenanceRepository.findUpcomingMaintenances agora aceita
        // LocalDate como parâmetro
        return repositorioManutencoes.findUpcomingMaintenances(futureDate);
    }

    public Map<String, Object> getChartData() {
        Map<String, Object> dadosGrafico = new HashMap<>();

        // Dados para gráfico de status dos veículos
        Map<String, Long> statusCounts = new HashMap<>();
        statusCounts.put("OK", getOkMaintenances());
        statusCounts.put("PENDING", getPendingMaintenances());
        statusCounts.put("LATE", getCriticalAlerts());

        Map<String, Object> statusVeiculos = new HashMap<>();
        statusVeiculos.put("valores", List.of(getOkMaintenances(), getPendingMaintenances(), getCriticalAlerts()));

        dadosGrafico.put("dadosStatusVeiculos", statusVeiculos);

        dadosGrafico.put("tiposManutencao", repositorioManutencoes.countByMaintenanceType());

        // Dados para gráfico de saúde (exemplo - ajuste conforme sua lógica)
        Map<String, Object> saudeVeiculos = new HashMap<>();
        saudeVeiculos.put("rotulos", List.of("Bom", "Regular", "Ruim"));
        // Estes são valores de exemplo, você precisará de uma lógica para calculá-los
        saudeVeiculos.put("valores", List.of(70, 20, 10));
        dadosGrafico.put("dadosSaudeVeiculos", saudeVeiculos);

        return dadosGrafico;
    }
}