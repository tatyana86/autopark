package ru.krivonogova.autopark.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ru.krivonogova.autopark.models.Manager;

@Repository
public interface ManagersRepository extends JpaRepository<Manager, Integer> {
	Optional<Manager> findByUsername(String username);
}
