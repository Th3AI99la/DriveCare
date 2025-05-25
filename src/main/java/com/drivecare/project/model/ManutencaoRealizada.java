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
import jakarta.persistence.Lob;
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
    private Vehicle veiculo; // Mapeia para a coluna 'veiculo_id' (BIGINT)

    @Column(name = "data_execucao", nullable = false)
    private LocalDate dataExecucao; // Mapeia para 'data_execucao' (DATE) - IMPORTANTE PARA O REPOSITÓRIO

    @Lob
    @Column(name = "descricao_servico_realizado", columnDefinition = "TEXT")
    private String descricaoServicoRealizado; // Mapeia para 'descricao_servico_realizado' (TEXT) - CORRETO COMO STRING

    @Column(name = "tipo_manutencao")
    private String tipoManutencao; // Mapeia para 'tipo_manutencao' (VARCHAR)

    @Column(name = "custo_real")
    private Double custoReal; // Mapeia para 'custo_real' (DOUBLE PRECISION)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manutencao_agendada_id", nullable = true)
    private Maintenance manutencaoAgendada; // Mapeia para 'manutencao_agendada_id' (BIGINT)

    // Construtor padrão
    public ManutencaoRealizada() {
    }

    // Construtor ajustado (sem localOficina e notasAdicionais, mas com
    // descricaoServicoRealizado)
    public ManutencaoRealizada(Vehicle veiculo, LocalDate dataExecucao, String descricaoServicoRealizado,
            String tipoManutencao, Double custoReal, Maintenance manutencaoAgendada) {
        this.veiculo = veiculo;
        this.dataExecucao = dataExecucao;
        this.descricaoServicoRealizado = descricaoServicoRealizado;
        this.tipoManutencao = tipoManutencao;
        this.custoReal = custoReal;
        this.manutencaoAgendada = manutencaoAgendada;
    }

    // Getters e Setters para TODOS os campos acima
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

    public LocalDate getDataExecucao() { // Getter para dataExecucao
        return dataExecucao;
    }

    public void setDataExecucao(LocalDate dataExecucao) { // Setter para dataExecucao
        this.dataExecucao = dataExecucao;
    }

    public String getDescricaoServicoRealizado() { // Getter para descricaoServicoRealizado (String)
        return descricaoServicoRealizado;
    }

    public void setDescricaoServicoRealizado(String descricaoServicoRealizado) { // Setter para
                                                                                 // descricaoServicoRealizado (String)
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