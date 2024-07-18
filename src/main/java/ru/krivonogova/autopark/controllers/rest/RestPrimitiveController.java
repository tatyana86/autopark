package ru.krivonogova.autopark.controllers.rest;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.tags.Tag;
import ru.krivonogova.autopark.controllers.DatabaseController;
import ru.krivonogova.autopark.dto.DriverDTO;
import ru.krivonogova.autopark.dto.VehicleDTO_forAPI;
import ru.krivonogova.autopark.models.Brand;
import ru.krivonogova.autopark.models.Driver;
import ru.krivonogova.autopark.models.Enterprise;
import ru.krivonogova.autopark.models.Vehicle;

@Hidden
@Tag(name = "Секретный REST-контроллер", 
	description = "Контроллер для проверки работы CRUD-репозиториев "
			+ "без авторизации пользователей")
@RestController
@RequestMapping("/api")
public class RestPrimitiveController {
	private final DatabaseController databaseController;
	private final ModelMapper modelMapper;

	public RestPrimitiveController(DatabaseController databaseController, ModelMapper modelMapper) {
		this.modelMapper = modelMapper;
		this.databaseController = databaseController;
	}
	
	@GetMapping("/brands")
	public List<Brand> indexBrands() {
        return databaseController.findAllBrands();
	}
	
	@GetMapping("/brands/{id}")
	public Brand showBrand(@PathVariable("id") int id) {
		return databaseController.findOneBrand(id);
	}
	
	@GetMapping("/drivers")
	public List<DriverDTO> indexDrivers() {
		return databaseController.findAllDrivers().stream().map(this::convertToDriverDTO)
        		.collect(Collectors.toList());
	}

	@GetMapping("/drivers/{id}")
	public DriverDTO showDriver(@PathVariable("id") int id) {
		return convertToDriverDTO(databaseController.findOneDriver(id));
	}
	
	@GetMapping("/vehicles")
	public List<VehicleDTO_forAPI> indexVehicles() {
        return databaseController.findAllVehicles().stream().map(this::convertToVehicleDTO_forAPI)
        		.collect(Collectors.toList());
	}
	
	@GetMapping("/vehicles/{id}")
	public VehicleDTO_forAPI showVehicle(@PathVariable("id") int id) {
		return convertToVehicleDTO_forAPI(databaseController.findOneVehicle(id));
	}
	
	@GetMapping("/enterprises")
	public List<Enterprise> indexEnterprises() {
		return databaseController.findAllEnterprises();
	}
	
	@GetMapping("/enterprises/{id}")
	public Enterprise showEnterprise(@PathVariable("id") int id) {
		return databaseController.findOneEnterprise(id);
	}
	
	// маперы далее	
	
	private DriverDTO convertToDriverDTO(Driver driver) {
		return modelMapper.map(driver, DriverDTO.class);
	}
	
	private VehicleDTO_forAPI convertToVehicleDTO_forAPI(Vehicle vehicle) {
		
		String timezone = vehicle.getEnterprise().getTimezone();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
	    LocalDateTime dateOfSale_UTC = LocalDateTime.parse(vehicle.getDateOfSale(), formatter);
	    ZoneOffset timeZone = ZoneOffset.of(timezone);
	    LocalDateTime dateOfSale = dateOfSale_UTC.atZone(ZoneOffset.UTC).withZoneSameInstant(timeZone).toLocalDateTime();
	    String dateOfSaleForEntarprise = dateOfSale.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm"));
	    
	    VehicleDTO_forAPI vehicleDTO_forAPI = modelMapper.map(vehicle, VehicleDTO_forAPI.class);
	    vehicleDTO_forAPI.setDateOfSaleForEnterprise(dateOfSaleForEntarprise);
	    
		return vehicleDTO_forAPI;
	}
}
