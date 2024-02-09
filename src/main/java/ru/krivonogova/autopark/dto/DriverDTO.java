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

	@NotEmpty(message = "Обязательное поле")
	private double salary;
	
	private EnterpriseDTO enterprise;

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

}
