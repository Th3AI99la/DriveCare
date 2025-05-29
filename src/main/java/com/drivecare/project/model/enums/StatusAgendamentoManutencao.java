package com.drivecare.project.model.enums;

// Enum que representa os status de agendamento de manutenção.
// O Enum StatusAgendamentoManutencao define os possíveis estados de um agendamento de manutenção,
// como "Agendada", "Concluída" e "Cancelada".
// ou seja, no banco de dados, ele aceitará SOMENTE esse valores: "AGENDADA", "CONCLUIDA" e "CANCELADA".

public enum StatusAgendamentoManutencao {
    AGENDADA("Agendada"),
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


