package ru.krivonogova.autopark.controllers.rest;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import ru.krivonogova.autopark.controllers.DatabaseController;
import ru.krivonogova.autopark.models.Enterprise;

@Testcontainers
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class RestManagerControllerTest {

	@Autowired
    private MockMvc mockMvc;
	
	@Container
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:12.2-alpine")
            .withDatabaseName("autopark")
            .withUsername("username")
            .withPassword("pass12345");
	 
	@DynamicPropertySource
	static void postgresqlProperties(DynamicPropertyRegistry registry) {
	  registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
	  registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
	  registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
	}
	 
	@Test
	public void contextLoads() {
	}
	  
    @Test
    @WithMockUser(roles = {"MANAGER1"})
    public void indexEnterprises_shouldReturnEnterprises() throws Exception {
        mockMvc.perform(get("/api/managers/1/enterprises")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").isNotEmpty());
    }
    
}
