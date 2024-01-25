package ru.krivonogova.autopark.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.krivonogova.autopark.models.Enterprise;
import ru.krivonogova.autopark.repositories.EnterprisesRepository;

@Service
@Transactional(readOnly = true)
public class EnterprisesService {
	
	private final EnterprisesRepository enterprisesRepository;

	@Autowired
	public EnterprisesService(EnterprisesRepository enterprisesRepository) {
		this.enterprisesRepository = enterprisesRepository;
	}
	
	public List<Enterprise> findAll() {
		return enterprisesRepository.findAll();
	}
	
	public Enterprise findOne(int id) {
		Optional<Enterprise> foundEnterprise = enterprisesRepository.findById(id);
		return foundEnterprise.orElse(null);
	}
	
}
