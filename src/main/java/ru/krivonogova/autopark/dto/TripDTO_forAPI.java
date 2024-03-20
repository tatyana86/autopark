package ru.krivonogova.autopark.dto;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

public class TripDTO_forAPI {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
	
    private VehicleIdDTO vehicleId;
	
    private String timeOfStartForEnterprise;
	
    private String timeOfEndForEnterprise;
    
    private String addressOfStart;
    
    private String addressOfEnd;

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

	public String getTimeOfStartForEnterprise() {
		return timeOfStartForEnterprise;
	}

	public void setTimeOfStartForEnterprise(String timeOfStartForEnterprise) {
		this.timeOfStartForEnterprise = timeOfStartForEnterprise;
	}

	public String getTimeOfEndForEnterprise() {
		return timeOfEndForEnterprise;
	}

	public void setTimeOfEndForEnterprise(String timeOfEndForEnterprise) {
		this.timeOfEndForEnterprise = timeOfEndForEnterprise;
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
