package ru.krivonogova.autopark.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.krivonogova.autopark.models.Driver;
import ru.krivonogova.autopark.models.Enterprise;
import ru.krivonogova.autopark.repositories.DriversRepository;

@Service
@Transactional(readOnly = true)
public class DriversService {
	private final DriversRepository driversRepository;
	private final EnterprisesService enterpriseService;
	
	@Autowired
	public DriversService(DriversRepository driversRepository, EnterprisesService enterpriseService) {
		this.driversRepository = driversRepository;
		this.enterpriseService = enterpriseService;
	}
	
	public List<Driver> findAll() {
		return driversRepository.findAll();
	}
	
	public Driver findOne(int id) {
		Optional<Driver> foundDriver = driversRepository.findById(id);
		
		return foundDriver.orElse(null);
	}
	
	public List<Driver> findAllForManager(int managerId) {
		
		List<Enterprise> enterprises = enterpriseService.findAllForManager(managerId);
		
		List<Driver> drivers = new ArrayList<Driver>();
		
		for(Enterprise enterprise : enterprises) {
			drivers.addAll(driversRepository.findDriversByEnterprise_id(enterprise.getId()));
		}

		return drivers;
	}
	
	@Transactional
	public void saveAll(List<Driver> drivers) {
		driversRepository.saveAll(drivers);
	}
}
