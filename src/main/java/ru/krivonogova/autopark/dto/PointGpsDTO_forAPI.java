package ru.krivonogova.autopark.dto;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

public class PointGpsDTO_forAPI {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
	
    private VehicleIdDTO vehicleId;

    private double longitude; // x
    
    private double latitude; // y
    
    private String timeOfPointGpsForEnterprise;
        
    public PointGpsDTO_forAPI() {
	}

	public PointGpsDTO_forAPI(VehicleIdDTO vehicleId, double longitude, double latitude, String timeOfPointGpsForEnterprise) {
		this.vehicleId = vehicleId;
		this.longitude = longitude;
		this.latitude = latitude;
		this.timeOfPointGpsForEnterprise = timeOfPointGpsForEnterprise;
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

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}



	public String getTimeOfPointGpsForEnterprise() {
		return timeOfPointGpsForEnterprise;
	}

	public void setTimeOfPointGpsForEnterprise(String timeOfPointGpsForEnterprise) {
		this.timeOfPointGpsForEnterprise = timeOfPointGpsForEnterprise;
	}
	
}
