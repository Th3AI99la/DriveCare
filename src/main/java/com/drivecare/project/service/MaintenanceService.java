package com.drivecare.project.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.drivecare.project.model.AgendamentoManutencao;
import com.drivecare.project.model.ManutencaoRealizada;
import com.drivecare.project.model.enums.StatusAgendamentoManutencao;
import com.drivecare.project.repository.AgendamentoManutencaoRepository;
import com.drivecare.project.repository.ManutencaoRealizadaRepository;


@Service
public class MaintenanceService {

    private final ManutencaoRealizadaRepository manutencaoRealizadaRepository;
    private final AgendamentoManutencaoRepository agendamentoManutencaoRepository;

    public MaintenanceService(ManutencaoRealizadaRepository manutencaoRealizadaRepository,
                            AgendamentoManutencaoRepository agendamentoManutencaoRepository) {
        this.manutencaoRealizadaRepository = manutencaoRealizadaRepository;
        this.agendamentoManutencaoRepository = agendamentoManutencaoRepository;
    }

    // Método para obter as manutenções realizadas com paginação
    public Page<AgendamentoManutencao> searchScheduled(String keyword, int pagina, int itensPorPagina) {
        Pageable pageable = PageRequest.of(pagina, itensPorPagina);
        StatusAgendamentoManutencao status = StatusAgendamentoManutencao.AGENDADA;

        if (keyword != null && !keyword.trim().isEmpty()) {
            return agendamentoManutencaoRepository.findByStatusWithKeyword(status, keyword, pageable);
        }
        return agendamentoManutencaoRepository.findByStatusAgendamento(status, pageable);
    }

    // Método para salvar uma manutenção realizada
    public void salvarManutencao(ManutencaoRealizada manutencao) {
        // Salva a manutenção no banco de dados
        manutencaoRealizadaRepository.save(manutencao);
    }

    // Método para salvar um agendamento
    public void salvarAgendamento(AgendamentoManutencao agendamento) {
        agendamentoManutencaoRepository.save(agendamento);
    }

    // Método para buscar um agendamento por ID
    public AgendamentoManutencao findAgendamentoById(Long id) {
        return agendamentoManutencaoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Agendamento não encontrado"));
    }

    // Método para buscar histórico de manutenções por ID do agendamento
    public List<ManutencaoRealizada> findMaintenanceHistoryByScheduleId(Long id) {
        return manutencaoRealizadaRepository.findByManutencaoAgendadaId(id);
    }

    // Método para deletar um agendamento por ID
    public void deleteAgendamentoById(Long id) {
        agendamentoManutencaoRepository.deleteById(id);
    }

    // Finalizar  um agendamento
    @Transactional // Garante que as duas operações (salvar e atualizar) aconteçam juntas
    public void finalizarManutencao(Long agendamentoId, String descricaoFinal, Double custoFinal, LocalDate dataExecucao) {
        // 1. Busca o agendamento original
        AgendamentoManutencao agendamento = findAgendamentoById(agendamentoId);

        // 2. Cria um novo registro de Manutenção Realizada
        ManutencaoRealizada realizada = new ManutencaoRealizada();
        realizada.setVeiculo(agendamento.getVeiculo());
        realizada.setDataExecucao(dataExecucao); 
        realizada.setDescricaoServicoRealizado(descricaoFinal);
        realizada.setTipoManutencao(agendamento.getTipo().getDisplayName());
        realizada.setCustoReal(custoFinal);
        realizada.setManutencaoAgendada(agendamento); // Link para o agendamento original

        // 3. Atualiza o status do agendamento original para CONCLUÍDA
        agendamento.setStatusAgendamento(StatusAgendamentoManutencao.CONCLUIDA);

        // 4. Salva as duas entidades no banco de dados
        manutencaoRealizadaRepository.save(realizada);
        agendamentoManutencaoRepository.save(agendamento);
    }
}
