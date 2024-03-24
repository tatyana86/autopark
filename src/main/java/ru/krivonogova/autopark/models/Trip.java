package ru.krivonogova.autopark.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "trip")
public class Trip {
	
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
	
	@ManyToOne
    @JoinColumn(name = "vehicle_id", referencedColumnName = "id")
    private Vehicle vehicle;
	
	@Column(name = "time_start")
    private String timeOfStart;
	
	@Column(name = "time_end")
    private String timeOfEnd;
	
	@Column(name = "distance")
    private double distance;

	public Trip() {
	}

	public Trip(Vehicle vehicle, String timeOfStart, String timeOfEnd, double distance) {
		this.vehicle = vehicle;
		this.timeOfStart = timeOfStart;
		this.timeOfEnd = timeOfEnd;
		this.distance = distance;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Vehicle getVehicle() {
		return vehicle;
	}

	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}

	public String getTimeOfStart() {
		return timeOfStart;
	}

	public void setTimeOfStart(String timeOfStart) {
		this.timeOfStart = timeOfStart;
	}

	public String getTimeOfEnd() {
		return timeOfEnd;
	}

	public void setTimeOfEnd(String timeOfEnd) {
		this.timeOfEnd = timeOfEnd;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

}
