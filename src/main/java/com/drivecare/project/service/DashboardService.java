package com.drivecare.project.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
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

    // MÉTODO para calcular dinamicamente os status dos veículos
    public Map<String, Long> getDynamicVehicleStatusCounts() {
        Map<String, Long> counts = new HashMap<>();
        long agendamentosOkCount = 0; 
        long agendamentosPendentesCount = 0;
        long agendamentosCriticosCount = 0;

        // 1. Definir quais status de manutenção devem ser considerados para os cards
        // (AGENDADA e PENDENTE_ATRASADA, por exemplo, se PENDENTE_ATRASADA deve se crítica)
        List<StatusAgendamentoManutencao> statusesConsideradosParaCards = Arrays.asList(
                StatusAgendamentoManutencao.AGENDADA

        );

        // 2. Buscar TODAS as manutenções com os status definidos
        List<Maintenance> todasManutencoesRelevantes = repositorioManutencoes
                .findByStatusAgendamentoInOrderByProximaDataAsc(statusesConsideradosParaCards);

        // 3. Iterar sobre CADA manutenção relevante e classificá-la
        for (Maintenance manutencao : todasManutencoesRelevantes) {

            Long diasCalculados = manutencao.getDiasCalculados();

            if (diasCalculados != null) {
                if (diasCalculados < 0) {
                    agendamentosCriticosCount++; // Conta este agendamento como crítico
                } else if (diasCalculados < 20) { // Pendente (0 a 19 dias)
                    agendamentosPendentesCount++; // Conta este agendamento como pendente
                } else { // Em Dia (20 dias ou mais de folga)
                    agendamentosOkCount++; // Conta este agendamento como em dia
                }
            }

        }

        // Os nomes das chaves no Map devem corresponder ao que o Controller e o HTML
        counts.put("veiculosOk", agendamentosOkCount);
        counts.put("veiculosPendentes", agendamentosPendentesCount);
        counts.put("veiculosAtrasados", agendamentosCriticosCount);
        return counts;
    }

    public List<ManutencaoRealizada> getManutencoesRealizadasRecentes() {
        Pageable topFive = PageRequest.of(0, 5);
        List<ManutencaoRealizada> recentes = repositorioManutencoesRealizadas.findRecentManutencoesRealizadas(topFive);

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

    // Novo método para buscar dados para o gráfico de Ganhos
    public Map<String, Map<String, Object>> getDadosGraficoGanhos() {
        Map<String, Map<String, Object>> todosOsDadosDeGanhos = new HashMap<>();
        LocalDate hoje = LocalDate.now(); // referência: data atual

        // --- DADOS SEMANAIS (Semana atual) ---
        Map<String, Object> dadosSemanais = new LinkedHashMap<>(); // Usar LinkedHashMap para manter ordem das labels
        List<String> labelsSemanais = new ArrayList<>();
        List<Double> ganhosSemanais = new ArrayList<>();
        List<Long> qtdSemanais = new ArrayList<>();

        LocalDate inicioSemana = hoje.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate fimSemana = hoje.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
        Map<LocalDate, Object[]> mapaResultadosSemanais = new HashMap<>();
        List<Object[]> resultadosRepoSemanais = repositorioManutencoesRealizadas.findGanhoEQuantidadePorDiaAgrupado(inicioSemana, fimSemana);
        for (Object[] resultado : resultadosRepoSemanais) {
            mapaResultadosSemanais.put((LocalDate) resultado[0], resultado);
        }

        DateTimeFormatter formatterDiaSemana = DateTimeFormatter.ofPattern("EEE"); // Ex: Seg, Ter
        for (int i = 0; i < 7; i++) {
            LocalDate diaCorrente = inicioSemana.plusDays(i);
            labelsSemanais.add(diaCorrente.format(formatterDiaSemana));
            Object[] dadosDoDia = mapaResultadosSemanais.get(diaCorrente);
            if (dadosDoDia != null) {
                ganhosSemanais.add(dadosDoDia[1] != null ? ((Number) dadosDoDia[1]).doubleValue() : 0.0);
                qtdSemanais.add(dadosDoDia[2] != null ? ((Number) dadosDoDia[2]).longValue() : 0L);
            } else {
                ganhosSemanais.add(0.0);
                qtdSemanais.add(0L);
            }
        }
        dadosSemanais.put("labels", labelsSemanais);
        dadosSemanais.put("datasets", Arrays.asList(
                Map.of("label", "Ganhos (R$)", "data", ganhosSemanais, "borderColor", "#4CAF50", "backgroundColor", "rgba(76, 175, 80, 0.1)", "yAxisID", "yGanhos"),
                Map.of("label", "Qtd. Manutenções", "data", qtdSemanais, "borderColor", "#2196F3", "backgroundColor", "rgba(33, 150, 243, 0.1)", "yAxisID", "yQtd")
        ));
        todosOsDadosDeGanhos.put("semanal", dadosSemanais);


        // --- DADOS MENSAIS (Mês atual, detalhado por dia) ---
        Map<String, Object> dadosMensais = new LinkedHashMap<>();
        List<String> labelsMensais = new ArrayList<>();
        List<Double> ganhosMensais = new ArrayList<>();
        List<Long> qtdMensais = new ArrayList<>();

        YearMonth mesAtual = YearMonth.from(hoje);
        LocalDate inicioMes = hoje.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate fimMes = hoje.with(TemporalAdjusters.lastDayOfMonth());
        Map<LocalDate, Object[]> mapaResultadosMensais = new HashMap<>();
        List<Object[]> resultadosRepoMensais = repositorioManutencoesRealizadas.findGanhoEQuantidadePorDiaAgrupado(inicioMes, fimMes);
        for (Object[] resultado : resultadosRepoMensais) {
            mapaResultadosMensais.put((LocalDate) resultado[0], resultado);
        }

        for (int i = 1; i <= mesAtual.lengthOfMonth(); i++) {
            LocalDate diaCorrente = inicioMes.withDayOfMonth(i);
            labelsMensais.add(String.valueOf(i)); // Label: 1, 2, ..., 31
            Object[] dadosDoDia = mapaResultadosMensais.get(diaCorrente);
            if (dadosDoDia != null) {
                ganhosMensais.add(dadosDoDia[1] != null ? ((Number) dadosDoDia[1]).doubleValue() : 0.0);
                qtdMensais.add(dadosDoDia[2] != null ? ((Number) dadosDoDia[2]).longValue() : 0L);
            } else {
                ganhosMensais.add(0.0);
                qtdMensais.add(0L);
            }
        }
        dadosMensais.put("labels", labelsMensais);
        dadosMensais.put("datasets", Arrays.asList(
                Map.of("label", "Ganhos (R$)", "data", ganhosMensais, "borderColor", "#4CAF50", "backgroundColor", "rgba(76, 175, 80, 0.1)", "yAxisID", "yGanhos"),
                Map.of("label", "Qtd. Manutenções", "data", qtdMensais, "borderColor", "#2196F3", "backgroundColor", "rgba(33, 150, 243, 0.1)", "yAxisID", "yQtd")
        ));
        todosOsDadosDeGanhos.put("mensal", dadosMensais);

        // --- DADOS ANUAIS (Últimos 6 meses, agrupado por mês) ---
        Map<String, Object> dadosAnuais = new LinkedHashMap<>();
        List<String> labelsAnuais = new ArrayList<>();
        List<Double> ganhosAnuais = new ArrayList<>();
        List<Long> qtdAnuais = new ArrayList<>();

        LocalDate fimPeriodoAnual = hoje.with(TemporalAdjusters.lastDayOfMonth()); // Até o fim do mês atual
        LocalDate inicioPeriodoAnual = hoje.minusMonths(5).with(TemporalAdjusters.firstDayOfMonth()); // Início de 6 meses atrás
        
        Map<YearMonth, Object[]> mapaResultadosAnuais = new HashMap<>();
        List<Object[]> resultadosRepoAnuais = repositorioManutencoesRealizadas.findGanhoEQuantidadePorMesAgrupado(inicioPeriodoAnual, fimPeriodoAnual);

        for (Object[] resultado : resultadosRepoAnuais) {
            // Assumindo que resultado[0] é Ano (Integer/Long) e resultado[1] é Mês (Integer/Long)
            int ano = ((Number) resultado[0]).intValue();
            int mes = ((Number) resultado[1]).intValue();
            mapaResultadosAnuais.put(YearMonth.of(ano, mes), resultado);
        }
        
        DateTimeFormatter formatterMesAno = DateTimeFormatter.ofPattern("MMM/yy"); // Ex: Mai/25
        for (int i = 0; i < 6; i++) {
            YearMonth mesCorrenteLoop = YearMonth.from(inicioPeriodoAnual.plusMonths(i));
            labelsAnuais.add(mesCorrenteLoop.format(formatterMesAno));
            Object[] dadosDoMes = mapaResultadosAnuais.get(mesCorrenteLoop);
            if (dadosDoMes != null) {
                ganhosAnuais.add(dadosDoMes[2] != null ? ((Number) dadosDoMes[2]).doubleValue() : 0.0);
                qtdAnuais.add(dadosDoMes[3] != null ? ((Number) dadosDoMes[3]).longValue() : 0L);
            } else {
                ganhosAnuais.add(0.0);
                qtdAnuais.add(0L);
            }
        }
        dadosAnuais.put("labels", labelsAnuais);
        dadosAnuais.put("datasets", Arrays.asList(
                Map.of("label", "Ganhos (R$)", "data", ganhosAnuais, "borderColor", "#4CAF50", "backgroundColor", "rgba(76, 175, 80, 0.1)", "yAxisID", "yGanhos"),
                Map.of("label", "Qtd. Manutenções", "data", qtdAnuais, "borderColor", "#2196F3", "backgroundColor", "rgba(33, 150, 243, 0.1)", "yAxisID", "yQtd")
        ));
        todosOsDadosDeGanhos.put("anual", dadosAnuais);

        return todosOsDadosDeGanhos;
    }

    public Map<String, Object> getChartData() {
        Map<String, Object> dadosGrafico = new HashMap<>();

        // Dados para o gráfico "Manutenções Pendentes"
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
                    rotulosTipos.add(item[0].toString()); 
                    valoresTipos.add((Long) item[1]);
                }
            }
        }
        dadosTiposManutencao.put("rotulos", rotulosTipos.isEmpty() ? Collections.emptyList() : rotulosTipos);
        dadosTiposManutencao.put("valores", valoresTipos.isEmpty() ? Collections.emptyList() : valoresTipos);
        dadosGrafico.put("tiposManutencao", dadosTiposManutencao);


        // Dados para o gráfico "Saúde dos Veículos"

        Map<String, Object> veiculosPorMarcaData = new HashMap<>();
        List<String> marcasLabels = new ArrayList<>();
        List<Long> marcasValores = new ArrayList<>();

        List<Object[]> resultadosMarcas = repositorioVeiculos.countVehiclesByMarca(); // Chamando o novo método
        if (resultadosMarcas != null) {
            for (Object[] resultado : resultadosMarcas) {
                if (resultado[0] != null && resultado[1] != null) { // Checagem de nulos
                    marcasLabels.add((String) resultado[0]);    // Nome da marca
                    marcasValores.add(((Number) resultado[1]).longValue()); // Quantidade de veículos
                }
            }
        }

        veiculosPorMarcaData.put("rotulos", marcasLabels);
        veiculosPorMarcaData.put("valores", marcasValores);
        
        dadosGrafico.put("dadosSaudeVeiculos", veiculosPorMarcaData); 
        

        return dadosGrafico;
    }
}