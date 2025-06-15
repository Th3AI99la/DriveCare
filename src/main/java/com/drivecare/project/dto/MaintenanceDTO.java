package com.drivecare.project.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.drivecare.project.model.ManutencaoRealizada;
import com.drivecare.project.model.enums.StatusAgendamentoManutencao;

public record MaintenanceDTO(
    Long id, String descricao, BigDecimal orcamento, LocalDate dataExecucao,
    String tipoManutencao, StatusAgendamentoManutencao statusAgendamento,
    Long veiculoId, String veiculoPlaca, String veiculoMarca, String veiculoModelo
) {
    // Construtor customizado "à prova de nulos"
    public MaintenanceDTO(ManutencaoRealizada manutencao) {
        this(
            manutencao.getId(),
            manutencao.getDescricaoServicoRealizado(),
            // VERIFICA SE O CUSTO É NULO
            manutencao.getCustoReal() != null ? BigDecimal.valueOf(manutencao.getCustoReal()) : null,
            manutencao.getDataExecucao(),
            manutencao.getTipoManutencao(),
            // VERIFICA SE O AGENDAMENTO EXISTE
            (manutencao.getManutencaoAgendada() != null) ? manutencao.getManutencaoAgendada().getStatusAgendamento() : null,
            // VERIFICA SE O VEÍCULO EXISTE
            (manutencao.getVeiculo() != null) ? manutencao.getVeiculo().getId() : null,
            (manutencao.getVeiculo() != null) ? manutencao.getVeiculo().getPlaca() : null,
            (manutencao.getVeiculo() != null) ? manutencao.getVeiculo().getMarca() : null,
            (manutencao.getVeiculo() != null) ? manutencao.getVeiculo().getModelo() : null
        );
    }
}