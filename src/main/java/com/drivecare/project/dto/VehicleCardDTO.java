package com.drivecare.project.dto;

import java.math.BigDecimal;

public record VehicleCardDTO(
    Long id,
    String marca,
    String modelo,
    String placa,
    String cor,
    BigDecimal quilometragem,
    Long agendamentosCount, 
    Long historicoCount    
) {
}