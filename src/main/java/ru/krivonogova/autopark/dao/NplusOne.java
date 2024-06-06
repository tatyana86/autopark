package ru.krivonogova.autopark.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import ru.krivonogova.autopark.models.Vehicle;

@Slf4j
@Component
public class NplusOne {
	
	private final SessionFactory sessionFactory;
	
	@Autowired
	public NplusOne(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public void demonstrate() {
		
		// Проблема N + 1
		Session session1 = sessionFactory.openSession();
		try {
			log.info("Get vehicles by default");
	        List<Vehicle> vehicles = session1.createQuery("SELECT v FROM Vehicle v", Vehicle.class).list();
	        
	        log.info("Start N requests");
	        for(Vehicle vehicle : vehicles) {
	            System.out.println("Vehicle with id " + vehicle.getId() + " have " + vehicle.getTrip());
	        }
	        log.info("End N requests");
	        
		} finally {
	        session1.close();
	    }
		
		// Решение проблемы - использовать JOIN
		Session session2 = sessionFactory.openSession();
		try {
			log.info("Get vehicles with JOIN");
	        List<Vehicle> newVehicles = session2.createQuery("SELECT v FROM Vehicle v LEFT JOIN FETCH v.trip", Vehicle.class).list();
	        
	        log.info("Start after JOIN");
	        for(Vehicle newVehicle : newVehicles) {
	            System.out.println("Vehicle with id " + newVehicle.getId() + " have " + newVehicle.getTrip());
	        }
	        log.info("End after JOIN");
	        
		} finally {
	        session2.close();
	    }
		
	}

}
