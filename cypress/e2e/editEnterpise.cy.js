describe("Edit enterpise", () => {

  it("Edit enterpise", () => {
	// авторизация
    cy.visit("http://localhost:8080/auth/login");
    cy.get("#username").type("manager1");
    cy.get("#password").type("pass111");
    cy.get("button[type='submit']").click();
    
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
