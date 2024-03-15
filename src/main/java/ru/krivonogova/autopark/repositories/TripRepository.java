package ru.krivonogova.autopark.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ru.krivonogova.autopark.models.Trip;

@Repository
public interface TripRepository extends JpaRepository<Trip, Integer> {
	
	@Query("SELECT t FROM Trip t WHERE t.vehicle.id = :vehicleId AND t.timeOfStart >= :dateFrom AND t.timeOfEnd <= :dateTo")
    List<Trip> findTripsByVehicleAndTimeRange(@Param("vehicleId") int vehicleId,
								    		@Param("dateFrom") String dateFrom, 
											@Param("dateTo") String dateTo);
	
}
