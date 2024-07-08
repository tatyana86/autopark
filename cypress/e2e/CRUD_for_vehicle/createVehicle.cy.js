describe("Create vehicle", () => {

  it("Create vehicle", () => {
	// авторизация
    cy.visit("http://localhost:8080/auth/login");
    cy.get("#username").type("manager1");
    cy.get("#password").type("pass111");
    cy.get("button[type='submit']").click();
    
    // список машин
	cy.get('a[href="/managers/enterprises/1/vehicles"]').click();
	
	// создаем машинку	
	cy.contains('a.btn.btn-primary', 'Добавить транспортное средство').click();
	cy.get('#registrationNumber').clear().type('А555АА55');
	cy.get('#yearOfProduction').clear().type('2024');
	cy.get('#price').clear().type('20000');
	cy.get('#mileage').clear().type('100000');
   	cy.get('input[type="submit"]').contains('Сохранить ТС').click();
    
    function goToNextPage() {
      cy.get('li.page-item').last().then(($el) => {
        // проверка, если есть кнопка "следующая страница"
        if (!$el.hasClass('disabled')) {
          cy.wrap($el).find('a.page-link').click();
          goToNextPage();
        } else {
	          // проверяем последнюю строку таблицы на наличие созданной машини
	          cy.get('tbody tr').last().within(() => {
	          cy.get('td').eq(1).should('contain', 'А555АА55'); 
	          cy.get('td').eq(2).should('contain', '2024'); 
	          });
        }
      });
    }

    // запуск функции перехода на следующую страницу
    goToNextPage();
    
    
  });

});
