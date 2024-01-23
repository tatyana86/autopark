package ru.krivonogova.autopark.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ru.krivonogova.autopark.dto.VehicleDTO;
import ru.krivonogova.autopark.models.Vehicle;
import ru.krivonogova.autopark.services.VehiclesService;

@RestController
@RequestMapping("/api/vehicles")
public class ApiVehiclesController {
	
	private final VehiclesService vehiclesService;
	private final ModelMapper modelMapper;
	
	@Autowired
	public ApiVehiclesController(VehiclesService vehiclesService, ModelMapper modelMapper) {
		this.vehiclesService = vehiclesService;
		this.modelMapper = modelMapper;
	}
	
	@GetMapping
	public List<VehicleDTO> index() {
        return vehiclesService.findAll().stream().map(this::convertToVehicleDTO)
        		.collect(Collectors.toList());
	}
	
	@GetMapping("/{id}")
	public VehicleDTO show(@PathVariable("id") int id) {
		return convertToVehicleDTO(vehiclesService.findOne(id));
	}
	
	private VehicleDTO convertToVehicleDTO(Vehicle vehicle) {
		return modelMapper.map(vehicle, VehicleDTO.class);
	}	
}
