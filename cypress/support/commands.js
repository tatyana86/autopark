Cypress.Commands.add("login", (username, password) => {
  cy.visit("http://localhost:8080/auth/login");
  cy.get("#username").type(username);
  cy.get("#password").type(password);
  cy.get("button[type='submit']").click();
});

Cypress.Commands.add("loginAndCreateTestVehicle", (username, password) => {
	cy.visit("http://localhost:8080/auth/login");
	cy.get("#username").type(username);
	cy.get("#password").type(password);
	cy.get("button[type='submit']").click();
	
	// переход на страницу автомобилей первого предприятия в таблице
	cy.get('[data-cy="vehicles-link"]:first').click();
  
	// создаем машинку	
	cy.get('[data-cy="add-vehicle-button"]').click();
	cy.get('[data-cy="registration-number"]').clear().type('T888ST88');
	cy.get('[data-cy="year-of-production"]').clear().type('2024');
	cy.get('[data-cy="price"]').clear().type('20000');
	cy.get('[data-cy="mileage"]').clear().type('100000');
	cy.get('[data-cy="save-vehicle"]').click();
});
