package com.drivecare.project.service;

import com.drivecare.project.model.AgendamentoManutencao;
import com.drivecare.project.model.ManutencaoRealizada;
import com.drivecare.project.repository.AgendamentoManutencaoRepository;
import com.drivecare.project.repository.ManutencaoRealizadaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;

import java.util.List;

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
    public Page<ManutencaoRealizada> search(String keyword, int pagina, int itensPorPagina) {
        Pageable pageable = PageRequest.of(pagina, itensPorPagina);
        if (keyword != null && !keyword.trim().isEmpty()) {
            // Precisaremos criar este método no repositório
            return manutencaoRealizadaRepository.searchByKeyword(keyword, pageable);
        }
        return manutencaoRealizadaRepository.findAll(pageable);
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
}
