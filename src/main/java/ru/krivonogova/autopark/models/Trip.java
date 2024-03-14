package ru.krivonogova.autopark.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/*
 * 19. Добавьте модель Поездка, в которой хранится автомобиль, 
 * и дата-время в UTC начало и окончание поездки без ограничений 
 * (может быть два часа, может быть два года).
В API сделайте запрос, который по заданным дате-времени 
начала и конца вытащит все поездки (считаем, что они в таймзоне предприятия) 
— запрос именно по поездкам, но выдаются только точки трека по id заданного автомобиля. 
Точки могут быть из нескольких реальных треков 
(которые в проекте сами по себе никак не представлены); 
условно, я могу запросить за месяц все поездки, и получу один огромный трек.
Если начало первой поездки раньше начального времени запроса, 
или окончание последней поездки позже времени конца запроса, 
то их не включаем в выдачу.
 */

@Entity
@Table(name = "trip")
public class Trip {
	
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
	
	@ManyToOne
    @JoinColumn(name = "vehicle_id", referencedColumnName = "id")
    private Vehicle vehicle;
	
	@Column(name = "time_start")
    private String timeOfStart;
	
	@Column(name = "time_end")
    private String timeOfEnd;

	public Trip() {
	}

	public Trip(Vehicle vehicle, String timeOfStart, String timeOfEnd) {
		this.vehicle = vehicle;
		this.timeOfStart = timeOfStart;
		this.timeOfEnd = timeOfEnd;
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

	public String getTimeOfStart() {
		return timeOfStart;
	}

	public void setTimeOfStart(String timeOfStart) {
		this.timeOfStart = timeOfStart;
	}

	public String getTimeOfEnd() {
		return timeOfEnd;
	}

	public void setTimeOfEnd(String timeOfEnd) {
		this.timeOfEnd = timeOfEnd;
	}

}
