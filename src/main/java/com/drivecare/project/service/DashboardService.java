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

    private final VehicleRepository vehicleRepository;
    private final MaintenanceRepository maintenanceRepository;

    public DashboardService(VehicleRepository vehicleRepository,
            MaintenanceRepository maintenanceRepository) {
        this.vehicleRepository = vehicleRepository;
        this.maintenanceRepository = maintenanceRepository;
    }

    public long getTotalVehicles() {
        return vehicleRepository.count();
    }

    public long getOkMaintenances() {
        return vehicleRepository.countByStatus("OK");
    }

    public long getPendingMaintenances() {
        return vehicleRepository.countByStatus("PENDING");
    }

    public long getCriticalAlerts() {
        return vehicleRepository.countByStatus("LATE");
    }

    public double getMonthlyExpenses() {
        Double expenses = maintenanceRepository.sumMonthlyExpenses();
        return expenses != null ? expenses : 0.0;
    }

    public List<Maintenance> getRecentMaintenances() {
        return maintenanceRepository.findRecentMaintenances();
    }

    public List<Vehicle> getAllVehicles() {
        return vehicleRepository.findAll();
    }

    public List<Maintenance> getUpcomingMaintenances() {
        return maintenanceRepository.findUpcomingMaintenances();
    }

    public Map<String, Object> getChartData() {
        Map<String, Object> chartData = new HashMap<>();

        // Dados para gráfico de status dos veículos
        chartData.put("totalVehicles", getTotalVehicles()); // Total de veículos
        chartData.put("okVehicles", getOkMaintenances()); // Veículos com manutenção OK
        chartData.put("pendingVehicles", getPendingMaintenances()); // Veículos com manutenção pendente
        chartData.put("lateVehicles", getCriticalAlerts()); // Veículos com manutenção atrasada

        // Dados para gráfico de tipos de manutenção
        chartData.put("maintenanceTypes", maintenanceRepository.countByMaintenanceType()); // Contagem de tipos de manutenção

        return chartData;
    }
}