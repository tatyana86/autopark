package ru.krivonogova.autopark.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import ru.krivonogova.autopark.models.Manager;
import ru.krivonogova.autopark.repositories.ManagersRepository;
import ru.krivonogova.autopark.security.ManagerDetails;

@Service
public class ManagerDetailsService implements UserDetailsService {
	
	private final ManagersRepository managersRepository;

	@Autowired
	public ManagerDetailsService(ManagersRepository managersRepository) {
		this.managersRepository = managersRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<Manager> manager = managersRepository.findByUsername(username);
		
		if(manager.isEmpty()) {
			throw new UsernameNotFoundException("Manager not found!");
		}
		
		return new ManagerDetails(manager.get());
	}
	
}
