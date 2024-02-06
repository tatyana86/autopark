package ru.krivonogova.autopark.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.krivonogova.autopark.models.Person;
import ru.krivonogova.autopark.repositories.PeopleRepository;

@Service
public class RegistrationService {
    // private final ManagersRepository managersRepository; //depricated
    private final PasswordEncoder passwordEncoder;
    
    private final PeopleRepository peopleRepository;

    @Autowired
  public RegistrationService(PasswordEncoder passwordEncoder, PeopleRepository peopleRepository) {
    this.passwordEncoder = passwordEncoder;
    this.peopleRepository = peopleRepository;
  }

    //depricated
//    @Transactional
//    public void register(Manager manager) {
//        manager.setPassword(passwordEncoder.encode(manager.getPassword()));
//        managersRepository.save(manager);
//    }
    
    @Transactional
    public void register(Person person) {
        person.setPassword(passwordEncoder.encode(person.getPassword()));
        person.setRole("ROLE_USER");
        peopleRepository.save(person);
    }


}
