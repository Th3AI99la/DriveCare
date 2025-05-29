package com.drivecare.project.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "manutencao_realizada")
public class ManutencaoRealizada {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "veiculo_id", nullable = false)
    private Vehicle veiculo;

    @Column(name = "data_execucao", nullable = false)
    private LocalDate dataExecucao;

    @Column(name = "descricao_servico_realizado", length = 1000) // VARCHAR(1000)
    private String descricaoServicoRealizado;

    @Column(name = "tipo_manutencao")
    private String tipoManutencao;

    @Column(name = "custo_real")
    private Double custoReal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manutencao_agendada_id", nullable = true)
    private Maintenance manutencaoAgendada;

    // Construtor padrão
    public ManutencaoRealizada() {
    }

    // Construtor que inicializa todos os campos
    public ManutencaoRealizada(Vehicle veiculo, LocalDate dataExecucao, String descricaoServicoRealizado,
            String tipoManutencao, Double custoReal, Maintenance manutencaoAgendada) {
        this.veiculo = veiculo;
        this.dataExecucao = dataExecucao;
        this.descricaoServicoRealizado = descricaoServicoRealizado;
        this.tipoManutencao = tipoManutencao;
        this.custoReal = custoReal;
        this.manutencaoAgendada = manutencaoAgendada;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Vehicle getVeiculo() {
        return veiculo;
    }

    public void setVeiculo(Vehicle veiculo) {
        this.veiculo = veiculo;
    }

    public LocalDate getDataExecucao() {
        return dataExecucao;
    }

    public void setDataExecucao(LocalDate dataExecucao) {
        this.dataExecucao = dataExecucao;
    }

    public String getDescricaoServicoRealizado() {
        return descricaoServicoRealizado;
    }

    public void setDescricaoServicoRealizado(String descricaoServicoRealizado) {
        this.descricaoServicoRealizado = descricaoServicoRealizado;
    }

    public String getTipoManutencao() {
        return tipoManutencao;
    }

    public void setTipoManutencao(String tipoManutencao) {
        this.tipoManutencao = tipoManutencao;
    }

    public Double getCustoReal() {
        return custoReal;
    }

    public void setCustoReal(Double custoReal) {
        this.custoReal = custoReal;
    }

    public Maintenance getManutencaoAgendada() {
        return manutencaoAgendada;
    }

    public void setManutencaoAgendada(Maintenance manutencaoAgendada) {
        this.manutencaoAgendada = manutencaoAgendada;
    }

    public String getDataExecucaoFormatada() {
        return dataExecucao != null ? dataExecucao.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "-";
    }
}