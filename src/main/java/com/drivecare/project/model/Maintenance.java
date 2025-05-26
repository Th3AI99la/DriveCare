package com.drivecare.project.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import com.drivecare.project.model.enums.CategoriaManutencao;
import com.drivecare.project.model.enums.StatusAgendamentoManutencao; // << IMPORTAR NOVO ENUM

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Maintenance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING) 
    @Column(name = "tipo_categoria") 
                                    
    private CategoriaManutencao tipo; // << ALTERADO PARA O ENUM

    private String descricao; 
    private Double custo; 

    @Column(name = "creation_date")
    private LocalDate dataCriacao;

    @Column(name = "next_date")
    private LocalDate proximaData;

    @ManyToOne
    @JoinColumn(name = "veiculo_id")
    private Vehicle veiculo;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_agendamento")
    private StatusAgendamentoManutencao statusAgendamento;

    // Construtor
    public Maintenance() {
        this.dataCriacao = LocalDate.now();
        this.statusAgendamento = StatusAgendamentoManutencao.AGENDADA;
       
    }

    // Construtor ajustado
    public Maintenance(CategoriaManutencao tipo, String descricao, Double custo, LocalDate proximaData,
            Vehicle veiculo) {
        this();
        this.tipo = tipo; // << ACEITA O ENUM
        this.descricao = descricao;
        this.custo = custo;
        this.proximaData = proximaData;
        this.veiculo = veiculo;
    }

    // Getters e Setters para 'tipo' ajustados
    public CategoriaManutencao getTipo() { // << RETORNA O ENUM
        return tipo;
    }

    public void setTipo(CategoriaManutencao tipo) { // << ACEITA O ENUM
        this.tipo = tipo;
    }

    // getters e setters 
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return descricao;
    }

    public void setDescription(String descricao) {
        this.descricao = descricao;
    }

    public Double getCusto() {
        return custo;
    }

    public void setCusto(Double custo) {
        this.custo = custo;
    }

    public LocalDate getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDate dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public LocalDate getProximaData() {
        return proximaData;
    }

    public void setProximaData(LocalDate proximaData) {
        this.proximaData = proximaData;
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

    public String getProximaDataFormatada() {
        return proximaData != null ? proximaData.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "-";
    }

    public Long getDiasCalculados() {
        if (this.proximaData == null ||
                this.statusAgendamento == StatusAgendamentoManutencao.CONCLUIDA ||
                this.statusAgendamento == StatusAgendamentoManutencao.CANCELADA) {
            return null;
        }
        LocalDate hoje = LocalDate.now();
        return ChronoUnit.DAYS.between(hoje, this.proximaData);
    }

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
