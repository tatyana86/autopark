describe("Create vehicle", () => {

	beforeEach(() => {
		cy.login("manager1", "pass111");
	});

	it("Should create vehicle", () => {

		// переход на страницу автомобилей первого предприятия в таблице
		cy.get('[data-cy="vehicles-link"]:first').click();
		cy.url().should("include", "/managers/enterprises/1/vehicles");
		
		// создаем машинку	
		cy.get('[data-cy="add-vehicle-button"]').click();
	    cy.get('[data-cy="registration-number"]').clear().type('А555АА55');
	    cy.get('[data-cy="year-of-production"]').clear().type('2024');
	    cy.get('[data-cy="price"]').clear().type('20000');
	    cy.get('[data-cy="mileage"]').clear().type('100000');
	    cy.get('[data-cy="save-vehicle"]').click();
		
		// проверяем, что машина есть в таблице и добавлена в конец
		
		// функция для перехода на следующую страницу и проверки последней строки
		const goToNextPage = () => {
		  cy.get('li.page-item').last().then(($el) => {
		    // проверка, если есть кнопка "следующая страница"
		    if (!$el.hasClass('disabled')) {
		      cy.wrap($el).find('a.page-link').click();
		      goToNextPage();
		    } else {
		          // проверяем последнюю строку таблицы на наличие созданной машины
		          cy.get('tbody tr').last().within(() => {
			          cy.get('td').eq(1).should('contain', 'А555АА55'); 
			          cy.get('td').eq(2).should('contain', '2024'); 
		          });
		    }
		  });
		};
		
		// запуск функции перехода на следующую страницу
		goToNextPage();
    
    
	});

});
