package ru.krivonogova.autopark.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.krivonogova.autopark.models.Vehicle;
import ru.krivonogova.autopark.repositories.VehiclesRepository;

@Service
@Transactional(readOnly = true)
public class VehiclesService {

	private final VehiclesRepository vehiclesRepository;
	private final BrandsService brandsService;

	@Autowired
	public VehiclesService(VehiclesRepository vehiclesRepository, BrandsService brandsService) {
		this.vehiclesRepository = vehiclesRepository;
		this.brandsService = brandsService;
	}
	
	public List<Vehicle> findAll() {
		return vehiclesRepository.findAll();
	}
	
	public Vehicle findOne(int id) {
		Optional<Vehicle> foundVehicle = vehiclesRepository.findById(id);
		
		return foundVehicle.orElse(null);
	}
	
	@Transactional
	public void save(Vehicle vehicle, int brandId) {
		vehicle.setBrand(brandsService.findOne(brandId));
		vehiclesRepository.save(vehicle);
	}
	
	@Transactional
	public void update(int id, Vehicle updatedVehicle, int updatedBrandId) {
		updatedVehicle.setId(id);
		updatedVehicle.setBrand(brandsService.findOne(updatedBrandId));
		
		vehiclesRepository.save(updatedVehicle);		
	}
	
	@Transactional
	public void delete(int id) {
		vehiclesRepository.deleteById(id);
	}
		
}
