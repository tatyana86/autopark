package ru.krivonogova.autopark.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
	
	private static final Logger logger = LoggerFactory.getLogger(DriversService.class);
	
	public List<Driver> findAll() {
		return driversRepository.findAll();
	}
	
	public Driver findOne(int id) {
		Optional<Driver> foundDriver = driversRepository.findById(id);
		
		return foundDriver.orElse(null);
	}
	
	@Transactional
	public void saveAll(List<Driver> drivers) {
		driversRepository.saveAll(drivers);
	}
	
	public List<Driver> findAllForManager(int managerId) {
		
		List<Enterprise> enterprises = enterpriseService.findAllForManager(managerId);
		
		List<Driver> drivers = new ArrayList<Driver>();
		
		for(Enterprise enterprise : enterprises) {
			drivers.addAll(driversRepository.findDriversByEnterprise_id(enterprise.getId()));
		}

		return drivers;
	}
	
	//new
	public Page<Driver> findAllForManager(int managerId, Integer page, Integer itemsPerPage) {
		
		logger.info("Received page: " + page + ", itemsPerPage: " + itemsPerPage);
		
		List<Enterprise> enterprises = enterpriseService.findAllForManager(managerId);
		
		List<Driver> drivers = new ArrayList<Driver>();
		
		for(Enterprise enterprise : enterprises) {
			drivers.addAll(driversRepository.findDriversByEnterprise_id(enterprise.getId()));
		}
		
		
//    	Pageable pageable = PageRequest.of(page - 1, itemsPerPage);
//	    return new PageImpl<Driver>(drivers, pageable, drivers.size());
		
		int pageNumber = (page != null) ? Math.max(page - 1, 0) : 0; 
	    Pageable pageable = PageRequest.of(pageNumber, itemsPerPage);
	    
	    int pageSize = (itemsPerPage != null) ? itemsPerPage : drivers.size(); // Установим размер страницы равным количеству результатов, если itemsPerPage = null
	    
	    int start = (int)pageable.getOffset();
	    int end = Math.min((start + pageable.getPageSize()), drivers.size());
	    
	    return new PageImpl<Driver>(drivers.subList(start, end), pageable, drivers.size()); 
	}
	
}
