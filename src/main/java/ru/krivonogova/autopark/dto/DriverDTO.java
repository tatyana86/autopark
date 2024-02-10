package ru.krivonogova.autopark.dto;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;

public class DriverDTO {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@NotEmpty(message = "Обязательное поле")
	private String name;

	private double salary;
	
	private EnterpriseDTO enterprise;
	
	private boolean isActive;
	
	private VehicleIdDTO activeVehicle;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getSalary() {
		return salary;
	}

	public void setSalary(double salary) {
		this.salary = salary;
	}

	public EnterpriseDTO getEnterprise() {
		return enterprise;
	}

	public void setEnterprise(EnterpriseDTO enterprise) {
		this.enterprise = enterprise;
	}
	
	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public VehicleIdDTO getActiveVehicle() {
		return activeVehicle;
	}

	public void setActiveVehicle(VehicleIdDTO activeVehicle) {
		this.activeVehicle = activeVehicle;
	}
}
