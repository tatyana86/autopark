package ru.krivonogova.autopark.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Schema(description = "id предприятия")
public class EnterpriseDTO {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Schema(description = "id")
    private int id;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
}
