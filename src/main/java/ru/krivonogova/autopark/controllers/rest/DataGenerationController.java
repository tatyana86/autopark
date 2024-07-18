package ru.krivonogova.autopark.controllers.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.tags.Tag;
import ru.krivonogova.autopark.dto.DataGenerationDTO;
import ru.krivonogova.autopark.services.DataGenerationService;
import ru.krivonogova.autopark.util.DataGenerationErrorResponse;
import ru.krivonogova.autopark.util.DataGenerationException;

@Hidden
@Tag(name = "Секретный REST-контроллер", 
	description = "Контроллер для генерации данных для БД")
@RestController
@RequestMapping("api/admin")
public class DataGenerationController {
	
	private final DataGenerationService dataGenerationService;

	@Autowired
	public DataGenerationController(DataGenerationService dataGenerationService) {
		this.dataGenerationService = dataGenerationService;
	}

	@PostMapping
	public ResponseEntity<HttpStatus> generate(@RequestBody DataGenerationDTO request,
												BindingResult bindingResult) {
		
		if(bindingResult.hasErrors()) {
    		StringBuilder errorMsg = new StringBuilder();
    		
    		List<FieldError> errors = bindingResult.getFieldErrors();
    		
    		for(FieldError error : errors) {
    			errorMsg.append(error.getField())
    					.append(" - ")
    					.append(error.getDefaultMessage())
    					.append(";");
    		}
    		
    		throw new DataGenerationException(errorMsg.toString());
    	}
		
		dataGenerationService.generate(request);
		
		return ResponseEntity.ok(HttpStatus.OK);
		
	}

    @ExceptionHandler
    private ResponseEntity<DataGenerationErrorResponse> handlerException(DataGenerationException e) {
    	DataGenerationErrorResponse response = new DataGenerationErrorResponse(
    			e.getMessage(), 
    			System.currentTimeMillis()
    	);
    	
    	return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST); // статус 400
    }

}
