package com.drivecare.project.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit; // Importar ChronoUnit

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

    private String tipo; // Preventiva, Corretiva, Revisão
    private String descricao;
    private Double custo;
    private LocalDate data;

    @Column(name = "next_date")
    private LocalDate proximaData;

    @ManyToOne // Relacionamento com a entidade Vehicle
    @JoinColumn(name = "veiculo_id")
    private Vehicle veiculo;

    // Construtores
    public Maintenance() {
    }

    public Maintenance(String tipo, LocalDate data, Vehicle veiculo) {
        this.tipo = tipo; // Preventiva, Corretiva, Revisão
        this.data = data; // Data da manutenção
        this.veiculo = veiculo; // Veículo associado à manutenção
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    // Removido getTipo() duplicado // Comentário meu: Verifique se realmente havia um getTipo() duplicado e foi removido.

    public String getDescription() { // Mantido como getDescription para consistência com o que pode já estar em uso
        return descricao;
    }

    public void setDescription(String descricao) {
        this.descricao = descricao;
    }

    public Double getCost() { // Mantido como getCost
        return custo;
    }

    public void setCost(Double custo) {
        this.custo = custo;
    }

    public LocalDate getDate() { // Mantido como getDate
        return data;
    }

    public void setDate(LocalDate data) {
        this.data = data;
    }

    public String getDataFormatada() {
        return data != null ? data.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "-";
    }

    public LocalDate getNextDate() {
        return proximaData;
    }

    public void setNextDate(LocalDate proximaData) {
        this.proximaData = proximaData;
    }

    public String getProximaDataFormatada() {
        return proximaData != null ? proximaData.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "-";
    }

    public Vehicle getVeiculo() {
        return veiculo;
    }

    public void setVeiculo(Vehicle veiculo) {
        this.veiculo = veiculo;
    }

    public String getStatusCalculado() {
        if (this.proximaData == null) {
            return "INDETERMINADO"; // Ou um status padrão, ex: 'PENDENTE' se fizer mais sentido
        }

        LocalDate hoje = LocalDate.now();
        long diasParaProximaManutencao = ChronoUnit.DAYS.between(hoje, this.proximaData);

        if (diasParaProximaManutencao < 0) {
            return "ATRASADO"; // Já passou da data
        } else if (diasParaProximaManutencao <= 7) { // Exemplo: "Próximo" se for em até 7 dias
            return "PROXIMO";
        } else {
            return "EM_DIA";
        }
    }

    public Long getDiasCalculados() { // MUDANÇA: de long para Long (wrapper)
        if (this.proximaData == null) {
            return null; // MUDANÇA: Retorna null se não há data
        }
        LocalDate hoje = LocalDate.now();
        return ChronoUnit.DAYS.between(hoje, this.proximaData);
    }
}