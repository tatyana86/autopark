package ru.krivonogova.autopark.controllers.view;

import org.modelmapper.ModelMapper;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import ru.krivonogova.autopark.controllers.DatabaseController;
import ru.krivonogova.autopark.models.Brand;
import ru.krivonogova.autopark.models.TypeVehicle;

@RestController
@RequestMapping
public class PrimitiveController {

	private final DatabaseController databaseController;
	private final ModelMapper modelMapper;

	public PrimitiveController(DatabaseController databaseController, ModelMapper modelMapper) {
		this.modelMapper = modelMapper;
		this.databaseController = databaseController;
	}
	
	// CRUD для брендов
	
	@GetMapping("/brands")
	public String index(Model model) {
		model.addAttribute("brands", databaseController.findAllBrands());
        return "brands/index";
	}
	
	@GetMapping("/brands/{id}")
	public String show(@PathVariable("id") int id, Model model, 
						@ModelAttribute("vehicle") Brand brand) {
		model.addAttribute("brand", databaseController.findOneBrand(id));
		
		return "brands/show";
	}	
	
	@GetMapping("/brands/new")
	public String newBrand(@ModelAttribute("brand") Brand brand,
							Model model) {	
				
		TypeVehicle[] types = TypeVehicle.values();
		model.addAttribute("types", types);
		
		return "brands/new";
	}
	
    @PostMapping("/brands")
	public String create(@ModelAttribute("brand") @Valid Brand brand,
						BindingResult bindingResult) {
		if(bindingResult.hasErrors()) {
			return "brands/new";
		}
		
		databaseController.saveBrand(brand);
		return "redirect:/brands";
	}
	
    @GetMapping("/brands/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
    	
    	TypeVehicle[] types = TypeVehicle.values();
		model.addAttribute("types", types);
    	
    	model.addAttribute("brand", databaseController.findOneBrand(id));
    	
    	return "brands/edit";
    }
	
    @PatchMapping("/brands/{id}")
    public String update(@ModelAttribute("brand") @Valid Brand brand,
    					BindingResult bindingResult,
    					@PathVariable("id") int id) {
		if(bindingResult.hasErrors()) {
			return "brands/edit";
		}
		
		databaseController.updateBrand(id, brand);
		return "redirect:/brands";
    }
    
    @DeleteMapping("/brands/{id}")
    public String delete(@PathVariable("id") int id) {
    	databaseController.deleteBrand(id);
    	return "redirect:/brands";
    }
    
    // CRUD для транспорта
    
}
