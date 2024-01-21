package ru.krivonogova.autopark.models;

public enum TypeVehicle {
//	PASSENGER_CAR, BUS,	VAN, TRUCK, MOTORCYCLE, SCOOTER
	
	PASSENGER_CAR ("Пассажирский автомобиль"),
	BUS ("Автобус"),	
	VAN ("Фургон"), 
	TRUCK ("Грузовик"), 
	MOTORCYCLE ("Мотоцикл"), 
	SCOOTER ("Скутер");
	
	private String type;
	
	TypeVehicle(String type) {
		this.type = type;
	}
	
	public String getTitle() {
		return type;
	}
	
}
