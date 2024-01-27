package ru.krivonogova.autopark.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import ru.krivonogova.autopark.services.ManagerDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	private final ManagerDetailsService managerDetailsService;

	@Autowired	
	public SecurityConfig(ManagerDetailsService managerDetailsService) {
		this.managerDetailsService = managerDetailsService;
	}
	
    @Bean
    public PasswordEncoder passwordEncoder() {
        // Замените NoOpPasswordEncoder.getInstance() на вашу реализацию PasswordEncoder
        return new BCryptPasswordEncoder();
    }
	
	//@Bean
	public SecurityFilterChain configure(HttpSecurity http) throws Exception {
		AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
		authenticationManagerBuilder.userDetailsService(managerDetailsService);
		
		AuthenticationManager authenticationManager = authenticationManagerBuilder.build();
		
		
		http.csrf(AbstractHttpConfigurer::disable);
		

		
		return http.build();
	}
	
}
