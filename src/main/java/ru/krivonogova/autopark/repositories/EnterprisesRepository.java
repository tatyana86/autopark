package ru.krivonogova.autopark.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ru.krivonogova.autopark.models.Enterprise;

@Repository
public interface EnterprisesRepository extends JpaRepository<Enterprise, Integer> {

	List<Enterprise> findEnterprisesByManagers_id(int id);
}
