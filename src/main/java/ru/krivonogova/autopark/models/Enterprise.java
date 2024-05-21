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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;

@Entity
@Table(name = "enterprise")
public class Enterprise {
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
	
	@Column(name = "name")
	@NotEmpty(message = "Обязательное поле")
	private String name;
	
    @Column(name = "city")
    @NotEmpty(message = "Обязательное поле")
    private String city;
    
    @Column(name = "phone")
    @NotEmpty(message = "Обязательное поле")
    private String phone;
    
    @Column(name = "timezone")
    private String timezone;

	@OneToMany(mappedBy = "enterprise")
	@JsonIgnore
    private List<Vehicle> vehicles;
	
	@OneToMany(mappedBy = "enterprise")
	@JsonIgnore
    private List<Driver> drivers;
    
	@ManyToMany
	@JoinTable(name = "enterprise_manager",
				joinColumns = @JoinColumn(name = "enterprise_id"),
				inverseJoinColumns = @JoinColumn(name = "manager_id"))
	@JsonIgnore
	private List<Manager> managers;
	
	public Enterprise() {
	}

	public Enterprise(@NotEmpty(message = "Обязательное поле") String name,
			@NotEmpty(message = "Обязательное поле") String city, @NotEmpty(message = "Обязательное поле") String phone,
			List<Vehicle> vehicles, List<Driver> drivers, List<Manager> managers) {
		this.name = name;
		this.city = city;
		this.phone = phone;
		this.vehicles = vehicles;
		this.drivers = drivers;
		this.managers = managers;
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

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getTimezone() {
		return timezone;
	}

	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}

	public List<Vehicle> getVehicles() {
		return vehicles;
	}

	public void setVehicles(List<Vehicle> vehicles) {
		this.vehicles = vehicles;
	}

	public List<Driver> getDrivers() {
		return drivers;
	}

	public void setDrivers(List<Driver> drivers) {
		this.drivers = drivers;
	}

	public List<Manager> getManagers() {
		return managers;
	}

	public void setManagers(List<Manager> managers) {
		this.managers = managers;
	}
	
}
