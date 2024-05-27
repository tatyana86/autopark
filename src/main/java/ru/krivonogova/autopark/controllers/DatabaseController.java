package ru.krivonogova.autopark.controllers;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;
import ru.krivonogova.autopark.models.Brand;
import ru.krivonogova.autopark.models.Driver;
import ru.krivonogova.autopark.models.Enterprise;
import ru.krivonogova.autopark.models.Manager;
import ru.krivonogova.autopark.models.PointGps;
import ru.krivonogova.autopark.models.ReactDriver;
import ru.krivonogova.autopark.models.Trip;
import ru.krivonogova.autopark.models.Vehicle;
import ru.krivonogova.autopark.repositories.BrandsRepository;
import ru.krivonogova.autopark.repositories.DriversRepository;
import ru.krivonogova.autopark.repositories.EnterprisesRepository;
import ru.krivonogova.autopark.repositories.ManagersRepository;
import ru.krivonogova.autopark.repositories.PointsGpsRepository;
import ru.krivonogova.autopark.repositories.TripRepository;
import ru.krivonogova.autopark.repositories.VehiclesRepository;
import ru.krivonogova.autopark.util.EnterpriseNotDeletedException;
import ru.krivonogova.autopark.util.EnterpriseNotUpdatedException;
import ru.krivonogova.autopark.util.VehicleNotFoundException;

@Slf4j
@Service
@Transactional(readOnly = true)
public class DatabaseController {
	private final BrandsRepository brandsRepository;
	private final DriversRepository driversRepository;
	private final VehiclesRepository vehiclesRepository;
	private final EnterprisesRepository enterprisesRepository;
	private final ManagersRepository managersRepository;
	private final PointsGpsRepository pointsGpsRepository;
	private final TripRepository tripRepository;

	@Autowired
	public DatabaseController(BrandsRepository brandsRepository, DriversRepository driversRepository, VehiclesRepository vehiclesRepository, EnterprisesRepository enterprisesRepository, ManagersRepository managersRepository, PointsGpsRepository pointsGpsRepository, TripRepository tripRepository) {
		this.brandsRepository = brandsRepository;
		this.driversRepository = driversRepository;
		this.vehiclesRepository = vehiclesRepository;
		this.enterprisesRepository = enterprisesRepository;
		this.managersRepository = managersRepository;
		this.pointsGpsRepository = pointsGpsRepository;
		this.tripRepository = tripRepository;
	}
	
	/* Бренды */
	
	public List<Brand> findAllBrands() {
		return brandsRepository.findAll();
	}
	
	public Brand findOneBrand(int id) {
		Optional<Brand> foundBrand = brandsRepository.findById(id);
		return foundBrand.orElse(null);
	}
	
	@Transactional
	public void saveBrand(Brand brand) {
		brandsRepository.save(brand);
	}
	
	@Transactional
	public void updateBrand(int id, Brand updatedBrand) {
		updatedBrand.setId(id);
		brandsRepository.save(updatedBrand);
	}
	
	@Transactional
	public void deleteBrand(int id) {
		brandsRepository.deleteById(id);
	}
	
	/* Водители */
	
	public List<Driver> findAllDrivers() {
		return driversRepository.findAll();
	}
	
	public Driver findOneDriver(int id) {
		Optional<Driver> foundDriver = driversRepository.findById(id);
		return foundDriver.orElse(null);
	}
	
	@Transactional
	public void saveAllDrivers(List<Driver> drivers) {
		driversRepository.saveAll(drivers);
	}
		
	/* Транспорт */
	
	public List<Vehicle> findAllVehicles() {
		return vehiclesRepository.findAll();
	}
	
	public Page<Vehicle> findAllVehicles(Integer page,
								Integer itemsPerPage) {
	
		Pageable pageable = PageRequest.of(page - 1, itemsPerPage);	
	
		return vehiclesRepository.findAll(pageable);
	}	
	
	// проверка кеша
	@Cacheable("vehicle")
	public Vehicle findOneVehicle(int id) {
		//log.info("Getting vehicle from repo by id: {}", id);
		Optional<Vehicle> foundVehicle = vehiclesRepository.findById(id);
		return foundVehicle.orElseThrow(VehicleNotFoundException::new);
	}
	
	@Transactional
	public void saveVehicle(Vehicle vehicle) {
		vehiclesRepository.save(vehicle);
	}
	
	@Transactional
	public void saveVehicle(Vehicle vehicle, int brandId) {
		vehicle.setBrand(findOneBrand(brandId));
		vehiclesRepository.save(vehicle);
	}
	
	@Transactional
	public void saveVehicle(Vehicle vehicle, int brandId, int idEnterprise) {
		vehicle.setBrand(findOneBrand(brandId));
		vehicle.setEnterprise(findOneEnterprise(idEnterprise));
		vehiclesRepository.save(vehicle);
	}
	
	@Transactional
	public void saveAllVehicles(List<Vehicle> vehicles) {
		vehiclesRepository.saveAll(vehicles);
	}
	
	@Transactional
	public void updateVehicle(int id, Vehicle updatedVehicle) {
		updatedVehicle.setId(id);
		vehiclesRepository.save(updatedVehicle);	
	}
	
	@Transactional
	public void updateVehicle(int id, Vehicle updatedVehicle, int updatedBrandId) {
		updatedVehicle.setId(id);
		updatedVehicle.setBrand(findOneBrand(updatedBrandId));
		vehiclesRepository.save(updatedVehicle);		
	}
	
	@Transactional
	public void updateVehicle(int id, Vehicle updatedVehicle, int updatedBrandId, int idEnterprise) {
		updatedVehicle.setId(id);
		updatedVehicle.setBrand(findOneBrand(updatedBrandId));
		updatedVehicle.setEnterprise(findOneEnterprise(idEnterprise));
		vehiclesRepository.save(updatedVehicle);		
	}
	
	@Transactional
	public void deleteVehicle(int id) {
		vehiclesRepository.deleteById(id);
	}
	
	/* Предприятия */
	
	public List<Enterprise> findAllEnterprises() {
		return enterprisesRepository.findAll();
	}
	
	public Enterprise findOneEnterprise(int id) {
		Optional<Enterprise> foundEnterprise = enterprisesRepository.findById(id);
		return foundEnterprise.orElse(null);
	}
	
	/* Менеджеры */
	
	public Manager findOneManager(int id) {
		Optional<Manager> foundManager = managersRepository.findById(id);
		return foundManager.orElse(null);
	}
	
	public Manager findManagerByUsername(String username) {
		Optional<Manager> foundManager = managersRepository.findByUsername(username);
		return foundManager.orElse(null);
	}
	
	// Предприятия, доступные менеджеру
	
	public List<Enterprise> findAllEnterprisesForManager(int id) {
	    List<Enterprise> enterprises = enterprisesRepository.findEnterprisesByManagers_id(id);
	    enterprises.sort(Comparator.comparingInt(Enterprise::getId));
	    return enterprises;
	}
	
	@Transactional
	public void saveEnterprise(Enterprise enterprise, int id) {
		Optional<Manager> foundManager = managersRepository.findById(id);
		
		if(enterprise.getManagers() == null) {
			enterprise.setManagers(new ArrayList<>());
		}
		
		enterprise.getManagers().add(foundManager.get());
	
		enterprisesRepository.save(enterprise);
	}
	
	@Transactional
	public void updateEnterprise(int idManager, int idEnterprise, Enterprise updatedEnterprise) {
		
		Enterprise enterprise = enterprisesRepository.findById(idEnterprise).get();
		List<Enterprise> managerEnterprises = managersRepository.findById(idManager).get().getEnterprises();
		
		if(!managerEnterprises.contains(enterprise)) {
			throw new EnterpriseNotUpdatedException("Нет доступа к данному предприятию");
		}
		
		List<Manager> managers = enterprisesRepository.findById(idEnterprise).get().getManagers();
		
		updatedEnterprise.setManagers(managers);
		updatedEnterprise.setId(idEnterprise);
		
		enterprisesRepository.save(updatedEnterprise);
	}
	
	@Transactional
	public void deleteEnterprise(int idManager, int idEnterprise) {

		Enterprise enterprise = enterprisesRepository.findById(idEnterprise).get();
		List<Enterprise> managerEnterprises = managersRepository.findById(idManager).get().getEnterprises();
		
		if(!managerEnterprises.contains(enterprise)) {
			throw new EnterpriseNotDeletedException("Нет доступа к данному предприятию");
		}
		        
        // Удаление предприятия из списка у всех менеджеров, у которых оно есть
        for (Manager manager : enterprise.getManagers()) {
            manager.getEnterprises().remove(enterprise);
        }

        enterprisesRepository.deleteById(idEnterprise);
	}
	
	// Транспорт, доступный менеджеру
	
	public List<Vehicle> findAllVehiclesForManager(int managerId) {
		
		List<Enterprise> enterprises = findAllEnterprisesForManager(managerId);
		
		List<Vehicle> vehicles = new ArrayList<Vehicle>();
		
		for(Enterprise enterprise : enterprises) {
			vehicles.addAll(vehiclesRepository.findVehiclesByEnterprise_id(enterprise.getId()));
		}
		
		return vehicles;
	}
	
	public Page<Vehicle> findAllVehiclesForManager(int managerId, Integer page, Integer itemsPerPage) {
		
		List<Enterprise> enterprises = findAllEnterprisesForManager(managerId);
		
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
	
	public List<Vehicle> findForManagerByEnterpriseId(int managerId, int enterpriseId) {
		List<Vehicle> vehicles = new ArrayList<Vehicle>();
		vehicles.addAll(vehiclesRepository.findVehiclesByEnterprise_id(enterpriseId));
		return vehicles;
	}
	
	public Page<Vehicle> findForManagerByEnterpriseId(int managerId, 
			int enterpriseId,
			Integer page,
			Integer itemsPerPage) {

		Pageable pageable = PageRequest.of(page - 1, itemsPerPage);
		return vehiclesRepository.findVehiclesByEnterprise_id(enterpriseId, pageable);
	}	
	
	// Водители, доступные менеджеру
	
	public List<Driver> findAllDriversForManager(int managerId) {
		
		List<Enterprise> enterprises = findAllEnterprisesForManager(managerId);
		
		List<Driver> drivers = new ArrayList<Driver>();
		
		for(Enterprise enterprise : enterprises) {
			drivers.addAll(driversRepository.findDriversByEnterprise_id(enterprise.getId()));
		}

		return drivers;
	}
	
	public Page<Driver> findAllDriversForManager(int managerId, Integer page, Integer itemsPerPage) {
		
		// logger.info("Received page: " + page + ", itemsPerPage: " + itemsPerPage);
		
		List<Enterprise> enterprises = findAllEnterprisesForManager(managerId);
		
		List<Driver> drivers = new ArrayList<Driver>();
		
		for(Enterprise enterprise : enterprises) {
			drivers.addAll(driversRepository.findDriversByEnterprise_id(enterprise.getId()));
		}
				
		int pageNumber = (page != null) ? Math.max(page - 1, 0) : 0; 
	    Pageable pageable = PageRequest.of(pageNumber, itemsPerPage);
	    
	    //int pageSize = (itemsPerPage != null) ? itemsPerPage : drivers.size(); // Установим размер страницы равным количеству результатов, если itemsPerPage = null
	    
	    int start = (int)pageable.getOffset();
	    int end = Math.min((start + pageable.getPageSize()), drivers.size());
	    
	    return new PageImpl<Driver>(drivers.subList(start, end), pageable, drivers.size()); 
	}
	
	/* GPS-данные */
	
	public List<PointGps> findAllPoints() {
		return pointsGpsRepository.findAll();
	}
	
	public List<PointGps> findAllPointsByVehicleAndTimePeriod(int vehicleId, String dateFrom, String dateTo) {
		return pointsGpsRepository.findAllByVehicleAndTimePeriod(vehicleId, dateFrom, dateTo);
	}
	
    public Optional<PointGps> findByVehicleAndTime(int vehicleId, String time) {
        return pointsGpsRepository.findFirstByVehicleIdAndTimeOfPointGps(vehicleId, time);
    }
    
	public List<PointGps> findAllByVehicleAndTrip(int vehicleId, List<Trip> trips) {
		String dateFrom_upd = trips.get(0).getTimeOfStart();
		String dateTo_upd = trips.get(trips.size() - 1).getTimeOfEnd();
		return pointsGpsRepository.findAllByVehicleAndTimePeriod(vehicleId, dateFrom_upd, dateTo_upd);
	}
 	
    // проверка кеша
 	@Cacheable("track")
	public List<PointGps> findAllByVehicleAndTrip(int vehicleId, int tripId) {
		
		String dateFrom_upd = findOneTrip(tripId).getTimeOfStart();
		String dateTo_upd = findOneTrip(tripId).getTimeOfEnd();
		
		log.info("Getting points from repo for trip by id: {}", tripId);
		
		return pointsGpsRepository.findAllByVehicleAndTimePeriod(vehicleId, dateFrom_upd, dateTo_upd);
	}
	
    @Transactional
    public void saveAllPoints(List<PointGps> pointsGps) {
    	pointsGpsRepository.saveAll(pointsGps);
    }
    
    @Transactional
    public void savePoint(PointGps pointsGps) {
    	pointsGpsRepository.save(pointsGps);
    }
    
	/* Поездки */
    
	public Trip findOneTrip(int id) {
		Optional<Trip> foundTrip = tripRepository.findById(id);
		return foundTrip.orElse(null);
	}
	
	// проверка кеша
	@Cacheable("trips")
	public List<Trip> findAllTripsByTimePeriod(int vehicleId, String dateFrom, String dateTo) {
		//log.info("Getting trips from repo for vehicle by id: {}", vehicleId);
		
		return tripRepository.findTripsByVehicleAndTimeRange(vehicleId, dateFrom, dateTo);
	}
	
	@Transactional
    public void saveTrip(Trip trip) {
		tripRepository.save(trip);
	}
	
}
