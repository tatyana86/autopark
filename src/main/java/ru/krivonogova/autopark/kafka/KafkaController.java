package ru.krivonogova.autopark.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KafkaController {
	
	private final KafkaProducer kafkaProducer;

	@Autowired
	public KafkaController(KafkaProducer kafkaProducer) {
		this.kafkaProducer = kafkaProducer;
	}
	
	@PostMapping("kafka/test")
	public String send(@RequestBody String message) {
		
		kafkaProducer.sendMessage(message);
		
		return "Success";
	}

}
