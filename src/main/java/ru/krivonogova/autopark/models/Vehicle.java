package ru.krivonogova.autopark.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;

@Entity
@Table(name = "vehicle")
public class Vehicle {
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
	
    @Column(name = "registration_number")
    @NotEmpty(message = "Обязательное поле")
    private String registrationNumber;
    
    @Column(name = "year_of_production")
    @Min(value = 1950, message = "Допускается добавление ТС старше 1950 г.")
    @Max(value = 2024, message = "Недопустимое значение")
    private int yearOfProduction;
	
    @Column(name = "price")
    @Min(value = 0)
    private double price;

    @Column(name = "mileage")
    @Min(value = 0)
    private double mileage;
    
    @Column(name = "date_of_sale")
    private String dateOfSale;
    
    @ManyToOne
    @JoinColumn(name = "brand_id", referencedColumnName = "id")
    private Brand brand;
    
    @ManyToOne
    @JoinColumn(name = "enterprise_id", referencedColumnName = "id")
    private Enterprise enterprise;
    
    @ManyToMany(mappedBy = "vehicles")
	@JsonIgnore
    private List<Driver> drivers;
    
    @OneToOne(mappedBy = "activeVehicle")
    @JsonIgnore // add
    private Driver activeDriver;
    
    @OneToMany(mappedBy = "vehicle")
    @JsonIgnore
    private List<PointGps> PointsGps;
    
    @OneToMany(mappedBy = "vehicle")
    @JsonIgnore
    private List<Trip> trip;
    
	public Vehicle() {
	}

	public Vehicle(@NotEmpty(message = "Обязательное поле") String registrationNumber,
			@Min(value = 1950, message = "Допускается добавление ТС старше 1950 г.") @Max(value = 2024, message = "Недопустимое значение") int yearOfProduction,
			@Min(0) double price, @Min(0) double mileage, Brand brand, Enterprise enterprise) {
		this.registrationNumber = registrationNumber;
		this.yearOfProduction = yearOfProduction;
		this.price = price;
		this.mileage = mileage;
		this.brand = brand;
		this.enterprise = enterprise;
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

	public String getDateOfSale() {
		return dateOfSale;
	}

	public void setDateOfSale(String dateOfSale) {
		this.dateOfSale = dateOfSale;
	}

	public Brand getBrand() {
		return brand;
	}

	public void setBrand(Brand brand) {
		this.brand = brand;
	}

	public Enterprise getEnterprise() {
		return enterprise;
	}

	public void setEnterprise(Enterprise enterprise) {
		this.enterprise = enterprise;
	}

	public List<Driver> getDrivers() {
		return drivers;
	}

	public void setDrivers(List<Driver> drivers) {
		this.drivers = drivers;
	}

	public Driver getActiveDriver() {
		return activeDriver;
	}

	public void setActiveDriver(Driver activeDriver) {
		this.activeDriver = activeDriver;
	}

	public List<PointGps> getPointsGps() {
		return PointsGps;
	}

	public void setPointsGps(List<PointGps> pointsGps) {
		PointsGps = pointsGps;
	}

	public List<Trip> getTrip() {
		return trip;
	}

	public void setTrip(List<Trip> trip) {
		this.trip = trip;
	}
	
}
