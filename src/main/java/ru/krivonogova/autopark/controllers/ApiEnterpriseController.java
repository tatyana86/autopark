package ru.krivonogova.autopark.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ru.krivonogova.autopark.models.Enterprise;
import ru.krivonogova.autopark.services.EnterprisesService;

@RestController
@RequestMapping("/api/enterprises")
public class ApiEnterpriseController {
	
	private final EnterprisesService enterprisesService;

	@Autowired
	public ApiEnterpriseController(EnterprisesService enterprisesService) {
		this.enterprisesService = enterprisesService;
	}
	
	@GetMapping
	public List<Enterprise> index() {
		return enterprisesService.findAll();
	}
	
	@GetMapping("/{id}")
	public Enterprise show(@PathVariable("id") int id) {
		return enterprisesService.findOne(id);
	}
}
