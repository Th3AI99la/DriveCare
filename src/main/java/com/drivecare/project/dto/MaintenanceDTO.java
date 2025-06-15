package com.drivecare.project.dto;

import com.drivecare.project.model.enums.StatusAgendamentoManutencao;
import java.math.BigDecimal;
import java.time.LocalDate;

public record MaintenanceDTO(
    Long id,
    String descricao,
    BigDecimal orcamento,
    LocalDate dataExecucao,
    String tipoManutencao,
    StatusAgendamentoManutencao statusAgendamento,
    Long veiculoId,
    String veiculoPlaca
) {
    public MaintenanceDTO(
        Long id, 
        String descricao, 
        BigDecimal orcamento, 
        LocalDate dataExecucao, 
        String tipoManutencao, 
        StatusAgendamentoManutencao statusAgendamento, 
        Long veiculoId, 
        String veiculoPlaca
    ) {
        this.id = id;
        this.descricao = descricao;
        this.orcamento = orcamento;
        this.dataExecucao = dataExecucao;
        this.tipoManutencao = tipoManutencao;
        this.statusAgendamento = statusAgendamento;
        this.veiculoId = veiculoId;
        this.veiculoPlaca = veiculoPlaca;
    }

    // Construtor a partir de ManutencaoRealizada
    public MaintenanceDTO(com.drivecare.project.model.ManutencaoRealizada manutencao) {
        this(
            manutencao.getId(),
            manutencao.getDescricaoServicoRealizado(),
            BigDecimal.valueOf(manutencao.getCustoReal()),
            manutencao.getDataExecucao(),
            manutencao.getTipoManutencao(),
            manutencao.getManutencaoAgendada() != null ? manutencao.getManutencaoAgendada().getStatusAgendamento() : null,
            manutencao.getVeiculo() != null ? manutencao.getVeiculo().getId() : null,
            manutencao.getVeiculo() != null ? manutencao.getVeiculo().getPlaca() : null
        );
    }
}
