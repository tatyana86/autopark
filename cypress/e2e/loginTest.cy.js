describe("Visit localhost", () => {

  it("Visit the localhost page", () => {
    cy.visit("http://localhost:8080/auth/login");
    cy.get("#username").type("manager1");
    cy.get("#password").type("pass111");
    cy.get("button[type='submit']").click();
  });

});
