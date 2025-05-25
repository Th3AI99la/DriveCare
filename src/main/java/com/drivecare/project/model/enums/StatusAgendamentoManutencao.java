package com.drivecare.project.model.enums;

public enum StatusAgendamentoManutencao {
    AGENDADA("Agendada"),
    PENDENTE_ATRASADA("Pendente (Atrasada)"), 
    CONCLUIDA("Concluída"), 
    CANCELADA("Cancelada");

    private final String displayName;

    StatusAgendamentoManutencao(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
// EXEMPLO DE USO 
/*  ISERIR NO BANCO DE DADOS  EXEMPLO 
INSERT INTO maintenance (
    custo, creation_date, descricao, next_date, status_agendamento, tipo, veiculo_id
) VALUES (
    390.00, '2025-05-24', 'Troca de óleo, filtro de ar e alinhamento',
    '2025-07-20', 'AGENDADA', 'Preventiva', 1
);
*/


