package ru.krivonogova.autopark.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.krivonogova.autopark.models.Driver;
import ru.krivonogova.autopark.repositories.DriversRepository;

@Service
@Transactional(readOnly = true)
public class DriversService {
	private final DriversRepository driversRepository;
	//private final EnterprisesService enterpriseService;
	
	@Autowired
	public DriversService(DriversRepository driversRepository) {
			//EnterprisesService enterpriseService) {
		this.driversRepository = driversRepository;
		//this.enterpriseService = enterpriseService;
	}
	
	public List<Driver> findAll() {
		return driversRepository.findAll();
	}
	
	public Driver findOne(int id) {
		Optional<Driver> foundDriver = driversRepository.findById(id);
		
		return foundDriver.orElse(null);
	}
	
}
