package ru.krivonogova.autopark.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.krivonogova.autopark.models.PointGps;


@Repository
public interface PointsGpsRepository extends JpaRepository<PointGps, Integer>{

	@Query("SELECT p FROM PointGps p WHERE p.vehicle.id = :vehicleId AND p.timeOfPointGps BETWEEN :dateFrom AND :dateTo")
    List<PointGps> findAllByVehicleAndTimePeriod(@Param("vehicleId") int vehicleId, @Param("dateFrom") String dateFrom, @Param("dateTo") String dateTo);
	
	
	/*@Query("SELECT p FROM PointGps p WHERE p.vehicle.id = :vehicleId")
    List<PointGps> findAllByVehicleId(@Param("vehicleId") int vehicleId);*/
}
