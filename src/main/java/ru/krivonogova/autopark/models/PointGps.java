package ru.krivonogova.autopark.models;

import org.locationtech.jts.geom.Point;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "track")
public class PointGps {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
	
	@ManyToOne
    @JoinColumn(name = "vehicle_id", referencedColumnName = "id")
    private Vehicle vehicle;
    
    @Column(name="coordinates")
    private Point coordinates;
    
    @Transient
    private double longitude; // x
    
    @Transient
    private double latitude; // y
        
    public PointGps() {
	}

	public PointGps(Vehicle vehicle, Point coordinates) {
		this.vehicle = vehicle;
		this.coordinates = coordinates;
	}

	public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

	public Point getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(Point coordinates) {
		this.coordinates = coordinates;
	}

	public double getLongitude() {
		return this.coordinates.getX();
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return this.coordinates.getY();
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

}
