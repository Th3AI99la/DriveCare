package com.drivecare.project.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String brand;
    private String model;
    private Integer year;
    private String plate;
    private Double mileage;
    private String status; // OK, PENDING, LATE
    
    @Column(name = "next_maintenance")
    private LocalDate nextMaintenance;
    
    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL)
    private List<Maintenance> maintenances;

    // Construtores
    public Vehicle() {}

    public Vehicle(String brand, String model, String plate) {
        this.brand = brand;
        this.model = model;
        this.plate = plate;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }
    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }
    public Integer getYear() { return year; }
    public void setYear(Integer year) { this.year = year; }
    public String getPlate() { return plate; }
    public void setPlate(String plate) { this.plate = plate; }
    public Double getMileage() { return mileage; }
    public void setMileage(Double mileage) { this.mileage = mileage; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDate getNextMaintenance() { return nextMaintenance; }
    public void setNextMaintenance(LocalDate nextMaintenance) { this.nextMaintenance = nextMaintenance; }
    public List<Maintenance> getMaintenances() { return maintenances; }
    public void setMaintenances(List<Maintenance> maintenances) { this.maintenances = maintenances; }
}