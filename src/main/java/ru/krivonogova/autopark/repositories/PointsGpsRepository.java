package ru.krivonogova.autopark.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.krivonogova.autopark.models.PointGps;


@Repository
public interface PointsGpsRepository extends JpaRepository<PointGps, Integer>{

}
