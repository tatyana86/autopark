package ru.krivonogova.autopark.models;

import jakarta.persistence.*;

@Entity
@Table(name = "vehicle")
public class Vehicle {
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
	
    @Column(name = "registration_number")
    private String registrationNumber;
    
    @Column(name = "year_of_production")
    private int yearOfProduction;
	
    @Column(name = "price")
    private double price;

    @Column(name = "mileage")
    private double mileage;
    
	public Vehicle() {
	}

	public Vehicle(String registrationNumber, int yearOfProduction, double price, double mileage) {
		this.registrationNumber = registrationNumber;
		this.yearOfProduction = yearOfProduction;
		this.price = price;
		this.mileage = mileage;
	}

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
	
}
