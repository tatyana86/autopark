describe("Edit vehicle", () => {

  it("Edit vehicle", () => {
	// авторизация
    cy.visit("http://localhost:8080/auth/login");
    cy.get("#username").type("manager1");
    cy.get("#password").type("pass111");
    cy.get("button[type='submit']").click();
    
    // список машин
	cy.get('a[href="/managers/enterprises/1/vehicles"]').click();
	
	// вносим изменения в карточке
	cy.get('tbody .icon-link').first().should('have.attr', 'title', 'Редактировать').click();
	cy.get('#registrationNumber').clear().type('А555АА55');
	cy.get('#yearOfProduction').clear().type('2024');
    cy.get('input[type="submit"]').contains('Сохранить изменения').click();
    
    // проверяем изменения
    cy.get('tbody tr').first().within(() => {
      cy.get('td').eq(1).should('contain', 'А555АА55'); // проверка регистрационного номера
      cy.get('td').eq(2).should('contain', '2024'); // проверка года выпуска
    });
  });

});
