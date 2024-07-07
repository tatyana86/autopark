describe("Launches a website", () => {
  it("Visit the localhost page", () => {
    cy.visit("http://localhost:8080/auth/login");
  });
});
