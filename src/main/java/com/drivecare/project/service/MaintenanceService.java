package com.drivecare.project.service;

import com.drivecare.project.model.ManutencaoRealizada;
import com.drivecare.project.repository.ManutencaoRealizadaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class MaintenanceService {

    private final ManutencaoRealizadaRepository manutencaoRealizadaRepository;

    public MaintenanceService(ManutencaoRealizadaRepository manutencaoRealizadaRepository) {
        this.manutencaoRealizadaRepository = manutencaoRealizadaRepository;
    }

    // Método para obter as manutenções realizadas com paginação
    public Page<ManutencaoRealizada> getManutencaoPaginada(int pagina, int itensPorPagina) {
        return manutencaoRealizadaRepository.findAll(PageRequest.of(pagina, itensPorPagina));
    }

    // Método para salvar uma manutenção realizada
    public void salvarManutencao(ManutencaoRealizada manutencao) {
        // Salva a manutenção no banco de dados
        manutencaoRealizadaRepository.save(manutencao);
    }
}
