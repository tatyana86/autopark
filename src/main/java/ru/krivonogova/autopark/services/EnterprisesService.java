package ru.krivonogova.autopark.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.krivonogova.autopark.models.Enterprise;
import ru.krivonogova.autopark.models.Manager;
import ru.krivonogova.autopark.repositories.EnterprisesRepository;
import ru.krivonogova.autopark.repositories.ManagersRepository;

@Service
@Transactional(readOnly = true)
public class EnterprisesService {
	
	private final EnterprisesRepository enterprisesRepository;
	private final ManagersRepository managersRepository;

	@Autowired
	public EnterprisesService(EnterprisesRepository enterprisesRepository, ManagersRepository managersRepository) {
		this.enterprisesRepository = enterprisesRepository;
		this.managersRepository = managersRepository;
	}
	
	public List<Enterprise> findAll() {
		return enterprisesRepository.findAll();
	}
	
	public Enterprise findOne(int id) {
		Optional<Enterprise> foundEnterprise = enterprisesRepository.findById(id);
		return foundEnterprise.orElse(null);
	}
	
	public List<Enterprise> findAllForManager(int id) {
		return enterprisesRepository.findEnterprisesByManagers_id(id);
	}
	
	@Transactional
	public void save(Enterprise enterprise, int id) {
		Optional<Manager> foundManager = managersRepository.findById(id);
		
		if(enterprise.getManagers() == null) {
			enterprise.setManagers(new ArrayList<>());
		}
		
		enterprise.getManagers().add(foundManager.get());
	
		enterprisesRepository.save(enterprise);
	}
	
	@Transactional
	public void update(int id, Enterprise updatedEnterprise) {
		List<Manager> managers = enterprisesRepository.findById(id).get().getManagers();
		
		updatedEnterprise.setManagers(managers);
		updatedEnterprise.setId(id);
		
		enterprisesRepository.save(updatedEnterprise);
	}
	
	@Transactional
	public void delete(int id) {
		//enterprisesRepository.deleteById(id);
		System.out.print("tut");
		Enterprise enterprise = enterprisesRepository.findById(id).get();
		        
        // Удаление предприятия из списка у всех менеджеров, у которых оно есть
        for (Manager manager : enterprise.getManagers()) {
            manager.getEnterprises().remove(enterprise);
        }

        enterprisesRepository.deleteById(id);
	}
	
}
