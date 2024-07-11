describe("Create report", () => {

	beforeEach(() => {
		cy.login("manager1", "pass111");
	});

	it("Should create report", () => {

		cy.contains('input', 'Создать отчет').click();
		   
		cy.contains('input', 'Получить отчет').click();
    
	});

});
