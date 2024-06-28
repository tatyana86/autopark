package ru.krivonogova.autopark.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import ru.krivonogova.autopark.security.PersonDetails;
/*
@RestController
public class KafkaController {
	
	private final KafkaProducer kafkaProducer;

	@Autowired
	public KafkaController(KafkaProducer kafkaProducer) {
		this.kafkaProducer = kafkaProducer;
	}
	
	@PostMapping("/kafka/test")
	public String send(@RequestBody String message) {
		
		kafkaProducer.sendMessage(message);
		
		Integer idManager = getManagerId();
		
		kafkaProducer.sendMessage("Выполнен вход пользователем: id=" + idManager);
		
		return "Success";
	}

	private Integer getManagerId() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
		Integer idManager = personDetails.getPerson().getId();
		
		return idManager;
	}
}*/
