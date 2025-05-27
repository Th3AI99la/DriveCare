package com.drivecare.project.controller;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.drivecare.project.model.Maintenance;
import com.drivecare.project.model.ManutencaoRealizada;
import com.drivecare.project.service.DashboardService;

@Controller
public class DashboardController {

    private final DashboardService dashboardService;
    private static final int ITENS_POR_PAGINA_AGENDAMENTOS = 5;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/")
    public String dashboard(@RequestParam(name = "pagina", defaultValue = "1") int paginaAtual, Model model) {
        // Dados gerais
        model.addAttribute("totalVeiculos", dashboardService.getTotalVehicles());
        model.addAttribute("despesasMensais", dashboardService.getMonthlyExpenses());

        // Chamando o novo método do serviço e declarando a variável
        Map<String, Long> dynamicStatusCounts = dashboardService.getDynamicVehicleStatusCounts(); // Declaração da variável
        model.addAttribute("veiculosOk", dynamicStatusCounts.get("veiculosOk"));
        model.addAttribute("veiculosPendentes", dynamicStatusCounts.get("veiculosPendentes"));
        model.addAttribute("veiculosAtrasados", dynamicStatusCounts.get("veiculosAtrasados"));

        // Buscar histórico de manutenções realizadas
        List<ManutencaoRealizada> historicoRecente = dashboardService.getManutencoesRealizadasRecentes();
        model.addAttribute("historicoManutencoesRecentes", historicoRecente);

        // Lógica de paginação para "Agendamentos Pendentes"
        List<Maintenance> todosAgendamentosPendentes = dashboardService.getAgendamentosPendentesOrdenados();
        int totalContagemAgendamentos = todosAgendamentosPendentes.size();

        int totalPaginasAgendamentos = (int) Math
                        .ceil((double) totalContagemAgendamentos / ITENS_POR_PAGINA_AGENDAMENTOS); // ITENS_POR_PAGINA_AGENDAMENTOS precisa estar definido
        if (totalPaginasAgendamentos == 0)
            totalPaginasAgendamentos = 1;

        if (paginaAtual < 1)
            paginaAtual = 1;
        else if (paginaAtual > totalPaginasAgendamentos && totalPaginasAgendamentos > 0)
            paginaAtual = totalPaginasAgendamentos;

        int inicio = (paginaAtual - 1) * ITENS_POR_PAGINA_AGENDAMENTOS;
        int fim = Math.min(inicio + ITENS_POR_PAGINA_AGENDAMENTOS, totalContagemAgendamentos);

        List<Maintenance> agendamentosPendentesPaginado = (inicio >= fim) ? // Condição simplificada para subList
                                                            Collections.emptyList() :
                                                            todosAgendamentosPendentes.subList(inicio, fim);

        model.addAttribute("agendamentosPendentes", agendamentosPendentesPaginado);
        model.addAttribute("totalContagem", totalContagemAgendamentos);
        model.addAttribute("contagemAtual", agendamentosPendentesPaginado.size());
        model.addAttribute("paginaAtual", paginaAtual);
        model.addAttribute("totalPaginas", totalPaginasAgendamentos);

        // --- DADOS DOS GRÁFICOS  ---
        // Dados para Donut e Barras (se o getChartData foi simplificado)
        Map<String, Object> outrosDadosGrafico = dashboardService.getChartData(); 
        model.addAttribute("tiposManutencaoLabels", ((Map<String, Object>) outrosDadosGrafico.getOrDefault("tiposManutencao", Collections.emptyMap())).get("rotulos"));
        model.addAttribute("tiposManutencaoData", ((Map<String, Object>) outrosDadosGrafico.getOrDefault("tiposManutencao", Collections.emptyMap())).get("valores"));
        model.addAttribute("saudeVeiculosLabels", ((Map<String, Object>) outrosDadosGrafico.getOrDefault("dadosSaudeVeiculos", Collections.emptyMap())).get("rotulos"));
        model.addAttribute("saudeVeiculosData", ((Map<String, Object>) outrosDadosGrafico.getOrDefault("dadosSaudeVeiculos", Collections.emptyMap())).get("valores"));

        // NOVO: Adicionar dados para o Gráfico de Ganhos (Line Chart)
        Map<String, Map<String, Object>> dadosGraficoGanhos = dashboardService.getDadosGraficoGanhos();
        model.addAttribute("dadosGraficoGanhos", dadosGraficoGanhos); // Passa toda a estrutura (semanal, mensal, anual)

        return "index";
    }
}
