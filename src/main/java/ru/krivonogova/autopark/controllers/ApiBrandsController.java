package ru.krivonogova.autopark.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ru.krivonogova.autopark.models.Brand;
import ru.krivonogova.autopark.services.BrandsService;

@RestController
@RequestMapping("/api/brands")
public class ApiBrandsController {

	private final BrandsService brandsService;

	@Autowired
	public ApiBrandsController(BrandsService brandsService) {
		this.brandsService = brandsService;
	}
	
	@GetMapping
	public List<Brand> index() {
        return brandsService.findAll();
	}
	
	@GetMapping("/{id}")
	public Brand show(@PathVariable("id") int id) {
		return brandsService.findOne(id);
	}	
}
