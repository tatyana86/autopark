package ru.krivonogova.autopark.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ru.krivonogova.autopark.models.Vehicle;

@Repository
public interface VehiclesRepository extends JpaRepository<Vehicle, Integer> {

	List<Vehicle> findVehiclesByEnterprise_id(int id);
	
	Page<Vehicle> findVehiclesByEnterprise_id(int id, Pageable pageable);
	
}
