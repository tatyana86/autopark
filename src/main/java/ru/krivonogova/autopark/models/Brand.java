package ru.krivonogova.autopark.models;

import java.util.List;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;

@Entity
@Table(name = "brand")
public class Brand {
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name = "name")
	@NotEmpty(message = "Обязательное поле")
	private String name;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "type_vehicle")
	private TypeVehicle typeVehicle;
	
	@Column(name = "tank_capacity")
	private double tankCapacity;
	
	@Column(name = "load_capacity")
	private double loadCapacity;
	
	@Column(name = "passenger_capacity")
	private int passengerCapacity;
	
	@Column(name = "engine_power")
	private double enginePower;
	
	@OneToMany(mappedBy = "brand")
	private List<Vehicle> vehicles;

	public Brand() {
	}

	public Brand(String name, TypeVehicle typeVehicle, double tankCapacity, double loadCapacity, int passengerCapacity,
			double enginePower, List<Vehicle> vehicles) {
		this.name = name;
		this.typeVehicle = typeVehicle;
		this.tankCapacity = tankCapacity;
		this.loadCapacity = loadCapacity;
		this.passengerCapacity = passengerCapacity;
		this.enginePower = enginePower;
		this.vehicles = vehicles;
	}

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

	public TypeVehicle getTypeVehicle() {
		return typeVehicle;
	}

	public void setTypeVehicle(TypeVehicle typeVehicle) {
		this.typeVehicle = typeVehicle;
	}

	public double getTankCapacity() {
		return tankCapacity;
	}

	public void setTankCapacity(double tankCapacity) {
		this.tankCapacity = tankCapacity;
	}

	public double getLoadCapacity() {
		return loadCapacity;
	}

	public void setLoadCapacity(double loadCapacity) {
		this.loadCapacity = loadCapacity;
	}

	public int getPassengerCapacity() {
		return passengerCapacity;
	}

	public void setPassengerCapacity(int passengerCapacity) {
		this.passengerCapacity = passengerCapacity;
	}

	public double getEnginePower() {
		return enginePower;
	}

	public void setEnginePower(double enginePower) {
		this.enginePower = enginePower;
	}

	public List<Vehicle> getVehicles() {
		return vehicles;
	}

	public void setVehicles(List<Vehicle> vehicles) {
		this.vehicles = vehicles;
	}
	
}
