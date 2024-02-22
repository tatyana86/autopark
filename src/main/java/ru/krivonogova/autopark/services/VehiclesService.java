package ru.krivonogova.autopark.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.krivonogova.autopark.models.Enterprise;
import ru.krivonogova.autopark.models.Vehicle;
import ru.krivonogova.autopark.repositories.VehiclesRepository;
import ru.krivonogova.autopark.util.VehicleNotFoundException;

@Service
@Transactional(readOnly = true)
public class VehiclesService {

	private final VehiclesRepository vehiclesRepository;
	private final BrandsService brandsService;
	
	private final EnterprisesService enterprisesService;
	
	@Autowired
	public VehiclesService(VehiclesRepository vehiclesRepository, BrandsService brandsService, EnterprisesService enterprisesService, ManagersService managersService) {
		this.vehiclesRepository = vehiclesRepository;
		this.brandsService = brandsService;
		this.enterprisesService = enterprisesService;
	}
	
	public List<Vehicle> findAll() {
		return vehiclesRepository.findAll();
	}
	
	public Vehicle findOne(int id) {
		Optional<Vehicle> foundVehicle = vehiclesRepository.findById(id);
		
		return foundVehicle.orElseThrow(VehicleNotFoundException::new);
	}
	
	@Transactional
	public void save(Vehicle vehicle, int brandId) {
		vehicle.setBrand(brandsService.findOne(brandId));
		vehiclesRepository.save(vehicle);
	}
	
	@Transactional
	public void save(Vehicle vehicle) {
		vehiclesRepository.save(vehicle);
	}
	
	@Transactional
	public void saveAll(List<Vehicle> vehicles) {
		vehiclesRepository.saveAll(vehicles);
	}
		
	@Transactional
	public void update(int id, Vehicle updatedVehicle, int updatedBrandId) {
		updatedVehicle.setId(id);
		updatedVehicle.setBrand(brandsService.findOne(updatedBrandId));
		
		vehiclesRepository.save(updatedVehicle);		
	}
	
	@Transactional
	public void update(int id, Vehicle updatedVehicle) {
		updatedVehicle.setId(id);
		vehiclesRepository.save(updatedVehicle);	
	}
	
	@Transactional
	public void delete(int id) {
		vehiclesRepository.deleteById(id);
	}
	
	public List<Vehicle> findAllForManager(int managerId) {
		
		List<Enterprise> enterprises = enterprisesService.findAllForManager(managerId);
		
		List<Vehicle> vehicles = new ArrayList<Vehicle>();
		
		for(Enterprise enterprise : enterprises) {
			vehicles.addAll(vehiclesRepository.findVehiclesByEnterprise_id(enterprise.getId()));
		}
		
		return vehicles;
	}
	
	public List<Vehicle> findForManagerByEnterpriseId(int managerId, int enterpriseId) {
		
		List<Vehicle> vehicles = new ArrayList<Vehicle>();
		
		vehicles.addAll(vehiclesRepository.findVehiclesByEnterprise_id(enterpriseId));
		
		return vehicles;
	}
	
	public Page<Vehicle> findAllForManager(int managerId, Integer page, Integer itemsPerPage) {
		
		List<Enterprise> enterprises = enterprisesService.findAllForManager(managerId);
		
		List<Vehicle> vehicles = new ArrayList<Vehicle>();
		
		for(Enterprise enterprise : enterprises) {
			vehicles.addAll(vehiclesRepository.findVehiclesByEnterprise_id(enterprise.getId()));
		}
		
		int pageNumber = (page != null) ? Math.max(page - 1, 0) : 0; 
	    Pageable pageable = PageRequest.of(pageNumber, itemsPerPage);
	    
	    //int pageSize = (itemsPerPage != null) ? itemsPerPage : vehicles.size(); // Установим размер страницы равным количеству результатов, если itemsPerPage = null
	    
	    int start = (int)pageable.getOffset();
	    int end = Math.min((start + pageable.getPageSize()), vehicles.size());
		
	    return new PageImpl<Vehicle>(vehicles.subList(start, end), pageable, vehicles.size());
	}
	
	// now
	public Page<Vehicle> findForManagerByEnterpriseId(int managerId, 
													int enterpriseId,
													Integer page,
													Integer itemsPerPage) {
				
		Pageable pageable = PageRequest.of(page - 1, itemsPerPage);	
		
	    return vehiclesRepository.findVehiclesByEnterprise_id(enterpriseId, pageable);
	}
		
}
