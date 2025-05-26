package com.drivecare.project.model.enums;

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