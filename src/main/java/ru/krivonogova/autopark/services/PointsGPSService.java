package ru.krivonogova.autopark.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.krivonogova.autopark.models.PointGPS;
import ru.krivonogova.autopark.repositories.PointsGPSRepository;

@Service
@Transactional(readOnly = true)
public class PointsGPSService {

	private final PointsGPSRepository pointsGPSRepository;

	@Autowired
	public PointsGPSService(PointsGPSRepository pointsGPSRepository) {
		this.pointsGPSRepository = pointsGPSRepository;
	}
	
	public List<PointGPS> findAll() {
		return pointsGPSRepository.findAll();
	}
	
}
