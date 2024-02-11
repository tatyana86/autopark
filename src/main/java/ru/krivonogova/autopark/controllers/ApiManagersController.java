package ru.krivonogova.autopark.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import jakarta.validation.Valid;
import ru.krivonogova.autopark.dto.DriverDTO;
import ru.krivonogova.autopark.dto.VehicleDTO;
import ru.krivonogova.autopark.models.Driver;
import ru.krivonogova.autopark.models.Enterprise;
import ru.krivonogova.autopark.models.Vehicle;
import ru.krivonogova.autopark.security.ManagerDetails;
import ru.krivonogova.autopark.security.PersonDetails;
import ru.krivonogova.autopark.services.DriversService;
import ru.krivonogova.autopark.services.EnterprisesService;
import ru.krivonogova.autopark.services.ManagersService;
import ru.krivonogova.autopark.services.VehiclesService;
import ru.krivonogova.autopark.util.EnterpriseErrorResponse;
import ru.krivonogova.autopark.util.EnterpriseNotCreatedException;
import ru.krivonogova.autopark.util.EnterpriseNotDeletedException;
import ru.krivonogova.autopark.util.EnterpriseNotUpdatedException;
import ru.krivonogova.autopark.util.VehicleErrorResponse;
import ru.krivonogova.autopark.util.VehicleNotCreatedException;
import ru.krivonogova.autopark.util.VehicleNotFoundException;
import ru.krivonogova.autopark.util.VehicleNotUpdatedException;

@RestController
@RequestMapping("/api/managers")
public class ApiManagersController {
	
	private final EnterprisesService enterprisesService;
	private final VehiclesService vehiclesService;
	private final DriversService driversService;
	private final ModelMapper modelMapper;
	
	private final ManagersService managersService; //new
	
	@Autowired
	public ApiManagersController(EnterprisesService enterprisesService, VehiclesService vehiclesService, ModelMapper modelMapper, DriversService driversService, ManagersService managersService) {
		this.enterprisesService = enterprisesService;
		this.vehiclesService = vehiclesService;
		this.driversService = driversService;
		this.modelMapper = modelMapper;
		this.managersService = managersService;
	}

	@GetMapping
	public ModelAndView start(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		//ManagerDetails managerDetails = (ManagerDetails) authentication.getPrincipal();
		
		//System.out.println(managerDetails.getManager().getUsername());
		
		//model.addAttribute("manager", managerDetails.getManager());
		
		PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
		
		String username = personDetails.getPerson().getUsername();
		System.out.println(username);
		
		model.addAttribute("manager", managersService.findByUsername(username));
		
		return new ModelAndView("start"); 
	}
	
	@GetMapping("/{id}/enterprises")
	public List<Enterprise> indexEnterprises(@PathVariable("id") int id) {
		return enterprisesService.findAllForManager(id);
	}
	
// так было	
//	@GetMapping("/{id}/vehicles")
//	public List<VehicleDTO> indexVehicles(@PathVariable("id") int id) {
//		return vehiclesService.findAllForManager(id).stream().map(this::convertToVehicleDTO)
//        		.collect(Collectors.toList());
//	}
	
	@GetMapping("/{id}/vehicles")
	public List<VehicleDTO> indexVehicles(@PathVariable("id") int id,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "itemsPerPage", required = false) Integer itemsPerPage) {
		
		if(page == null || itemsPerPage == null) {
			return vehiclesService.findAllForManager(id).stream().map(this::convertToVehicleDTO)
	        		.collect(Collectors.toList());
		}
		
		Page<Vehicle> vehiclePage = vehiclesService.findAllForManager(id, page, itemsPerPage);
		List<VehicleDTO> vehicleDTOList = vehiclePage.getContent().stream().map(this::convertToVehicleDTO)
				.collect(Collectors.toList());
		
		return vehicleDTOList;
	}
	
	private VehicleDTO convertToVehicleDTO(Vehicle vehicle) {
		return modelMapper.map(vehicle, VehicleDTO.class);
	}
	
// так было	
//	@GetMapping("/{id}/drivers")
//	public List<DriverDTO> indexDrivers(@PathVariable("id") int id) {
//		return driversService.findAllForManager(id).stream().map(this::convertToDriverDTO)
//        		.collect(Collectors.toList());
//	}
	
	@GetMapping("/{id}/drivers")
	public List<DriverDTO> indexDrivers(@PathVariable("id") int id,
					@RequestParam(value = "page", required = false) Integer page,
					@RequestParam(value = "itemsPerPage", required = false) Integer itemsPerPage) {
		
		if(page == null || itemsPerPage == null) {
			return driversService.findAllForManager(id).stream().map(this::convertToDriverDTO)
	        		.collect(Collectors.toList());			
		}
		
		Page<Driver> driverPage = driversService.findAllForManager(id, page, itemsPerPage);
		List<DriverDTO> driverDTOList = driverPage.getContent().stream().map(this::convertToDriverDTO)
	                .collect(Collectors.toList());
		
	    return driverDTOList;
	}
	
	private DriverDTO convertToDriverDTO(Driver driver) {
		return modelMapper.map(driver, DriverDTO.class);
	}
	
	@PostMapping("/{id}/vehicles")
	public ResponseEntity<HttpStatus> create(@RequestBody @Valid VehicleDTO vehicle,
											BindingResult bindingResult) {
		
		if(bindingResult.hasErrors()) {
    		StringBuilder errorMsg = new StringBuilder();
    		
    		List<FieldError> errors = bindingResult.getFieldErrors();
    		
    		for(FieldError error : errors) {
    			errorMsg.append(error.getField())
    					.append(" - ")
    					.append(error.getDefaultMessage())
    					.append(";");
    		}
    		
    		throw new VehicleNotCreatedException(errorMsg.toString());
    	}
		
		vehiclesService.save(convertToVehicle(vehicle));
		
		return ResponseEntity.ok(HttpStatus.OK);
	}
	
	@PostMapping("/{id}/enterprises")
	public ResponseEntity<HttpStatus> create(@RequestBody @Valid Enterprise enterprise,
											BindingResult bindingResult,
											@PathVariable("id") int id) {
		
		if(bindingResult.hasErrors()) {
    		StringBuilder errorMsg = new StringBuilder();
    		
    		List<FieldError> errors = bindingResult.getFieldErrors();
    		
    		for(FieldError error : errors) {
    			errorMsg.append(error.getField())
    					.append(" - ")
    					.append(error.getDefaultMessage())
    					.append(";");
    		}
    		
    		throw new EnterpriseNotCreatedException(errorMsg.toString());
    	}
		
		enterprisesService.save(enterprise, id);
		
		return ResponseEntity.ok(HttpStatus.OK);
	}
	
	@PutMapping("/{id}/vehicles/{idVehicle}")
	public ResponseEntity<HttpStatus> update(@RequestBody @Valid VehicleDTO vehicle,
											BindingResult bindingResult,
											@PathVariable("idVehicle") int id) {
		if(bindingResult.hasErrors()) {
			StringBuilder errorMsg = new StringBuilder();
    		
    		List<FieldError> errors = bindingResult.getFieldErrors();
    		
    		for(FieldError error : errors) {
    			errorMsg.append(error.getField())
    					.append(" - ")
    					.append(error.getDefaultMessage())
    					.append(";");
    		}
    		
    		throw new VehicleNotUpdatedException(errorMsg.toString());
		}
		
		vehiclesService.update(id, convertToVehicle(vehicle));
		
		return ResponseEntity.ok(HttpStatus.OK);
	}
	
	@PutMapping("/{id}/enterprises/{idEnterprise}")
	public ResponseEntity<HttpStatus> update(@PathVariable("id") int idManager,
											@RequestBody @Valid Enterprise enterprise,
											BindingResult bindingResult,
											@PathVariable("idEnterprise") int idEnterprise) {
		if(bindingResult.hasErrors()) {
			StringBuilder errorMsg = new StringBuilder();
    		
    		List<FieldError> errors = bindingResult.getFieldErrors();
    		
    		for(FieldError error : errors) {
    			errorMsg.append(error.getField())
    					.append(" - ")
    					.append(error.getDefaultMessage())
    					.append(";");
    		}
    		
    		throw new EnterpriseNotUpdatedException(errorMsg.toString());
		}
		
		enterprisesService.update(idManager, idEnterprise, enterprise);
		
		return ResponseEntity.ok(HttpStatus.OK);
	}
	
	@DeleteMapping("/{id}/vehicles/{idVehicle}")
	public ResponseEntity<HttpStatus> deleteVehicle(@PathVariable("idVehicle") int id) {
		vehiclesService.delete(id);
		
		return ResponseEntity.ok(HttpStatus.OK);
	}
	
	@DeleteMapping("/{id}/enterprises/{idEnterprise}")
	public ResponseEntity<HttpStatus> delete(@PathVariable("id") int idManager,
											@PathVariable("idEnterprise") int idEnterprise) {
		
		enterprisesService.delete(idManager, idEnterprise);
		
		return ResponseEntity.ok(HttpStatus.OK);
	}
	
	private Vehicle convertToVehicle(VehicleDTO vehicleDTO) {
		return modelMapper.map(vehicleDTO, Vehicle.class);
	}
	
	@ExceptionHandler
    private ResponseEntity<VehicleErrorResponse> handlerException(VehicleNotFoundException e) {
    	VehicleErrorResponse response = new VehicleErrorResponse(
    			"Vehicle with this id wasn't found", 
    			System.currentTimeMillis()
    	);
    	
    	return new ResponseEntity<>(response, HttpStatus.NOT_FOUND); // статус 404
    }	
		
    @ExceptionHandler
    private ResponseEntity<VehicleErrorResponse> handlerException(VehicleNotCreatedException e) {
    	VehicleErrorResponse response = new VehicleErrorResponse(
    			e.getMessage(), 
    			System.currentTimeMillis()
    	);
    	
    	return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST); // статус 400
    }
    
    @ExceptionHandler
    private ResponseEntity<VehicleErrorResponse> handlerException(VehicleNotUpdatedException e) {
    	VehicleErrorResponse response = new VehicleErrorResponse(
    			e.getMessage(), 
    			System.currentTimeMillis()
    	);
    	
    	return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST); // статус 400
    }
    
    @ExceptionHandler
    private ResponseEntity<EnterpriseErrorResponse> handlerException(EnterpriseNotCreatedException e) {
    	EnterpriseErrorResponse response = new EnterpriseErrorResponse(
    			e.getMessage(), 
    			System.currentTimeMillis()
    	);
    	
    	return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST); // статус 400
    }
    
    @ExceptionHandler
    private ResponseEntity<EnterpriseErrorResponse> handlerException(EnterpriseNotUpdatedException e) {
    	EnterpriseErrorResponse response = new EnterpriseErrorResponse(
    			e.getMessage(), 
    			System.currentTimeMillis()
    	);
    	
    	return new ResponseEntity<>(response, HttpStatus.FORBIDDEN); // статус 403
    }
    
    @ExceptionHandler
    private ResponseEntity<EnterpriseErrorResponse> handlerException(EnterpriseNotDeletedException e) {
    	EnterpriseErrorResponse response = new EnterpriseErrorResponse(
    			e.getMessage(), 
    			System.currentTimeMillis()
    	);
    	
    	return new ResponseEntity<>(response, HttpStatus.FORBIDDEN); // статус 403
    }

}
