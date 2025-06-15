package com.drivecare.project.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.drivecare.project.model.ManutencaoRealizada;
import com.drivecare.project.model.enums.StatusAgendamentoManutencao;

// Garanta que todos os 10 campos estão aqui
public record MaintenanceDTO(
    Long id,
    String descricao,
    BigDecimal orcamento,
    LocalDate dataExecucao,
    String tipoManutencao,
    StatusAgendamentoManutencao statusAgendamento,
    Long veiculoId,
    String veiculoPlaca,
    String veiculoMarca,   
    String veiculoModelo   
) {
    // Construtor "à prova de nulos"
    public MaintenanceDTO(ManutencaoRealizada manutencao) {
        this(
            manutencao.getId(),
            manutencao.getDescricaoServicoRealizado(),
            manutencao.getCustoReal() != null ? BigDecimal.valueOf(manutencao.getCustoReal()) : null,
            manutencao.getDataExecucao(),
            manutencao.getTipoManutencao(),
            (manutencao.getManutencaoAgendada() != null) ? manutencao.getManutencaoAgendada().getStatusAgendamento() : null,
            (manutencao.getVeiculo() != null) ? manutencao.getVeiculo().getId() : null,
            (manutencao.getVeiculo() != null) ? manutencao.getVeiculo().getPlaca() : null,
            (manutencao.getVeiculo() != null) ? manutencao.getVeiculo().getMarca() : null,
            (manutencao.getVeiculo() != null) ? manutencao.getVeiculo().getModelo() : null
        );
    }
}