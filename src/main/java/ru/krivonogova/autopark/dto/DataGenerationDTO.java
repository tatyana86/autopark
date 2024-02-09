package ru.krivonogova.autopark.dto;

import java.util.List;

public class DataGenerationDTO {

	private List<Integer> enterprisesID;
	private int numberOfVehicle;
	private int numberOfDriver;
	private int indicatorOfActiveVehicle;
	public List<Integer> getEnterprisesID() {
		return enterprisesID;
	}
	public void setEnterprisesID(List<Integer> enterprisesID) {
		this.enterprisesID = enterprisesID;
	}
	public int getNumberOfVehicle() {
		return numberOfVehicle;
	}
	public void setNumberOfVehicle(int numberOfVehicle) {
		this.numberOfVehicle = numberOfVehicle;
	}
	public int getNumberOfDriver() {
		return numberOfDriver;
	}
	public void setNumberOfDriver(int numberOfDriver) {
		this.numberOfDriver = numberOfDriver;
	}
	public int getIndicatorOfActiveVehicle() {
		return indicatorOfActiveVehicle;
	}
	public void setIndicatorOfActiveVehicle(int indicatorOfActiveVehicle) {
		this.indicatorOfActiveVehicle = indicatorOfActiveVehicle;
	}
	
	
}
