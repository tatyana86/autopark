describe("Edit enterpise", () => {

	beforeEach(() => {
		cy.login("manager1", "pass111");
	});

	it("Should edit first enterpise", () => {
		
		// вносим изменения в карточке
		cy.get('tbody .icon-link').first().should('have.attr', 'title', 'Редактировать').click();
		cy.get('#phone').clear().type('5555'); 
		cy.get('input[type="submit"]').contains('Сохранить изменения').click();
		
		// проверяем изменения
		cy.get('tbody tr').first().within(() => {
			cy.get('td').eq(3).should('contain.text', '5555');
		});
	});

});
