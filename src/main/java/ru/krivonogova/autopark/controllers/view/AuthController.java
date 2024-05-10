package ru.krivonogova.autopark.controllers.view;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import ru.krivonogova.autopark.models.Person;
import ru.krivonogova.autopark.services.RegistrationService;

@Controller
@RequestMapping("/auth")
public class AuthController {
	
	private final RegistrationService registrationService;
	
    @Autowired
	public AuthController(RegistrationService registrationService) {
		this.registrationService = registrationService;
	}

	@GetMapping("/login")
	public String loginPage() {
		return "auth/login";
	}
	
    @GetMapping("/registration")
    public String registrationPage(@ModelAttribute("person") Person person) {
    	return "auth/registration";
    }
    
    @PostMapping("/registration")
    public String performRegistration(@ModelAttribute("person") Person person,
                                      BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "/auth/registration";

        registrationService.register(person);

        return "redirect:/auth/login";
    }
        
}
