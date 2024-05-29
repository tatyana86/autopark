package ru.krivonogova.autopark.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ru.krivonogova.autopark.models.Driver;


@Repository
public interface DriversRepository extends JpaRepository<Driver, Integer> {

	List<Driver> findDriversByEnterprise_id(int id);
}
