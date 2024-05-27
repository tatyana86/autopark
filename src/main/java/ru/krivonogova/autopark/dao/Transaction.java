package ru.krivonogova.autopark.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import ru.krivonogova.autopark.models.Enterprise;

@Slf4j
@Component
public class Transaction {

	private final SessionFactory sessionFactory;

	@Autowired
	public Transaction(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
		
	public void goInside() {
		Session session = sessionFactory.openSession();
		Enterprise enterprise = new Enterprise();
		enterprise.setName("Transaction");
		enterprise.setCity("Hibernate");
		enterprise.setPhone("5555");
		enterprise.setTimezone("+00:00");

		try {
	        session.beginTransaction();
	        log.info("Inside transaction");

	        session.persist(enterprise);
	        log.info("New enterprise was added");

	        session.getTransaction().commit();
		} finally {
	        session.close();
	    }
	
	}
	
}
