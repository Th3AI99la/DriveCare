package com.drivecare.project.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.drivecare.project.model.Maintenance;
import com.drivecare.project.model.ManutencaoRealizada;
import com.drivecare.project.model.Vehicle;
import com.drivecare.project.model.enums.CategoriaManutencao;
import com.drivecare.project.model.enums.StatusAgendamentoManutencao;
import com.drivecare.project.repository.MaintenanceRepository;
import com.drivecare.project.repository.ManutencaoRealizadaRepository;
import com.drivecare.project.repository.VehicleRepository;

@Service
public class DashboardService {

    private final VehicleRepository repositorioVeiculos;
    private final MaintenanceRepository repositorioManutencoes;
    private final ManutencaoRealizadaRepository repositorioManutencoesRealizadas;

    @Autowired
    public DashboardService(VehicleRepository repositorioVeiculos,
            MaintenanceRepository repositorioManutencoes,
            ManutencaoRealizadaRepository repositorioManutencoesRealizadas) {
        this.repositorioVeiculos = repositorioVeiculos;
        this.repositorioManutencoes = repositorioManutencoes;
        this.repositorioManutencoesRealizadas = repositorioManutencoesRealizadas;
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

    public List<ManutencaoRealizada> getManutencoesRealizadasRecentes() {
        Pageable topFive = PageRequest.of(0, 5);
        List<ManutencaoRealizada> recentes = repositorioManutencoesRealizadas.findRecentManutencoesRealizadas(topFive);

        System.out.println("### DEBUG [Service]: Manutenções Realizadas Recentes - Tamanho: "
                + (recentes != null ? recentes.size() : "null"));
        if (recentes != null) {
            for (ManutencaoRealizada mr : recentes) {
                System.out.println(
                        "### DEBUG [Service]: MR ID: " + mr.getId() + ", Data: " + mr.getDataExecucaoFormatada());
            }
        }
        return recentes;
    }

    public List<Maintenance> getAgendamentosPendentesOrdenados() {
        List<StatusAgendamentoManutencao> statusesPendentes = Arrays.asList(
                StatusAgendamentoManutencao.AGENDADA);
        return repositorioManutencoes.findByStatusAgendamentoInOrderByProximaDataAsc(statusesPendentes);
    }

    public double getMonthlyExpenses() {
        Double despesas = repositorioManutencoes.sumMonthlyExpenses();
        return despesas != null ? despesas : 0.0;
    }

    public List<Vehicle> getAllVehicles() {
        return repositorioVeiculos.findAll();
    }

    public Map<String, Object> getChartData() {
        Map<String, Object> dadosGrafico = new HashMap<>();

        Map<String, Object> statusVeiculos = new HashMap<>();
        statusVeiculos.put("rotulos", List.of("Em Dia", "Pendentes", "Atrasados"));
        statusVeiculos.put("valores", List.of(
                repositorioVeiculos.countByStatus("OK"),
                repositorioVeiculos.countByStatus("PENDING"),
                repositorioVeiculos.countByStatus("LATE")));
        dadosGrafico.put("dadosStatusVeiculos", statusVeiculos);

        Map<String, Object> dadosTiposManutencao = new HashMap<>();
        List<String> rotulosTipos = new ArrayList<>();
        List<Long> valoresTipos = new ArrayList<>();
        List<Object[]> tiposManutencaoQueryResult = repositorioManutencoes.countByMaintenanceType();

        if (tiposManutencaoQueryResult != null && !tiposManutencaoQueryResult.isEmpty()) {
            for (Object[] item : tiposManutencaoQueryResult) {
                if (item[0] instanceof CategoriaManutencao) {
                    rotulosTipos.add(((CategoriaManutencao) item[0]).getDisplayName());
                    valoresTipos.add((Long) item[1]);
                } else if (item[0] != null) {
                    rotulosTipos.add(item[0].toString());
                    valoresTipos.add((Long) item[1]);
                }
            }
        }
        dadosTiposManutencao.put("rotulos", rotulosTipos);
        dadosTiposManutencao.put("valores", valoresTipos);
        dadosGrafico.put("tiposManutencao", dadosTiposManutencao);

        Map<String, Object> saudeVeiculos = new HashMap<>();
        saudeVeiculos.put("rotulos", List.of("Bom", "Regular", "Ruim"));
        long totalVeiculos = getTotalVehicles();
        List<Long> valoresSaude;
        if (totalVeiculos > 0) {
            long bons = (long) (totalVeiculos * 0.7);
            long regulares = (long) (totalVeiculos * 0.2);
            long ruins = totalVeiculos - bons - regulares;
            valoresSaude = List.of(bons, regulares, ruins);
        } else {
            valoresSaude = List.of(0L, 0L, 0L);
        }
        saudeVeiculos.put("valores", valoresSaude);
        dadosGrafico.put("dadosSaudeVeiculos", saudeVeiculos);

        return dadosGrafico;
    }
}
