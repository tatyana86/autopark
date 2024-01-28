package ru.krivonogova.autopark.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import ru.krivonogova.autopark.dto.DriverDTO;
import ru.krivonogova.autopark.dto.VehicleDTO;
import ru.krivonogova.autopark.models.Driver;
import ru.krivonogova.autopark.models.Enterprise;
import ru.krivonogova.autopark.models.Vehicle;
import ru.krivonogova.autopark.security.ManagerDetails;
import ru.krivonogova.autopark.services.DriversService;
import ru.krivonogova.autopark.services.EnterprisesService;
import ru.krivonogova.autopark.services.VehiclesService;

@RestController
@RequestMapping("/api/managers")
public class ApiManagersController {
	
	private final EnterprisesService enterprisesService;
	private final VehiclesService vehiclesService;
	private final DriversService driversService;
	private final ModelMapper modelMapper;
	
	@Autowired
	public ApiManagersController(EnterprisesService enterprisesService, VehiclesService vehiclesService, ModelMapper modelMapper, DriversService driversService) {
		this.enterprisesService = enterprisesService;
		this.vehiclesService = vehiclesService;
		this.driversService = driversService;
		this.modelMapper = modelMapper;
	}

	@GetMapping
	public ModelAndView start(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		ManagerDetails managerDetails = (ManagerDetails) authentication.getPrincipal();
		
		//System.out.println(managerDetails.getManager().getUsername());
		
		model.addAttribute("manager", managerDetails.getManager());
		
		return new ModelAndView("start"); 
	}
	
	@GetMapping("/{id}/enterprises")
	public List<Enterprise> indexEnterprises(@PathVariable("id") int id) {
		return enterprisesService.findAllForManager(id);
	}
	
	@GetMapping("/{id}/vehicles")
	public List<VehicleDTO> indexVehicles(@PathVariable("id") int id) {
		return vehiclesService.findAllForManager(id).stream().map(this::convertToVehicleDTO)
        		.collect(Collectors.toList());
	}
	
	private VehicleDTO convertToVehicleDTO(Vehicle vehicle) {
		return modelMapper.map(vehicle, VehicleDTO.class);
	}
	
	@GetMapping("/{id}/drivers")
	public List<DriverDTO> indexDrivers(@PathVariable("id") int id) {
		return driversService.findAllForManager(id).stream().map(this::convertToDriverDTO)
        		.collect(Collectors.toList());
	}
	
	private DriverDTO convertToDriverDTO(Driver driver) {
		return modelMapper.map(driver, DriverDTO.class);
	}

}
