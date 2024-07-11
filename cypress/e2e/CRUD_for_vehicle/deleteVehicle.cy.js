describe("Delete vehicle", () => {

	beforeEach(() => {
		cy.loginAndCreateTestVehicle("manager1", "pass111");
	});

	it("Delete test vehicle", () => {
	    
		// функция для перехода на следующую страницу
		const goToNextPage = () => {
			return cy.get('li.page-item').last().then(($el) => {
				if (!$el.hasClass('disabled')) {
					cy.wrap($el).find('a.page-link').click();
					return goToNextPage();
				} else {
					return cy.wrap($el);
				}
			});
		};

		// Функция для удаления последней строки
		const deleteLastRow = () => {
			cy.get('tbody tr').last().within(() => {
				cy.get('td').eq(1).then(($td1) => {
					cy.get('button[title="Удалить"]').click();
				});
			});
		};

		// Запуск функции перехода на следующую страницу и удаления последней строки
		goToNextPage().then(deleteLastRow);

	});

});
