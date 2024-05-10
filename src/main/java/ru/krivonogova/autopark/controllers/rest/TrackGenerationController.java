package ru.krivonogova.autopark.controllers.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ru.krivonogova.autopark.dto.BigDataGenerationDTO;
import ru.krivonogova.autopark.dto.TrackGenerationDTO;
import ru.krivonogova.autopark.services.TrackGenerationService;

@RestController
@RequestMapping("/api/routing")
public class TrackGenerationController {
	
	private final TrackGenerationService trackGenerationService;

	@Autowired
	public TrackGenerationController(TrackGenerationService trackGenerationService) {
		this.trackGenerationService = trackGenerationService;
	}
	
	@PostMapping
	public void generateTrack(@RequestBody TrackGenerationDTO request) {
		trackGenerationService.generateTrack(request);
	}
	
	@PostMapping("/bigdata")
	public void generateBigData(@RequestBody BigDataGenerationDTO request) throws InterruptedException {
		trackGenerationService.generateBigData(request);
	}
	
}
