package com.drivecare.project.model.enums;

// Oque o Enum representa:
// Este Enum representa as categorias de manutenção que podem ser realizadas em veículos,
// como manutenção preventiva, corretiva e revisão. Cada categoria tem um nome de exibição associado.
// ou seja, no banco de dados, ele aceitará SOMENTE os valores "PREVENTIVA", "CORRETIVA" e "REVISAO".

public enum CategoriaManutencao {
    PREVENTIVA("Preventiva"), 
    CORRETIVA("Corretiva"),
    REVISAO("Revisão"); 

    private final String displayName;

    CategoriaManutencao(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}