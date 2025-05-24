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

    private String marca; // Marca do veículo
    private String modelo; // Modelo do veículo
    private Integer ano; // Ano do veículo
    private String placa; // Placa do veículo
    private Double quilometragem; // Quilometragem do veículo
    private String status; // OK, PENDENTE, ATRASADO

    @Column(name = "next_maintenance")
    private LocalDate proximaManutencao;

    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL)
    private List<Maintenance> manutencoes;

    // Construtores
    public Vehicle() {
    }

    public Vehicle(String marca, String modelo, String placa) {
        this.marca = marca;
        this.modelo = modelo;
        this.placa = placa;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBrand() {
        return marca;
    }

    public void setBrand(String marca) {
        this.marca = marca;
    }

    public String getModel() {
        return modelo;
    }

    public void setModel(String modelo) {
        this.modelo = modelo;
    }

    public Integer getYear() {
        return ano;
    }

    public void setYear(Integer ano) {
        this.ano = ano;
    }

    public String getPlate() {
        return placa;
    }

    public void setPlate(String placa) {
        this.placa = placa;
    }

    public Double getMileage() {
        return quilometragem;
    }

    public void setMileage(Double quilometragem) {
        this.quilometragem = quilometragem;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getNextMaintenance() {
        return proximaManutencao;
    }

    public void setNextMaintenance(LocalDate proximaManutencao) {
        this.proximaManutencao = proximaManutencao;
    }

    public List<Maintenance> getMaintenances() {
        return manutencoes;
    }

    public void setMaintenances(List<Maintenance> manutencoes) {
        this.manutencoes = manutencoes;
    }
}