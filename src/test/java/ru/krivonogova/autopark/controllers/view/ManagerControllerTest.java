package ru.krivonogova.autopark.controllers.view;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import ru.krivonogova.autopark.controllers.DatabaseController;

@SpringBootTest
@AutoConfigureMockMvc
public class ManagerControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private DatabaseController databaseController;
	
	@Test
	void test() {
		// given
		
		// when 
		
		// than
		assertThat(databaseController).isNotNull();
	}
	
	


}
