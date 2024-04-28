package ru.krivonogova.autopark.controllers;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.validation.Valid;
import ru.krivonogova.autopark.dto.VehicleDTO;
import ru.krivonogova.autopark.models.Vehicle;
import ru.krivonogova.autopark.security.PersonDetails;
import ru.krivonogova.autopark.services.BrandsService;
import ru.krivonogova.autopark.services.VehiclesService;

@Controller
@RequestMapping("/vehicles")
public class VehiclesController {
	
	private final VehiclesService vehiclesService;
	private final BrandsService brandsService;
	private final ModelMapper modelMapper;

	@Autowired
	public VehiclesController(VehiclesService vehiclesService, BrandsService brandsService, ModelMapper modelMapper) {
		this.vehiclesService = vehiclesService;
		this.brandsService = brandsService;
		this.modelMapper = modelMapper;
	}
	
	@GetMapping
	public String index(@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
						@RequestParam(value = "itemsPerPage", required = false, defaultValue = "10") Integer itemsPerPage,
						Model model) {
		
		Integer idPerson = getPersonId();
		String timezone = getPersonTimezone();
		
		Page<Vehicle> vehiclesPage = vehiclesService.findAll(page, itemsPerPage);
		
		model.addAttribute("vehicles", vehiclesPage.getContent().stream().map(vehicle -> convertToVehicleDTO(vehicle, timezone)).collect(Collectors.toList()));
	    model.addAttribute("currentPage", vehiclesPage.getNumber() + 1);
	    model.addAttribute("totalPages", vehiclesPage.getTotalPages());
	    model.addAttribute("hasNext", vehiclesPage.hasNext());
	    model.addAttribute("hasPrevious", vehiclesPage.hasPrevious());

        return "vehicles/index_preview";
	}
	
	private Integer getPersonId() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
		Integer idPerson = personDetails.getPerson().getId();
		
		return idPerson;
	}
	
	private String getPersonTimezone() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
	    String timezone = personDetails.getPerson().getTimezone();   
	    
	    return timezone;
	}
	
	@GetMapping("/{id}")
	public String show(@PathVariable("id") int id, Model model, 
						@ModelAttribute("vehicle") Vehicle vehicle) {
		model.addAttribute("vehicle", vehiclesService.findOne(id));
		
		return "vehicles/show";
	}
	
	@GetMapping("/new")
	public String newVehicle(@ModelAttribute("vehicle") Vehicle vehicle,
							Model model) {
		
		model.addAttribute("brands", brandsService.findAll());
		
		return "vehicles/new";
	}
	
	@PostMapping
	public String create(@RequestParam("brandId") int brandId,
						Model model,
						@ModelAttribute("vehicle") @Valid Vehicle vehicle,
						BindingResult bindingResult) {
				
    	if(bindingResult.hasErrors()) {
    		model.addAttribute("brands", brandsService.findAll());
    		return "vehicles/new";
    	}
		    	    	
		vehiclesService.save(vehicle, brandId);
		
		return "redirect:/vehicles";
	}
	
	@GetMapping("/{id}/edit")
	public String edit(Model model, @PathVariable("id") int id) {
		model.addAttribute("vehicle", vehiclesService.findOne(id));
		model.addAttribute("brands", brandsService.findAll());
		
		return "vehicles/edit";
	}
	
	@PatchMapping("/{id}")
	public String update(@RequestParam("brandId") int brandId,
						Model model,
						@ModelAttribute("vehicle") @Valid Vehicle vehicle,
						BindingResult bindingResult,
						@PathVariable("id") int id) {
		
		if(bindingResult.hasErrors()) {
			model.addAttribute("brands", brandsService.findAll());
			return "vehicles/edit";
		}
		
		vehiclesService.update(id, vehicle, brandId);
		
		return "redirect:/vehicles";
	}
	
	@DeleteMapping("/{id}")
	public String delete(@PathVariable("id") int id) {
		vehiclesService.delete(id);
		
		return "redirect:/vehicles";
	}
	
	private VehicleDTO convertToVehicleDTO(Vehicle vehicle, String timezone) {
	    
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
	    LocalDateTime dateOfSale_UTC = LocalDateTime.parse(vehicle.getDateOfSale(), formatter);
	    ZoneOffset timeZone = ZoneOffset.of(timezone);
	    LocalDateTime dateOfSale = dateOfSale_UTC.atZone(ZoneOffset.UTC).withZoneSameInstant(timeZone).toLocalDateTime();
	    String dateOfSaleForManager = dateOfSale.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm"));

	    VehicleDTO vehicleDTO = modelMapper.map(vehicle, VehicleDTO.class);
	    vehicleDTO.setDateOfSaleForManager(dateOfSaleForManager);
	    
	    // System.out.println(vehicleDTO.getDateOfSale());
	    // System.out.println(vehicleDTO.getDateOfSaleForManager());
	    
	    return vehicleDTO;
	}
}
