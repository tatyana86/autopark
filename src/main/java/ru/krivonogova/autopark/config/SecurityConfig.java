package ru.krivonogova.autopark.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

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
        return new BCryptPasswordEncoder();
    }
	
	@Bean
	public SecurityFilterChain configure(HttpSecurity http) throws Exception {
		AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
		authenticationManagerBuilder.userDetailsService(managerDetailsService);
		
		AuthenticationManager authenticationManager = authenticationManagerBuilder.build();
		
		http.csrf(AbstractHttpConfigurer::disable);
		
		//http.csrf(csrf -> csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()));
		
        http.authenticationManager(authenticationManager)
        .authorizeHttpRequests((authz) -> authz
                .requestMatchers("/auth/login", "/auth/registration").permitAll()
                .anyRequest().authenticated()
        );
		
        http.formLogin((formLogin) ->
        formLogin
                .loginPage("/auth/login")
                .loginProcessingUrl("/process_login")
                .defaultSuccessUrl("/api/managers", true)
                .failureUrl("/auth/login?error")
        		);
		
        http.logout((logout) ->
        logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/auth/login")
        		);
        		
		return http.build();
	}
	
}
