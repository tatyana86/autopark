package ru.krivonogova.autopark.models;

public enum Period {
	
	DAY("День"),
	MONTH("Месяц");

	private String type;
		
	Period(String type) {
		this.type = type;
	}
	
	public String getTitle() {
		return type;
	}	
}
