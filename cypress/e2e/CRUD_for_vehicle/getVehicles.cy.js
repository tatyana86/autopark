describe("Get vehicles", () => {
	
	beforeEach(() => {
		cy.login("manager1", "pass111");
		});

	it("Should navigate to vehicles page", () => {
		
		// проверка перехода на нужную страницу после авторизации
    	cy.url().should("include", "/managers/enterprises");
    	
    	// проверка заголовка страницы
    	cy.get("#page-title").should("contain", "Мои предприятия:");
    
	    // проверка наличия таблицы предприятий
	    cy.get("#enterprises-table").should("be.visible");
    	    
	    // переход на страницу автомобилей первого предприятия в таблице
		cy.get('[data-cy="vehicles-link"]:first').click();
		cy.url().should("include", "/managers/enterprises/1/vehicles");
	    
	    // проверка заголовка страницы
    	cy.get("#page-title").should("contain", "Текущий автопарк:");
    	
    	// проверка наличия таблицы авто
    	cy.get("#vehicles-table").should("be.visible");
	    
	    // проверка наличия кнопки добавления авто
    	cy.get('[data-cy="add-vehicle-button"]').should("be.visible");
 	});

});
