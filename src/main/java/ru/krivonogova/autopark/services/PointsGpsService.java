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
		return pointsGpsRepository.findAll();
	}
	
	public List<PointGps> findAllByVehicleAndTimePeriod(int vehicleId, String dateFrom, String dateTo) {
		return pointsGpsRepository.findAllByVehicleAndTimePeriod(vehicleId, dateFrom, dateTo);
	}
	
	/*public List<PointGps> findAllByVehicleId(int vehicleId) {
		return pointsGpsRepository.findAllByVehicleId(vehicleId);
	}*/
	
    public Optional<PointGps> findById(int id) {
        return pointsGpsRepository.findById(id);
    }
    
    @Transactional
    public void saveAll(List<PointGps> pointsGps) {
    	pointsGpsRepository.saveAll(pointsGps);
    }
    
    @Transactional
    public void save(PointGps pointsGps) {
    	pointsGpsRepository.save(pointsGps);
    }

}
