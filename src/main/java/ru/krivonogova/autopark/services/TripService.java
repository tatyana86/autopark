package ru.krivonogova.autopark.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.krivonogova.autopark.models.Trip;
import ru.krivonogova.autopark.repositories.TripRepository;

@Service
@Transactional(readOnly = true)
public class TripService {
	
	private final TripRepository tripRepository;

	@Autowired
	public TripService(TripRepository tripRepository) {
		this.tripRepository = tripRepository;
	}
	

	
	public Trip findOne(int id) {
		Optional<Trip> foundTrip = tripRepository.findById(id);
		
		return foundTrip.orElse(null);
	}
	
	@Transactional
    public void save(Trip trip) {
		tripRepository.save(trip);
	}
	
}
