package ru.krivonogova.autopark.models;

public enum TypeReport {
	
	MILEAGE_BY_PERIOD("Пробег за период");
	
	private String type;
	
	TypeReport(String type) {
		this.type = type;
	}
	
	public String getTitle() {
		return type;
	}	
}
