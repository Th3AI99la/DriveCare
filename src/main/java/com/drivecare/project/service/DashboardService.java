package com.drivecare.project.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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

    // NOVO MÉTODO para calcular dinamicamente os status dos veículos
    public Map<String, Long> getDynamicVehicleStatusCounts() {
        Map<String, Long> counts = new HashMap<>();
        long okCount = 0;
        long pendingCount = 0;
        long criticalCount = 0;

        // 1. Buscar todos os veículos
        List<Vehicle> allVehicles = repositorioVeiculos.findAll();

        // 2. - Buscar todas as manutenções AGENDADAS para otimizar
        List<Maintenance> scheduledMaintenances = repositorioManutencoes
                .findByStatusAgendamentoInOrderByProximaDataAsc(
                        Collections.singletonList(StatusAgendamentoManutencao.AGENDADA)); //

        // 3. Mapear a próxima manutenção agendada por ID do veículo
        Map<Long, Maintenance> nextMaintenancePerVehicle = new HashMap<>();
        for (Maintenance m : scheduledMaintenances) {
            if (m.getVeiculo() != null && m.getProximaData() != null) {
                Long vehicleId = m.getVeiculo().getId();
                // Se o veículo ainda não está no mapa ou a manutenção atual é mais próxima
                if (!nextMaintenancePerVehicle.containsKey(vehicleId) ||
                        m.getProximaData().isBefore(nextMaintenancePerVehicle.get(vehicleId).getProximaData())) {
                    nextMaintenancePerVehicle.put(vehicleId, m);
                }
            }
        }

        // 4. Iterar sobre os veículos e classificar com base na próxima manutenção
        for (Vehicle vehicle : allVehicles) {
            // DEBUG: Exibir o ID do veículo sendo processado
            System.out.println("Processando Veículo ID: " + vehicle.getId());
            Maintenance nextMaintenance = nextMaintenancePerVehicle.get(vehicle.getId());
    
            if (nextMaintenance != null) {
                System.out.println("  Próxima Manutenção ID: " + nextMaintenance.getId() + ", Próxima Data: " + nextMaintenance.getProximaData() + ", Dias Calculados: " + nextMaintenance.getDiasCalculados());
                Long diasCalculados = nextMaintenance.getDiasCalculados();

                if (diasCalculados != null) {
                    if (diasCalculados < 0) {
                        criticalCount++; // Atrasado
                    } else if (diasCalculados < 20) { // Pendente (0 a 19 dias)
                        pendingCount++;
                    } else { // Em Dia (20 dias ou mais de folga)
                        okCount++;
                    }
                } else {
                    okCount++;
                }
            } else {
                System.out.println("  Sem manutenção AGENDADA encontrada.");
                okCount++;
            }
        }

        counts.put("veiculosOk", okCount);
        counts.put("veiculosPendentes", pendingCount);
        counts.put("veiculosAtrasados", criticalCount);
        return counts;
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
    // Método para obter as manutenções agendadas pendentes, ordenadas pela próxima data
    public List<Maintenance> getAgendamentosPendentesOrdenados() {
        List<StatusAgendamentoManutencao> statusesPendentes = Arrays.asList(
            // Statuses que indicam pendências (AGENDADA e PENDENTE_ATRASADA)
                StatusAgendamentoManutencao.AGENDADA);
        return repositorioManutencoes.findByStatusAgendamentoInOrderByProximaDataAsc(statusesPendentes);
    }

    public double getMonthlyExpenses() {
        Double despesas = repositorioManutencoesRealizadas.sumCurrentMonthRealizedExpenses(); 
        return despesas != null ? despesas : 0.0;
    }

    public List<Vehicle> getAllVehicles() {
        return repositorioVeiculos.findAll();
    }

    public Map<String, Object> getChartData() {
        Map<String, Object> dadosGrafico = new HashMap<>();

        // 1. Dados para o gráfico "Status dos Veículos" usando a nova lógica dinâmica
        Map<String, Object> statusVeiculos = new HashMap<>();
        statusVeiculos.put("rotulos", List.of("Em Dia", "Pendentes", "Atrasados")); // Rótulos permanecem

        // Reutilizar a lógica dinâmica já implementada em getDynamicVehicleStatusCounts()
        Map<String, Long> dynamicCounts = getDynamicVehicleStatusCounts(); 

        statusVeiculos.put("valores", List.of(
                dynamicCounts.getOrDefault("veiculosOk", 0L),
                dynamicCounts.getOrDefault("veiculosPendentes", 0L),
                dynamicCounts.getOrDefault("veiculosAtrasados", 0L)
        ));
        dadosGrafico.put("dadosStatusVeiculos", statusVeiculos);

        // 2. Dados para o gráfico "Tipos de Manutenção" (lógica existente, sem alterações aqui)
        Map<String, Object> dadosTiposManutencao = new HashMap<>();
        List<String> rotulosTipos = new ArrayList<>();
        List<Long> valoresTipos = new ArrayList<>();
        List<Object[]> tiposManutencaoQueryResult = repositorioManutencoes.countByMaintenanceType();

        if (tiposManutencaoQueryResult != null && !tiposManutencaoQueryResult.isEmpty()) {
            for (Object[] item : tiposManutencaoQueryResult) {
                if (item[0] instanceof CategoriaManutencao categoriaManutencao) {
                    rotulosTipos.add(categoriaManutencao.getDisplayName());
                    valoresTipos.add((Long) item[1]);
                } else if (item[0] != null) {
                    // Fallback caso o tipo não seja um Enum (improvável com a query atual)
                    rotulosTipos.add(item[0].toString()); 
                    valoresTipos.add((Long) item[1]);
                }
            }
        }
        // Garantir que, mesmo sem dados, as listas existam para o Thymeleaf/JS
        dadosTiposManutencao.put("rotulos", rotulosTipos.isEmpty() ? Collections.emptyList() : rotulosTipos);
        dadosTiposManutencao.put("valores", valoresTipos.isEmpty() ? Collections.emptyList() : valoresTipos);
        dadosGrafico.put("tiposManutencao", dadosTiposManutencao);


        // 3. Dados para o gráfico "Saúde dos Veículos" (lógica existente, sem alterações aqui)
        Map<String, Object> saudeVeiculos = new HashMap<>();
        saudeVeiculos.put("rotulos", List.of("Bom", "Regular", "Ruim")); // Exemplo de rótulos
        long totalVeiculos = getTotalVehicles(); // getTotalVehicles() continua válido
        List<Long> valoresSaude;
        if (totalVeiculos > 0) {
            // Lógica de exemplo para saúde (você pode ajustar conforme suas regras)
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
