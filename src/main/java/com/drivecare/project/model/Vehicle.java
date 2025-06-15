package com.drivecare.project.model;

import java.math.BigDecimal;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;


@Entity
public class Vehicle {

@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "vehicle_generator")
    @SequenceGenerator(name = "vehicle_generator", sequenceName = "vehicle_id_seq", allocationSize = 1)
    private Long id;

    private String marca;
    private String modelo;
    private Integer ano;
    private String placa;

    @Column(precision = 10, scale = 2)
    private BigDecimal quilometragem;

    @Column(name = "vehicle_color")
    private String cor;

    @OneToMany(mappedBy = "veiculo", cascade = CascadeType.ALL)
    private List<AgendamentoManutencao> manutencoes; 

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

    public BigDecimal getQuilometragem() {
        return quilometragem;
    }

    public void setQuilometragem(BigDecimal quilometragem) {
        this.quilometragem = quilometragem;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    public List<AgendamentoManutencao> getManutencoes() { 
        return manutencoes;
    }

     public void setManutencoes(List<AgendamentoManutencao> manutencoes) { 
        this.manutencoes = manutencoes;
    }
}