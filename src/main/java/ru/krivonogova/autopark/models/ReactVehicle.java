package ru.krivonogova.autopark.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import reactor.core.publisher.Mono;

@Entity
@Table(name = "vehicle")
public class ReactVehicle {
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
	
    @Column(name = "registration_number")
    @NotEmpty(message = "Обязательное поле")
    private Mono<String> registrationNumber;
    
    @Column(name = "year_of_production")
    @Min(value = 1950, message = "Допускается добавление ТС старше 1950 г.")
    @Max(value = 2024, message = "Недопустимое значение")
    private Mono<Integer> yearOfProduction;
	
    @Column(name = "price")
    @Min(value = 0)
    private Mono<Long> price;

    @Column(name = "mileage")
    @Min(value = 0)
    private Mono<Long> mileage;
    
    @Column(name = "date_of_sale")
    private Mono<String> dateOfSale;
    
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
    private ReactDriver activeDriver;
    
    @OneToMany(mappedBy = "vehicle")
    @JsonIgnore
    private List<PointGps> PointsGps;
    
    @OneToMany(mappedBy = "vehicle")
    @JsonIgnore
    private List<Trip> trip;
    
	public ReactVehicle() {
	}

	public ReactVehicle(@NotEmpty(message = "Обязательное поле") Mono<String> registrationNumber,
			@Min(value = 1950, message = "Допускается добавление ТС старше 1950 г.") @Max(value = 2024, message = "Недопустимое значение") Mono<Integer> yearOfProduction,
			@Min(0) Mono<Long> price, @Min(0) Mono<Long> mileage, Mono<String> dateOfSale, Brand brand,
			Enterprise enterprise, List<Driver> drivers, ReactDriver activeDriver, List<PointGps> pointsGps,
			List<Trip> trip) {
		super();
		this.registrationNumber = registrationNumber;
		this.yearOfProduction = yearOfProduction;
		this.price = price;
		this.mileage = mileage;
		this.dateOfSale = dateOfSale;
		this.brand = brand;
		this.enterprise = enterprise;
		this.drivers = drivers;
		this.activeDriver = activeDriver;
		PointsGps = pointsGps;
		this.trip = trip;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Mono<String> getRegistrationNumber() {
		return registrationNumber;
	}

	public void setRegistrationNumber(Mono<String> registrationNumber) {
		this.registrationNumber = registrationNumber;
	}

	public Mono<Integer> getYearOfProduction() {
		return yearOfProduction;
	}

	public void setYearOfProduction(Mono<Integer> yearOfProduction) {
		this.yearOfProduction = yearOfProduction;
	}

	public Mono<Long> getPrice() {
		return price;
	}

	public void setPrice(Mono<Long> price) {
		this.price = price;
	}

	public Mono<Long> getMileage() {
		return mileage;
	}

	public void setMileage(Mono<Long> mileage) {
		this.mileage = mileage;
	}

	public Mono<String> getDateOfSale() {
		return dateOfSale;
	}

	public void setDateOfSale(Mono<String> dateOfSale) {
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

	public ReactDriver getActiveDriver() {
		return activeDriver;
	}

	public void setActiveDriver(ReactDriver driver) {
		this.activeDriver = driver;
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
