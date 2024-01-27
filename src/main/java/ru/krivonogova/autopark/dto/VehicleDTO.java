package ru.krivonogova.autopark.dto;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;

public class VehicleDTO {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
	
    @NotEmpty(message = "Обязательное поле")
    private String registrationNumber;
    
    @Min(value = 1950, message = "Допускается добавление ТС старше 1950 г.")
    @Max(value = 2024, message = "Недопустимое значение")
    private int yearOfProduction;
	
    @Min(value = 0)
    private double price;

    @Min(value = 0)
    private double mileage;
    
    private BrandDTO brand;
    
    private EnterpriseDTO enterprise;
    
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getRegistrationNumber() {
		return registrationNumber;
	}

	public void setRegistrationNumber(String registrationNumber) {
		this.registrationNumber = registrationNumber;
	}

	public int getYearOfProduction() {
		return yearOfProduction;
	}

	public void setYearOfProduction(int yearOfProduction) {
		this.yearOfProduction = yearOfProduction;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public double getMileage() {
		return mileage;
	}

	public void setMileage(double mileage) {
		this.mileage = mileage;
	}

	public BrandDTO getBrand() {
		return brand;
	}

	public void setBrand(BrandDTO brand) {
		this.brand = brand;
	}

	public EnterpriseDTO getEnterprise() {
		return enterprise;
	}

	public void setEnterprise(EnterpriseDTO enterprise) {
		this.enterprise = enterprise;
	}
    
}
