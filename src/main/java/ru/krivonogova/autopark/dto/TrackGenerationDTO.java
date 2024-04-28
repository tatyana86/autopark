package ru.krivonogova.autopark.dto;

public class TrackGenerationDTO {

	private int idVehicle;
	
	private int lengthOfTrack;

	public int getIdVehicle() {
		return idVehicle;
	}

	public void setIdVehicle(int idVehicle) {
		this.idVehicle = idVehicle;
	}

	public int getLengthOfTrack() {
		return lengthOfTrack;
	}

	public void setLengthOfTrack(int lengthOfTrack) {
		this.lengthOfTrack = lengthOfTrack;
	}
	
}