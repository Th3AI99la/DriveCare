package com.drivecare.project.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

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

@Entity
public class Maintenance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tipo;
    private String descricao; // Descrição do que está planejado
    private Double custo;     // Custo estimado

    @Column(name = "creation_date") // Renomeando 'data' para ser mais claro
    private LocalDate dataCriacao;

    @Column(name = "next_date")
    private LocalDate proximaData;

    @ManyToOne
    @JoinColumn(name = "veiculo_id")
    private Vehicle veiculo;

    // NOVO CAMPO DE STATUS PARA O AGENDAMENTO
    @Enumerated(EnumType.STRING)
    @Column(name = "status_agendamento")
    private StatusAgendamentoManutencao statusAgendamento;

    // Construtor
    public Maintenance() {
        this.dataCriacao = LocalDate.now();
        this.statusAgendamento = StatusAgendamentoManutencao.AGENDADA; // Padrão
    }

    // Construtor ajustado
    public Maintenance(String tipo, String descricao, Double custo, LocalDate proximaData, Vehicle veiculo) {
        this(); // Chama o construtor padrão
        this.tipo = tipo;
        this.descricao = descricao;
        this.custo = custo;
        this.proximaData = proximaData;
        this.veiculo = veiculo;
    }

    // Getters e Setters (incluindo para statusAgendamento)
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public String getDescription() { return descricao; }
    public void setDescription(String descricao) { this.descricao = descricao; }
    public Double getCusto() { return custo; }
    public void setCusto(Double custo) { this.custo = custo; }
    public LocalDate getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDate dataCriacao) { this.dataCriacao = dataCriacao; }
    public LocalDate getProximaData() { return proximaData; }
    public void setProximaData(LocalDate proximaData) { this.proximaData = proximaData; }
    public Vehicle getVeiculo() { return veiculo; }
    public void setVeiculo(Vehicle veiculo) { this.veiculo = veiculo; }
    public StatusAgendamentoManutencao getStatusAgendamento() { return statusAgendamento; }
    public void setStatusAgendamento(StatusAgendamentoManutencao statusAgendamento) { this.statusAgendamento = statusAgendamento; }

    // Métodos utilitários
    public String getDataCriacaoFormatada() { // Renomeado de getDataFormatada
        return dataCriacao != null ? dataCriacao.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "-";
    }
    public String getProximaDataFormatada() {
        return proximaData != null ? proximaData.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "-";
    }

    // Este método agora calcula dias restantes para agendamentos PENDENTES
    public Long getDiasCalculados() {
        if (this.proximaData == null || 
            this.statusAgendamento == StatusAgendamentoManutencao.CONCLUIDA ||
            this.statusAgendamento == StatusAgendamentoManutencao.CANCELADA) {
            return null;
        }
        LocalDate hoje = LocalDate.now();
        return ChronoUnit.DAYS.between(hoje, this.proximaData);
    }

    // Este método define o status visual para a UI (ex: para cores de badges)
    // para agendamentos PENDENTES
    public String getStatusVisualAgendamento() {
        if (this.statusAgendamento == StatusAgendamentoManutencao.CONCLUIDA) {
            return "CONCLUIDA_DISPLAY"; // Ex: para não mostrar na lista de pendentes
        }
        if (this.statusAgendamento == StatusAgendamentoManutencao.CANCELADA) {
            return "CANCELADA_DISPLAY";
        }

        Long dias = getDiasCalculados();
        if (dias == null) { // Se não tem próxima data mas está agendada
            return (this.statusAgendamento == StatusAgendamentoManutencao.AGENDADA) ? "AGENDADA_SEM_DATA" : "INDETERMINADO";
        }

        if (dias < 0) {
            // Opcional: atualizar this.statusAgendamento para PENDENTE_ATRASADA aqui se desejar persistir este estado.
            return "ATRASADO";
        } else if (dias <= 7) {
            return "PROXIMO";
        } else {
            return "EM_DIA"; // Agendada e em dia
        }
    }
}