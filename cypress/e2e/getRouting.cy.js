describe("Get routing", () => {

  it("Get routing", () => {
    cy.visit("http://localhost:8080/auth/login");
    cy.get("#username").type("manager1");
    cy.get("#password").type("pass111");
    cy.get("button[type='submit']").click();
    
    cy.get('a[href="/managers/enterprises/1/vehicles"]').click();
    
    cy.get('a[href="/managers/enterprises/1/vehicles/1"]').click();
    
    cy.get('input[name="dateFrom"]').type('2024-04-08'); 
    cy.get('input[name="dateTo"]').type('2024-07-08');  
    cy.get('button[type="submit"]').contains('Обновить').click();

    // Нажимаем на кнопку, используя текст внутри кнопки
    cy.contains('button', 'Показать все треки на карте').click();
    
    // Проверяем, что произошел переход на нужную страницу
    cy.url().should('include', '/managers/enterprises/');

  });

});
