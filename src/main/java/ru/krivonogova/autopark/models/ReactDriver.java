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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import reactor.core.publisher.Mono;

@Entity
@Table(name = "driver")
public class ReactDriver {
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name = "name")
	@NotEmpty(message = "Обязательное поле")
	private Mono<String> name;
	
	@Column(name = "salary")
	private Mono<Long> salary;
	
	@ManyToOne
    @JoinColumn(name = "enterprise_id", referencedColumnName = "id")
    private Enterprise enterprise;
	
	@ManyToMany
	@JoinTable(name = "driver_vehicle",
				joinColumns = @JoinColumn(name = "driver_id"),
				inverseJoinColumns = @JoinColumn(name = "vehicle_id"))
	@JsonIgnore
    private List<ReactVehicle> vehicles;
	
	@Column(name = "is_active")
	private boolean isActive;
	
	
	//@Column(name = "active_vehicle_id")
	@OneToOne
	@JoinColumn(name = "active_vehicle_id", referencedColumnName = "id")
	private ReactVehicle activeVehicle;

	public ReactDriver() {
	}

	public ReactDriver(@NotEmpty(message = "Обязательное поле") Mono<String> name, Mono<Long> salary,
			Enterprise enterprise, List<ReactVehicle> vehicles, boolean isActive, ReactVehicle activeVehicle) {
		super();
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

	public Mono<String> getName() {
		return name;
	}

	public void setName(Mono<String> name) {
		this.name = name;
	}

	public Mono<Long> getSalary() {
		return salary;
	}

	public void setSalary(Mono<Long> mono) {
		this.salary = mono;
	}

	public List<ReactVehicle> getVehicles() {
		return vehicles;
	}

	public void setVehicles(List<ReactVehicle> vehicles) {
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

	public ReactVehicle getActiveVehicle() {
		return activeVehicle;
	}

	public void setActiveVehicle(ReactVehicle vehicle) {
		this.activeVehicle = vehicle;
	}
	
}
