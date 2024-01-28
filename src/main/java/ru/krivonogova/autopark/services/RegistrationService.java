package ru.krivonogova.autopark.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.krivonogova.autopark.models.Manager;
import ru.krivonogova.autopark.repositories.ManagersRepository;

@Service
public class RegistrationService {
    private final ManagersRepository managersRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public RegistrationService(ManagersRepository managersRepository, PasswordEncoder passwordEncoder) {
        this.managersRepository = managersRepository;
		this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void register(Manager manager) {
        manager.setPassword(passwordEncoder.encode(manager.getPassword()));
        managersRepository.save(manager);
    }
}
