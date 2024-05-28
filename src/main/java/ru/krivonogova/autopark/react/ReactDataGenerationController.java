package ru.krivonogova.autopark.react;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;
import ru.krivonogova.autopark.dto.DataGenerationDTO;
import ru.krivonogova.autopark.util.DataGenerationErrorResponse;
import ru.krivonogova.autopark.util.DataGenerationException;
/*
@RestController
@RequestMapping("api/react")
public class ReactDataGenerationController {

	private final ReactDataGenerationService reactDataGenerationService;

    @Autowired
    public ReactDataGenerationController(ReactDataGenerationService reactDataGenerationService) {
		this.reactDataGenerationService = reactDataGenerationService;
	}

    @PostMapping
    public Mono<ResponseEntity<String>> generate(@RequestBody DataGenerationDTO request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder errorMsg = new StringBuilder();
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors) {
                errorMsg.append(error.getField())
                        .append(" - ")
                        .append(error.getDefaultMessage())
                        .append(";");
            }
            throw new DataGenerationException(errorMsg.toString());
        }
        reactDataGenerationService.generate(request);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return Mono.just(ResponseEntity.ok().headers(headers).body("Data generated successfully"));
    }

    @ExceptionHandler
    private Mono<ResponseEntity<DataGenerationErrorResponse>> handleException(DataGenerationException e) {
        DataGenerationErrorResponse response = new DataGenerationErrorResponse(
                e.getMessage(),
                System.currentTimeMillis()
        );
        return Mono.just(new ResponseEntity<>(response, HttpStatus.BAD_REQUEST));
    }
	
}*/
