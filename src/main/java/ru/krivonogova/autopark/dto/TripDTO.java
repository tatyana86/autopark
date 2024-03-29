package ru.krivonogova.autopark.dto;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

public class TripDTO {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
	
    private VehicleIdDTO vehicleId;
	
    private String timeOfStartForManager;
	
    private String timeOfEndForManager;
    
    private String addressOfStart;
    
    private String addressOfEnd;

	public TripDTO() {
	}

	public TripDTO(VehicleIdDTO vehicleId, String timeOfStartForManager, String timeOfEndForManager,
			String addressOfStart, String addressOfEnd) {
		this.vehicleId = vehicleId;
		this.timeOfStartForManager = timeOfStartForManager;
		this.timeOfEndForManager = timeOfEndForManager;
		this.addressOfStart = addressOfStart;
		this.addressOfEnd = addressOfEnd;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public VehicleIdDTO getVehicleId() {
		return vehicleId;
	}

	public void setVehicleId(VehicleIdDTO vehicleId) {
		this.vehicleId = vehicleId;
	}

	public String getTimeOfStartForManager() {
		return timeOfStartForManager;
	}

	public void setTimeOfStartForManager(String timeOfStartForManager) {
		this.timeOfStartForManager = timeOfStartForManager;
	}

	public String getTimeOfEndForManager() {
		return timeOfEndForManager;
	}

	public void setTimeOfEndForManager(String timeOfEndForManager) {
		this.timeOfEndForManager = timeOfEndForManager;
	}

	public String getAddressOfStart() {
		return addressOfStart;
	}

	public void setAddressOfStart(String addressOfStart) {
		this.addressOfStart = addressOfStart;
	}

	public String getAddressOfEnd() {
		return addressOfEnd;
	}

	public void setAddressOfEnd(String addressOfEnd) {
		this.addressOfEnd = addressOfEnd;
	}
	
}
