package ru.krivonogova.autopark.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ru.krivonogova.autopark.models.Vehicle;

@Repository
public interface VehiclesRepository extends JpaRepository<Vehicle, Integer> {

}
