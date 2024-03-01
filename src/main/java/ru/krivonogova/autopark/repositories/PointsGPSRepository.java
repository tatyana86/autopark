package ru.krivonogova.autopark.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ru.krivonogova.autopark.models.PointGPS;

@Repository
public interface PointsGPSRepository extends JpaRepository<PointGPS, Integer>{

}
