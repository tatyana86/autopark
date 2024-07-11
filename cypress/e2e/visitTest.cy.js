describe("Launches a website", () => {
	
  it("Should visit the localhost page", () => {
    cy.visit("http://localhost:8080/auth/login");
  });
  
});
