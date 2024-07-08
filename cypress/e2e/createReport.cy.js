describe("Create report", () => {

  it("Create report", () => {
	// авторизация
    cy.visit("http://localhost:8080/auth/login");
    cy.get("#username").type("manager1");
    cy.get("#password").type("pass111");
    cy.get("button[type='submit']").click();
    
   cy.contains('input', 'Создать отчет').click();
   
   cy.contains('input', 'Получить отчет').click();
    
  });

});
