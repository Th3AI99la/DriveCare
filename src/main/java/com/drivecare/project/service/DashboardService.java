package com.drivecare.project.service;

import java.time.LocalDate;
import java.util.ArrayList;
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
    
    // Dados de status dos veículos (agora com labels)
    Map<String, Object> statusVeiculos = new HashMap<>();
    statusVeiculos.put("rotulos", List.of("Em Dia", "Pendentes", "Atrasados"));
    statusVeiculos.put("valores", List.of(
        getOkMaintenances(),
        getPendingMaintenances(),
        getCriticalAlerts()
    ));
    dadosGrafico.put("dadosStatusVeiculos", statusVeiculos);

    // Tipos de manutenção (mantém igual)
    List<Object[]> tiposManutencao = repositorioManutencoes.countByMaintenanceType();
    if (tiposManutencao != null && !tiposManutencao.isEmpty()) {
        Map<String, Object> dadosTiposManutencao = new HashMap<>();
        List<String> rotulos = new ArrayList<>();
        List<Long> valores = new ArrayList<>();
        for (Object[] item : tiposManutencao) {
            rotulos.add((String) item[0]);
            valores.add((Long) item[1]);
        }
        dadosTiposManutencao.put("rotulos", rotulos);
        dadosTiposManutencao.put("valores", valores);
        dadosGrafico.put("tiposManutencao", dadosTiposManutencao);
    }

    // Dados de saúde (agora calculados dinamicamente)
    Map<String, Object> saudeVeiculos = new HashMap<>();
    saudeVeiculos.put("rotulos", List.of("Bom", "Regular", "Ruim"));
    
    // Exemplo de cálculo dinâmico (substitua por sua lógica real)
    long totalVeiculos = getTotalVehicles();
    long bons = (long) (totalVeiculos * 0.7); // 70%
    long regulares = (long) (totalVeiculos * 0.2); // 20%
    long ruins = totalVeiculos - bons - regulares; // 10%
    
    saudeVeiculos.put("valores", List.of(bons, regulares, ruins));
    dadosGrafico.put("dadosSaudeVeiculos", saudeVeiculos);

    return dadosGrafico;
}
}