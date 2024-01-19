package ru.krivonogova.autopark.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import ru.krivonogova.autopark.models.Brand;
import ru.krivonogova.autopark.services.BrandsServices;

@Controller
@RequestMapping("/brands")
public class BrandsController {
	
	private final BrandsServices brandsServices;

	@Autowired
	public BrandsController(BrandsServices brandsServices) {
		this.brandsServices = brandsServices;
	}
	
	@GetMapping
	public String index(Model model) {
		model.addAttribute("brands", brandsServices.findAll());
        return "brands/index";
	}
	
	@GetMapping("/{id}")
	public String show(@PathVariable("id") int id, Model model, 
						@ModelAttribute("vehicle") Brand brand) {
		model.addAttribute("brand", brandsServices.findOne(id));
		
		return "brands/show";
	}	

}
