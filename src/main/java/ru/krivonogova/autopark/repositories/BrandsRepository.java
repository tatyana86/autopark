package ru.krivonogova.autopark.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ru.krivonogova.autopark.models.Brand;

@Repository
public interface BrandsRepository extends JpaRepository<Brand, Integer> {

}
