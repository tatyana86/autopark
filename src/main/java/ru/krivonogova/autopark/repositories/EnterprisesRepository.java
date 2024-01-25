package ru.krivonogova.autopark.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ru.krivonogova.autopark.models.Enterprise;

@Repository
public interface EnterprisesRepository extends JpaRepository<Enterprise, Integer> {

}
