package com.drivecare.project.model;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String marca;
    private String modelo;
    private Integer ano;
    private String placa;
    private Double quilometragem;

    @Column(name = "vehicle_color")
    private String cor;

    @Column(name = "next_maintenance")
    private LocalDate proximaManutencao;

    @OneToMany(mappedBy = "veiculo", cascade = CascadeType.ALL)
    private List<Maintenance> manutencoes;

    // Construtores
    public Vehicle() {
    }

    public Vehicle(String marca, String modelo, String placa, String cor) {
        this.marca = marca;
        this.modelo = modelo;
        this.placa = placa;
        this.cor = cor;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public Integer getAno() {
        return ano;
    }

    public void setAno(Integer ano) {
        this.ano = ano;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public Double getQuilometragem() {
        return quilometragem;
    }

    public void setQuilometragem(Double quilometragem) {
        this.quilometragem = quilometragem;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    public LocalDate getNextMaintenance() {
        return proximaManutencao;
    }

    public void setNextMaintenance(LocalDate proximaManutencao) {
        this.proximaManutencao = proximaManutencao;
    }

    public List<Maintenance> getManutencoes() {
        return manutencoes;
    }

    public void setManutencoes(List<Maintenance> manutencoes) {
        this.manutencoes = manutencoes;
    }
}