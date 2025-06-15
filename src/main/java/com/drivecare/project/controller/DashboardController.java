package com.drivecare.project.controller;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails; 
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import com.drivecare.project.model.AgendamentoManutencao; 
import com.drivecare.project.model.ManutencaoRealizada;
import com.drivecare.project.model.User;
import com.drivecare.project.repository.UserRepository;
import com.drivecare.project.service.DashboardService;

@Controller
public class DashboardController {

    private final DashboardService dashboardService;
    private final UserRepository userRepository;

    private static final int ITENS_POR_PAGINA_AGENDAMENTOS = 5; 

    public DashboardController(DashboardService dashboardService, UserRepository userRepository) {
        this.dashboardService = dashboardService;
        this.userRepository = userRepository;
    }


    @ModelAttribute("currentUser")
    public User addCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails != null) {
            return userRepository.findByEmail(userDetails.getUsername()).orElse(null);
        }
        return null;
    }
    
    @GetMapping("/")
    public String dashboard(@RequestParam(name = "pagina", defaultValue = "1") int paginaAtualParam, Model model) {

        // Dados do dashboard (Total de Veículos e Ganhos Totais (mês))
        model.addAttribute("totalVeiculos", dashboardService.getTotalVehicles());
        model.addAttribute("despesasMensais", dashboardService.getMonthlyExpenses());

        // Dados de status dinâmico dos veículos
        Map<String, Long> dynamicStatusCounts = dashboardService.getDynamicVehicleStatusCounts();
        model.addAttribute("veiculosOk", dynamicStatusCounts.get("veiculosOk"));
        model.addAttribute("veiculosPendentes", dynamicStatusCounts.get("veiculosPendentes"));
        model.addAttribute("veiculosAtrasados", dynamicStatusCounts.get("veiculosAtrasados"));

        // Dados de manutenção
        List<ManutencaoRealizada> historicoRecente = dashboardService.getManutencoesRealizadasRecentes();
        model.addAttribute("historicoManutencoesRecentes", historicoRecente);

        // Lógica de paginação para "Agendamentos Pendentes"
        int paginaParaServico = Math.max(1, paginaAtualParam); 
        
        Page<AgendamentoManutencao> paginaDeAgendamentos = dashboardService.getPaginatedAgendamentosPendentes(paginaParaServico, ITENS_POR_PAGINA_AGENDAMENTOS);

        // Adiciona os dados de agendamentos pendentes ao modelo
        model.addAttribute("agendamentosPendentes", paginaDeAgendamentos.getContent());
        model.addAttribute("totalContagem", paginaDeAgendamentos.getTotalElements());
        model.addAttribute("contagemAtual", paginaDeAgendamentos.getNumberOfElements()); 
        model.addAttribute("paginaAtual", paginaParaServico);
        model.addAttribute("totalPaginas", paginaDeAgendamentos.getTotalPages() == 0 ? 1 : paginaDeAgendamentos.getTotalPages());


        // Dados adicionais para gráficos
        Map<String, Object> outrosDadosGrafico = dashboardService.getChartData(); 
        model.addAttribute("tiposManutencaoLabels", ((Map<String, Object>) outrosDadosGrafico.getOrDefault("tiposManutencao", Collections.emptyMap())).get("rotulos"));
        model.addAttribute("tiposManutencaoData", ((Map<String, Object>) outrosDadosGrafico.getOrDefault("tiposManutencao", Collections.emptyMap())).get("valores"));
        model.addAttribute("saudeVeiculosLabels", ((Map<String, Object>) outrosDadosGrafico.getOrDefault("dadosSaudeVeiculos", Collections.emptyMap())).get("rotulos"));
        Object dadosSaudeVeiculosObj = outrosDadosGrafico.getOrDefault("dadosSaudeVeiculos", Collections.emptyMap());
        Map<String, Object> dadosSaudeVeiculos = dadosSaudeVeiculosObj instanceof Map ? (Map<String, Object>) dadosSaudeVeiculosObj : Collections.emptyMap();
        model.addAttribute("saudeVeiculosData", dadosSaudeVeiculos.get("valores"));

        // Dados do gráfico de ganhos
        Map<String, Map<String, Object>> dadosGraficoGanhos = dashboardService.getDadosGraficoGanhos();
        model.addAttribute("dadosGraficoGanhos", dadosGraficoGanhos);

        return "index";
    }
}