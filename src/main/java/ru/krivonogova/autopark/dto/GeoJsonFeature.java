package ru.krivonogova.autopark.dto;

import java.util.Map;

public class GeoJsonFeature {
	private String type;
    
    private Map<String, Object> geometry;
    
    private Map<String, Object> properties;
    
    private String timeOfPointGps;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Map<String, Object> getGeometry() {
		return geometry;
	}

	public void setGeometry(Map<String, Object> geometry) {
		this.geometry = geometry;
	}

	public Map<String, Object> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, Object> properties) {
		this.properties = properties;
	}

	public String getTimeOfPointGps() {
		return timeOfPointGps;
	}

	public void setTimeOfPointGps(String timeOfPointGps) {
		this.timeOfPointGps = timeOfPointGps;
	}  
	
}
