package ru.krivonogova.autopark.dto;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

public class VehicleIdDTO {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
