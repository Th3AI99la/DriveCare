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
        Map<String, Object> dadosGrafico = dashboardService.getChartData();

        Object statusVeiculosDataObj = dadosGrafico.get("dadosStatusVeiculos");
        if (statusVeiculosDataObj instanceof Map) {
            model.addAttribute("statusVeiculosData", statusVeiculosDataObj);
        } else {
            model.addAttribute("statusVeiculosData", Collections.emptyMap());
        }

        Object tiposManutencaoObj = dadosGrafico.get("tiposManutencao");
        if (tiposManutencaoObj instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, Object> tiposManutencaoMap = (Map<String, Object>) tiposManutencaoObj;
            model.addAttribute("tiposManutencaoLabels", tiposManutencaoMap.get("rotulos"));
            model.addAttribute("tiposManutencaoData", tiposManutencaoMap.get("valores"));
        } else {
            model.addAttribute("tiposManutencaoLabels", Collections.emptyList());
            model.addAttribute("tiposManutencaoData", Collections.emptyList());
        }

        Object saudeVeiculosObj = dadosGrafico.get("dadosSaudeVeiculos");
        if (saudeVeiculosObj instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, Object> saudeVeiculosMap = (Map<String, Object>) saudeVeiculosObj;
            model.addAttribute("saudeVeiculosLabels", saudeVeiculosMap.get("rotulos"));
            model.addAttribute("saudeVeiculosData", saudeVeiculosMap.get("valores"));
        } else {
            model.addAttribute("saudeVeiculosLabels", Collections.emptyList());
            model.addAttribute("saudeVeiculosData", Collections.emptyList());
        }

        return "index";
    }
}