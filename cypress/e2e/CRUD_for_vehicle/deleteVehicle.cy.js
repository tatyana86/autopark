describe("Delete vehicle", () => {

  it("Get vehicles", () => {
	// авторизация
    cy.visit("http://localhost:8080/auth/login");
    cy.get("#username").type("manager1");
    cy.get("#password").type("pass111");
    cy.get("button[type='submit']").click();
    
    // список машин
    cy.get('a[href="/managers/enterprises/1/vehicles"]').click();
    
    function goToNextPage() {
      cy.get('li.page-item').last().then(($el) => {
        // проверка, если есть кнопка "следующая страница"
        if (!$el.hasClass('disabled')) {
          cy.wrap($el).find('a.page-link').click();
          goToNextPage();
        } else {
			
	      // проверяем последнюю строку таблицы на наличие созданной машины и удаляем её
          cy.get('tbody tr').last().within(() => {
            cy.get('td').eq(1).then(($td1) => {
              if ($td1.text().includes('А555АА55')) {
                cy.get('td').eq(2).then(($td2) => {
                  if ($td2.text().includes('2024')) {
                    // нажимаем кнопку "Удалить" только если оба условия верны
                    cy.get('button[title="Удалить"]').click();
                  }
                });
              }
            });
          });

        }
      });
    }

    // запуск функции перехода на следующую страницу
    goToNextPage();

  });

});
