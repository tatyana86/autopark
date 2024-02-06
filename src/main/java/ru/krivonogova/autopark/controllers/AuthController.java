package ru.krivonogova.autopark.controllers;

import java.util.Map;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import ru.krivonogova.autopark.models.Manager;
import ru.krivonogova.autopark.services.RegistrationService;



@Controller
@RequestMapping("/auth")
public class AuthController {
	
	private final RegistrationService registrationService;
	
    private final ModelMapper modelMapper;
    //private final AuthenticationManager authenticationManager;
	
	@Autowired
	public AuthController(RegistrationService registrationService, ModelMapper modelMapper) {
		this.registrationService = registrationService;
		this.modelMapper = modelMapper;
		//this.authenticationManager = authenticationManager;
	}

	@GetMapping("/login")
	public String loginPage() {
		return "auth/login";
	}
	
    @GetMapping("/registration")
    public String registrationPage(@ModelAttribute("manager") Manager manager) {
    	return "auth/registration";
    }
    
    @PostMapping("/registration")
    public String performRegistration(@ModelAttribute("manager") Manager manager,
                                      BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "/auth/registration";

        registrationService.register(manager);

        return "redirect:/auth/login";
    }
    
//    @ResponseBody
//    @PostMapping("/registration")
//    public Map<String, String> performRegistration(@RequestBody Manager manager,
//                                      BindingResult bindingResult) {
//        if (bindingResult.hasErrors())
//        	return Map.of("message", "Ошибка");
//
//        registrationService.register(manager);
//
//        String token = jwtUtil.generateToken(manager.getUsername());
//        
//        return Map.of("jwt-token", token);
//    }
    
}
