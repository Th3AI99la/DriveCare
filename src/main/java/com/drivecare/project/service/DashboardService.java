package com.drivecare.project.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap; // Importar Arrays
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service; // Para injeção automática

import com.drivecare.project.model.Maintenance;
import com.drivecare.project.model.ManutencaoRealizada;
import com.drivecare.project.model.Vehicle; // Importar ManutencaoRealizada
import com.drivecare.project.model.enums.StatusAgendamentoManutencao;
import com.drivecare.project.repository.MaintenanceRepository; // Importar Enum StatusAgendamentoManutencao
import com.drivecare.project.repository.ManutencaoRealizadaRepository;
import com.drivecare.project.repository.VehicleRepository; // Importar ManutencaoRealizadaRepository

@Service
public class DashboardService {

    private final VehicleRepository repositorioVeiculos;
    private final MaintenanceRepository repositorioManutencoes;
    private final ManutencaoRealizadaRepository repositorioManutencoesRealizadas; // Novo repositório

    @Autowired
    public DashboardService(VehicleRepository repositorioVeiculos,
            MaintenanceRepository repositorioManutencoes,
            ManutencaoRealizadaRepository repositorioManutencoesRealizadas) {
        this.repositorioVeiculos = repositorioVeiculos;
        this.repositorioManutencoes = repositorioManutencoes;
        this.repositorioManutencoesRealizadas = repositorioManutencoesRealizadas;
    }

    // Contagem total de veículos
    public long getTotalVehicles() {
        return repositorioVeiculos.count();
    }

    // Contagem de veículos com status OK
    public long getOkMaintenances() {
        return repositorioVeiculos.countByStatus("OK");
    }

    // Contagem de veículos com manutenções pendentes
    public long getPendingMaintenances() {
        return repositorioVeiculos.countByStatus("PENDING");
    }

    // Contagem de veículos com alertas críticos (atrasados)
    public long getCriticalAlerts() {
        return repositorioVeiculos.countByStatus("LATE");
    }

    // Soma das despesas mensais estimadas (Maintenance)
    public double getMonthlyExpenses() {
        Double despesas = repositorioManutencoes.sumMonthlyExpenses();
        return despesas != null ? despesas : 0.0;
    }

    // Busca as últimas 5 manutenções realizadas (ManutencaoRealizada)
    public List<ManutencaoRealizada> getManutencoesRealizadasRecentes() {
        return repositorioManutencoesRealizadas.findTop5ByOrderByDataExecucaoDesc();
    }

    // Busca agendamentos pendentes ordenados pela próxima data
    public List<Maintenance> getAgendamentosPendentesOrdenados() {
        List<StatusAgendamentoManutencao> statusesPendentes = Arrays.asList(
                StatusAgendamentoManutencao.AGENDADA
        // Adicione outros status pendentes se necessário
        );
        return repositorioManutencoes.findByStatusAgendamentoInOrderByProximaDataAsc(statusesPendentes);
    }

    // Retorna todos os veículos
    public List<Vehicle> getAllVehicles() {
        return repositorioVeiculos.findAll();
    }

    // Dados para gráficos do dashboard
    public Map<String, Object> getChartData() {
        Map<String, Object> dadosGrafico = new HashMap<>();

        // Dados de status dos veículos
        Map<String, Object> statusVeiculos = new HashMap<>();
        statusVeiculos.put("rotulos", List.of("Em Dia", "Pendentes", "Atrasados"));
        statusVeiculos.put("valores", List.of(
                getOkMaintenances(),
                getPendingMaintenances(),
                getCriticalAlerts()));
        dadosGrafico.put("dadosStatusVeiculos", statusVeiculos);

        // Tipos de manutenção - GARANTIR QUE A ESTRUTURA SEMPRE EXISTA
        Map<String, Object> dadosTiposManutencao = new HashMap<>();
        List<String> rotulosTipos = new ArrayList<>();
        List<Long> valoresTipos = new ArrayList<>();
        List<Object[]> tiposManutencaoQueryResult = repositorioManutencoes.countByMaintenanceType();

        if (tiposManutencaoQueryResult != null && !tiposManutencaoQueryResult.isEmpty()) {
            for (Object[] item : tiposManutencaoQueryResult) {
                if (item[0] != null) { // Adicionar verificação para evitar NPE em item[0]
                    rotulosTipos.add((String) item[0]);
                    valoresTipos.add((Long) item[1]);
                }
            }
        }
        dadosTiposManutencao.put("rotulos", rotulosTipos); // Sempre adiciona 'rotulos', mesmo que lista vazia
        dadosTiposManutencao.put("valores", valoresTipos); // Sempre adiciona 'valores', mesmo que lista vazia
        dadosGrafico.put("tiposManutencao", dadosTiposManutencao); // Agora "tiposManutencao" sempre existirá

        // Dados de saúde dos veículos - GARANTIR QUE A ESTRUTURA SEMPRE EXISTA
        Map<String, Object> saudeVeiculos = new HashMap<>();
        // Se 'rotulos' for fixo, pode definir aqui, senão buscar/calcular.
        saudeVeiculos.put("rotulos", List.of("Bom", "Regular", "Ruim"));

        // Exemplo de cálculo dinâmico ou busca (substitua pela sua lógica real ou
        // valores padrão)
        long totalVeiculos = getTotalVehicles();
        List<Long> valoresSaude;
        if (totalVeiculos > 0) {
            long bons = (long) (totalVeiculos * 0.7);
            long regulares = (long) (totalVeiculos * 0.2);
            long ruins = totalVeiculos - bons - regulares;
            valoresSaude = List.of(bons, regulares, ruins);
        } else {
            valoresSaude = List.of(0L, 0L, 0L); // Valores padrão se não houver veículos
        }
        saudeVeiculos.put("valores", valoresSaude); // Sempre adiciona 'valores'
        dadosGrafico.put("dadosSaudeVeiculos", saudeVeiculos); // Agora "dadosSaudeVeiculos" sempre existirá

        return dadosGrafico;
    }
}
