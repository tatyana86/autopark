package ru.krivonogova.autopark.controllers;

import org.springframework.beans.factory.annotation.Autowired;
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
import ru.krivonogova.autopark.models.Brand;
import ru.krivonogova.autopark.models.Vehicle;
import ru.krivonogova.autopark.repositories.BrandsRepository;
import ru.krivonogova.autopark.services.BrandsServices;
import ru.krivonogova.autopark.services.VehiclesService;

@Controller
@RequestMapping("/vehicles")
public class VehiclesController {
	
	private final VehiclesService vehiclesService;
	private final BrandsServices brandsServices;

	@Autowired
	public VehiclesController(VehiclesService vehiclesService, BrandsServices brandsServices) {
		this.vehiclesService = vehiclesService;
		this.brandsServices = brandsServices;
	}
	
	@GetMapping
	public String index(Model model) {
		model.addAttribute("vehicles", vehiclesService.findAll());
        return "vehicles/index";
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
		
		model.addAttribute("brands", brandsServices.findAll());
		
		return "vehicles/new";
	}
	
	@PostMapping
	public String create(@RequestParam("brandId") int brandId,
						Model model,
						@ModelAttribute("vehicle") @Valid Vehicle vehicle,
						BindingResult bindingResult) {
				
    	if(bindingResult.hasErrors()) {
    		model.addAttribute("brands", brandsServices.findAll());
    		return "vehicles/new";
    	}
		    	    	
		vehiclesService.save(vehicle, brandId);
		
		return "redirect:/vehicles";
	}
	
	@GetMapping("/{id}/edit")
	public String edit(Model model, @PathVariable("id") int id) {
		model.addAttribute("vehicle", vehiclesService.findOne(id));
		model.addAttribute("brands", brandsServices.findAll());
		
		return "vehicles/edit";
	}
	
	@PatchMapping("/{id}")
	public String update(@RequestParam("brandId") int brandId,
						Model model,
						@ModelAttribute("vehicle") @Valid Vehicle vehicle,
						BindingResult bindingResult,
						@PathVariable("id") int id) {
		
		if(bindingResult.hasErrors()) {
			model.addAttribute("brands", brandsServices.findAll());
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
}
