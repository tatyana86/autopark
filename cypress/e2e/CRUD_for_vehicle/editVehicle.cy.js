describe("Edit vehicle", () => {

	beforeEach(() => {
		cy.login("manager1", "pass111");
	});

	it("Should edit first vehicle", () => {

		// переход на страницу автомобилей первого предприятия в таблице
		cy.get('[data-cy="vehicles-link"]:first').click();
		cy.url().should("include", "/managers/enterprises/1/vehicles");
		
		// функция для редактирования автомобиля
		const editVehicle = (registrationNumber, yearOfProduction) => {
			cy.get('tbody .icon-link').first().should('have.attr', 'title', 'Редактировать').click();
			cy.get('[data-cy="registration-number"]').clear().type(registrationNumber);
			cy.get('[data-cy="year-of-production"]').clear().type(yearOfProduction);
			cy.get('[data-cy="save-vehicle"]').click();
		};

		// функция для проверки изменений
		const checkVehicleDetails = (registrationNumber, yearOfProduction) => {
			cy.get('tbody tr').first().within(() => {
				cy.get('td').eq(1).should('contain', registrationNumber); // проверка регистрационного номера
				cy.get('td').eq(2).should('contain', yearOfProduction); // проверка года выпуска
			});
		};

		// вносим изменения в карточке
		editVehicle('А555АА55', '2024');
	    
		// проверяем изменения
		checkVehicleDetails('А555АА55', '2024');
	    
	});
});
