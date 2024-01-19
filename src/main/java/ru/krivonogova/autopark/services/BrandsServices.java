package ru.krivonogova.autopark.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.krivonogova.autopark.models.Brand;
import ru.krivonogova.autopark.repositories.BrandsRepository;

@Service
@Transactional(readOnly = true)
public class BrandsServices {
	
	private final BrandsRepository brandsRepository;

	@Autowired
	public BrandsServices(BrandsRepository brandsRepository) {
		this.brandsRepository = brandsRepository;
	}
	
	public List<Brand> findAll() {
		return brandsRepository.findAll();
	}
	
	public Brand findOne(int id) {
		Optional<Brand> foundBrand = brandsRepository.findById(id);
		return foundBrand.orElse(null);
	}

}
