package ru.krivonogova.autopark.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import ru.krivonogova.autopark.models.Vehicle;
import ru.krivonogova.autopark.services.VehiclesService;

@Controller
@RequestMapping("/vehicles")
public class VehiclesController {
	
	private final VehiclesService vehiclesService;

	@Autowired
	public VehiclesController(VehiclesService vehiclesService) {
		this.vehiclesService = vehiclesService;
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
	
}
