package com.drivecare.project.model;

import java.time.LocalDate;

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

    @ManyToOne
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

    public String getType() {
        return tipo;
    }

    public void setType(String tipo) {
        this.tipo = tipo;
    }

    public String getDescription() {
        return descricao;
    }

    public void setDescription(String descricao) {
        this.descricao = descricao;
    }

    public Double getCost() {
        return custo;
    }

    public void setCost(Double custo) {
        this.custo = custo;
    }

    public LocalDate getDate() {
        return data;
    }

    public void setDate(LocalDate data) {
        this.data = data;
    }

    public LocalDate getNextDate() {
        return proximaData;
    }

    public void setNextDate(LocalDate proximaData) {
        this.proximaData = proximaData;
    }

    public Vehicle getVeiculo() {
        return veiculo;
    }

    public void setVeiculo(Vehicle veiculo) {
        this.veiculo = veiculo;
    }

}