package ru.krivonogova.autopark.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.krivonogova.autopark.models.PointGps;
import ru.krivonogova.autopark.repositories.PointsGpsRepository;

@Service
@Transactional(readOnly = true)
public class PointsGpsService {

	private final PointsGpsRepository pointsGpsRepository;

	@Autowired
	public PointsGpsService(PointsGpsRepository pointsGpsRepository) {
		this.pointsGpsRepository = pointsGpsRepository;
	}

	public List<PointGps> findAll() {
		List<PointGps> res = pointsGpsRepository.findAll();
		return res;
	}

    public Optional<PointGps> findById(int id) {
        return pointsGpsRepository.findById(id);
    }    

}
