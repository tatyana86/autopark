package ru.krivonogova.autopark.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ru.krivonogova.autopark.models.Vehicle;
import ru.krivonogova.autopark.services.BrandsService;
import ru.krivonogova.autopark.services.VehiclesService;

@RestController
@RequestMapping("/api/vehicles")
public class ApiVehiclesController {
	
	private final VehiclesService vehiclesService;
	@Autowired
	public ApiVehiclesController(VehiclesService vehiclesService) {
		this.vehiclesService = vehiclesService;
	}
	
	@GetMapping
	public List<Vehicle> index() {
        return vehiclesService.findAll();
	}
	
	@GetMapping("/{id}")
	public Vehicle show(@PathVariable("id") int id) {
		return vehiclesService.findOne(id);
	}
	
}
