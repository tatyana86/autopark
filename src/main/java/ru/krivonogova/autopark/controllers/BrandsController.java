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

import jakarta.validation.Valid;
import ru.krivonogova.autopark.models.Brand;
import ru.krivonogova.autopark.models.TypeVehicle;
import ru.krivonogova.autopark.services.BrandsService;

@Controller
@RequestMapping("/brands")
public class BrandsController {
	
	private final BrandsService brandsService;

	@Autowired
	public BrandsController(BrandsService brandsService) {
		this.brandsService = brandsService;
	}
	
	@GetMapping
	public String index(Model model) {
		model.addAttribute("brands", brandsService.findAll());
        return "brands/index";
	}
	
	@GetMapping("/{id}")
	public String show(@PathVariable("id") int id, Model model, 
						@ModelAttribute("vehicle") Brand brand) {
		model.addAttribute("brand", brandsService.findOne(id));
		
		return "brands/show";
	}	
	
	@GetMapping("/new")
	public String newBrand(@ModelAttribute("brand") Brand brand,
							Model model) {	
				
		TypeVehicle[] types = TypeVehicle.values();
		model.addAttribute("types", types);
		
		return "brands/new";
	}
	
    @PostMapping
	public String create(@ModelAttribute("brand") @Valid Brand brand,
						BindingResult bindingResult) {
		if(bindingResult.hasErrors()) {
			return "brands/new";
		}
		
		brandsService.save(brand);
		return "redirect:/brands";
	}
    
    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
    	
    	TypeVehicle[] types = TypeVehicle.values();
		model.addAttribute("types", types);
    	
    	model.addAttribute("brand", brandsService.findOne(id));
    	
    	return "brands/edit";
    }
	
    @PatchMapping("/{id}")
    public String update(@ModelAttribute("brand") @Valid Brand brand,
    					BindingResult bindingResult,
    					@PathVariable("id") int id) {
		if(bindingResult.hasErrors()) {
			return "brands/edit";
		}
		
		brandsService.update(id, brand);
		return "redirect:/brands";
    }
    
    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
    	brandsService.delete(id);
    	return "redirect:/brands";
    }
	

}
