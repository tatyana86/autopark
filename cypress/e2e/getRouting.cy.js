describe("Get routing", () => {

  it("Get routing", () => {
	// авторизация
    cy.visit("http://localhost:8080/auth/login");
    cy.get("#username").type("manager1");
    cy.get("#password").type("pass111");
    cy.get("button[type='submit']").click();
    
    // список машин
    cy.get('a[href="/managers/enterprises/1/vehicles"]').click();
    cy.get('a[href="/managers/enterprises/1/vehicles/1"]').click();
    
    // заполняем параметры для роутинга
    cy.get('input[name="dateFrom"]').type('2024-04-08'); 
    cy.get('input[name="dateTo"]').type('2024-07-08');  
    cy.get('button[type="submit"]').contains('Обновить').click();

    cy.contains('button', 'Показать все треки на карте').click();
    
    cy.url().should('include', '/managers/enterprises/');

  });

});
