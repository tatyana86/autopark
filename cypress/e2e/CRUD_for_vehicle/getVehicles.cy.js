describe("Get vehicles", () => {

  it("Get vehicles", () => {
	// авторизация
    cy.visit("http://localhost:8080/auth/login");
    cy.get("#username").type("manager1");
    cy.get("#password").type("pass111");
    cy.get("button[type='submit']").click();
    
    cy.get('a[href="/managers/enterprises/1/vehicles"]').click();
    
    cy.get('a[href="/managers/enterprises"]').click();
  });

});
