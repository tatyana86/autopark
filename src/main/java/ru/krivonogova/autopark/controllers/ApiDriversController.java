package ru.krivonogova.autopark.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ru.krivonogova.autopark.dto.DriverDTO;
import ru.krivonogova.autopark.models.Driver;
import ru.krivonogova.autopark.services.DriversService;

@RestController
@RequestMapping("/api/drivers")
public class ApiDriversController {
	
	private final DriversService driversService;
	private final ModelMapper modelMapper;
	
	@Autowired	
	public ApiDriversController(DriversService driversService, ModelMapper modelMapper) {
		this.driversService = driversService;
		this.modelMapper = modelMapper;
	}

	@GetMapping
	public List<DriverDTO> index() {
		return driversService.findAll().stream().map(this::convertToDriverDTO)
        		.collect(Collectors.toList());
	}
	
	@GetMapping("/{id}")
	public DriverDTO show(@PathVariable("id") int id) {
		return convertToDriverDTO(driversService.findOne(id));
	}
	
	private DriverDTO convertToDriverDTO(Driver driver) {
		return modelMapper.map(driver, DriverDTO.class);
	}

}
