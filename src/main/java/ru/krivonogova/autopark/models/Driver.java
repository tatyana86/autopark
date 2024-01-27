package ru.krivonogova.autopark.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotEmpty;

@Entity
@Table(name = "driver")
public class Driver {
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name = "name")
	@NotEmpty(message = "Обязательное поле")
	private String name;
	
	@Column(name = "salary")
	@NotEmpty(message = "Обязательное поле")
	private String salary;
	
	@ManyToOne
    @JoinColumn(name = "enterprise_id", referencedColumnName = "id")
    private Enterprise enterprise;
	
	@ManyToMany
	@JoinTable(name = "driver_vehicle",
				joinColumns = @JoinColumn(name = "driver_id"),
				inverseJoinColumns = @JoinColumn(name = "vehicle_id"))
	@JsonIgnore
    private List<Vehicle> vehicles;
	
	@Transient
	private boolean isActive;
	
	@Transient
	private Vehicle activeVehicle;

	public Driver() {
	}

	public Driver(@NotEmpty(message = "Обязательное поле") String name,
			@NotEmpty(message = "Обязательное поле") String salary, Enterprise enterprise, List<Vehicle> vehicles,
			boolean isActive, Vehicle activeVehicle) {
		this.name = name;
		this.salary = salary;
		this.enterprise = enterprise;
		this.vehicles = vehicles;
		this.isActive = isActive;
		this.activeVehicle = activeVehicle;
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

	public String getSalary() {
		return salary;
	}

	public void setSalary(String salary) {
		this.salary = salary;
	}

	public List<Vehicle> getVehicles() {
		return vehicles;
	}

	public void setVehicles(List<Vehicle> vehicles) {
		this.vehicles = vehicles;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public Enterprise getEnterprise() {
		return enterprise;
	}

	public void setEnterprise(Enterprise enterprise) {
		this.enterprise = enterprise;
	}

	public Vehicle getActiveVehicle() {
		return activeVehicle;
	}

	public void setActiveVehicle(Vehicle activeVehicle) {
		this.activeVehicle = activeVehicle;
	}
	
}
