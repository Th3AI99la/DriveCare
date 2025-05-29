package com.drivecare.project.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import com.drivecare.project.model.enums.CategoriaManutencao;
import com.drivecare.project.model.enums.StatusAgendamentoManutencao;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "agendamento_manutencao")
public class AgendamentoManutencao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_categoria")
    private CategoriaManutencao tipo;

    private String descricao;

    @Column(name = "orcamento")
    private Double orcamento;

    @Column(name = "creation_date")
    private LocalDate dataCriacao;

    @Column(name = "data_agendamento")
    private LocalDate dataAgendamento;

    @ManyToOne
    @JoinColumn(name = "veiculo_id")
    private Vehicle veiculo;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_agendamento")
    private StatusAgendamentoManutencao statusAgendamento;

    public AgendamentoManutencao() {
        this.dataCriacao = LocalDate.now();
        
    }

    public AgendamentoManutencao(CategoriaManutencao tipo, String descricao, Double orcamento, LocalDate dataAgendamento,
            Vehicle veiculo, StatusAgendamentoManutencao statusAgendamento) {
        this(); 
        this.tipo = tipo;
        this.descricao = descricao;
        this.orcamento = orcamento;
        this.dataAgendamento = dataAgendamento;
        this.veiculo = veiculo;
        this.statusAgendamento = statusAgendamento; 
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CategoriaManutencao getTipo() {
        return tipo;
    }

    public void setTipo(CategoriaManutencao tipo) {
        this.tipo = tipo;
    }

    public String getDescricao() { 
        return descricao;
    }

    public void setDescricao(String descricao) { 
        this.descricao = descricao;
    }

    public Double getOrcamento() { 
        return orcamento;
    }

    public void setOrcamento(Double orcamento) { 
        this.orcamento = orcamento;
    }

    public LocalDate getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDate dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public LocalDate getDataAgendamento() { 
        return dataAgendamento;
    }

    public void setDataAgendamento(LocalDate dataAgendamento) { 
        this.dataAgendamento = dataAgendamento; 
    }

    public Vehicle getVeiculo() {
        return veiculo;
    }

    public void setVeiculo(Vehicle veiculo) {
        this.veiculo = veiculo;
    }

    public StatusAgendamentoManutencao getStatusAgendamento() {
        return statusAgendamento;
    }

    public void setStatusAgendamento(StatusAgendamentoManutencao statusAgendamento) {
        this.statusAgendamento = statusAgendamento;
    }

    public String getDataCriacaoFormatada() {
        return dataCriacao != null ? dataCriacao.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "-";
    }

    public String getDataAgendamentoFormatada() { 
        return dataAgendamento != null ? dataAgendamento.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "-";
    }

   // Método para calcular os dias restantes até o agendamento
    public Long getDiasCalculados() {
        if (this.dataAgendamento == null || 
                this.statusAgendamento == StatusAgendamentoManutencao.CONCLUIDA ||
                this.statusAgendamento == StatusAgendamentoManutencao.CANCELADA) {
            return null;
        }
        LocalDate hoje = LocalDate.now();
        return ChronoUnit.DAYS.between(hoje, this.dataAgendamento); 
    }

    // Método para obter a visualização do status do agendamento
    public String getStatusVisualAgendamento() {
        if (this.statusAgendamento == StatusAgendamentoManutencao.CONCLUIDA) {
            return "CONCLUIDA_DISPLAY"; 
        }
        if (this.statusAgendamento == StatusAgendamentoManutencao.CANCELADA) {
            return "CANCELADA_DISPLAY"; 
        }

        Long dias = getDiasCalculados();
        if (dias == null) { 
            return (this.statusAgendamento == StatusAgendamentoManutencao.AGENDADA) ? "AGENDADA_SEM_DATA"
                    : "INDETERMINADO";
        }
        if (dias < 0) {
            return "ATRASADO";
        } else if (dias <= 7) { 
            return "PROXIMO";
        } else {
            return "EM_DIA";
        }
    }
}