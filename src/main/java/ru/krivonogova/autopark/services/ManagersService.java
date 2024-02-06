package ru.krivonogova.autopark.services;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.krivonogova.autopark.models.Manager;
import ru.krivonogova.autopark.repositories.ManagersRepository;

@Service
@Transactional(readOnly = true)
public class ManagersService {
	
	private final ManagersRepository managersRepository;
	
	public ManagersService(ManagersRepository managersRepository) {
		this.managersRepository = managersRepository;
	}

	public Manager findOne(int id) {
		Optional<Manager> foundManager = managersRepository.findById(id);
		return foundManager.orElse(null);
	}
	
	public Manager findByUsername(String username) {
		Optional<Manager> foundManager = managersRepository.findByUsername(username);
		return foundManager.orElse(null);
	}

}
