package ru.krivonogova.autopark.security;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.extern.slf4j.Slf4j;
import ru.krivonogova.autopark.models.Person;

@Slf4j
public class PersonDetails implements UserDetails {
	
	private final Person person;

	public PersonDetails(Person person) {
		this.person = person;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		log.info("logged in by: {}", person.getRole());
		return Collections.singletonList(new SimpleGrantedAuthority(person.getRole()));
	}

	@Override
	public String getPassword() {
		return this.person.getPassword();
	}

	@Override
	public String getUsername() {
		return this.person.getUsername();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
	
	// чтобы получать данные аутентифицированного менеджера
//	public Manager getManager() {
//		return this.manager;
//	}
	
	public Person getPerson() {
		return this.person;
	}

}
