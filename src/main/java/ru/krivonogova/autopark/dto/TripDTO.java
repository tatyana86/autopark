package ru.krivonogova.autopark.dto;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import ru.krivonogova.autopark.models.Vehicle;

public class TripDTO {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
	
    private VehicleIdDTO vehicleId;
	
    private String timeOfStart;
	
    private String timeOfEnd;
    
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
