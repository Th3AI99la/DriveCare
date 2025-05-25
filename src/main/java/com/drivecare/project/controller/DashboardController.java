package com.drivecare.project.controller;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.drivecare.project.model.Maintenance;
import com.drivecare.project.service.DashboardService;

@Controller
public class DashboardController {

        private final DashboardService dashboardService;
        private static final int ITENS_POR_PAGINA_PROXIMAS_MANUTENCOES = 5;

        public DashboardController(DashboardService dashboardService) {
                this.dashboardService = dashboardService;
        }

        @GetMapping("/")
        public String dashboard(@RequestParam(name = "pagina", defaultValue = "1") int paginaAtual, Model model) {
                // Dados gerais
                model.addAttribute("totalVeiculos", dashboardService.getTotalVehicles());
                model.addAttribute("veiculosOk", dashboardService.getOkMaintenances());
                model.addAttribute("veiculosPendentes", dashboardService.getPendingMaintenances());
                model.addAttribute("veiculosAtrasados", dashboardService.getCriticalAlerts());
                model.addAttribute("despesasMensais", dashboardService.getMonthlyExpenses());
                model.addAttribute("manutencoesRecentes", dashboardService.getRecentMaintenances());

                // Lógica de paginação manual para "próximas manutenções"
                List<Maintenance> todasProximasManutencoes = dashboardService.getUpcomingMaintenances();
                int totalContagemProximas = todasProximasManutencoes.size();

                int totalPaginasProximas = (int) Math
                                .ceil((double) totalContagemProximas / ITENS_POR_PAGINA_PROXIMAS_MANUTENCOES);
                if (totalPaginasProximas == 0)
                        totalPaginasProximas = 1;

                if (paginaAtual < 1)
                        paginaAtual = 1;
                else if (paginaAtual > totalPaginasProximas)
                        paginaAtual = totalPaginasProximas;

                int inicio = (paginaAtual - 1) * ITENS_POR_PAGINA_PROXIMAS_MANUTENCOES;
                int fim = Math.min(inicio + ITENS_POR_PAGINA_PROXIMAS_MANUTENCOES, totalContagemProximas);
                List<Maintenance> proximasManutencoesPaginado = todasProximasManutencoes.subList(inicio, fim);

                model.addAttribute("proximasManutencoes", proximasManutencoesPaginado);
                model.addAttribute("totalContagem", totalContagemProximas);
                model.addAttribute("contagemAtual", proximasManutencoesPaginado.size());
                model.addAttribute("paginaAtual", paginaAtual);
                model.addAttribute("totalPaginas", totalPaginasProximas);

                // Dados dos gráficos
                Map<String, Object> dadosGrafico = dashboardService.getChartData();
                model.addAttribute("statusVeiculosData", dadosGrafico.get("dadosStatusVeiculos"));
                model.addAttribute("tiposManutencaoLabels",
                                ((Map<String, Object>) dadosGrafico.get("tiposManutencao")).get("rotulos"));
                model.addAttribute("tiposManutencaoData",
                                ((Map<String, Object>) dadosGrafico.get("tiposManutencao")).get("valores"));
                model.addAttribute("saudeVeiculosLabels",
                                ((Map<String, Object>) dadosGrafico.get("dadosSaudeVeiculos")).get("rotulos"));
                model.addAttribute("saudeVeiculosData",
                                ((Map<String, Object>) dadosGrafico.get("dadosSaudeVeiculos")).get("valores"));

                return "index";
        }
}
