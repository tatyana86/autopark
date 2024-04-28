package ru.krivonogova.autopark.models;

import java.util.ArrayList;
import java.util.List;

import ru.krivonogova.autopark.dto.PointGpsCoord;

public class RouteResponse {

	double distance;
	
	List<PointGpsCoord> track;
	
	public RouteResponse() {
        this.distance = 0;
        this.track = new ArrayList<>();
    }

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public List<PointGpsCoord> getTrack() {
		return track;
	}

	public void setTrack(List<PointGpsCoord> track) {
		this.track = track;
	}
	
	
}
