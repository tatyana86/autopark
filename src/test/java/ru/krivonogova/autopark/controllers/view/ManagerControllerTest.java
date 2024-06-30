package ru.krivonogova.autopark.controllers.view;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest 
@AutoConfigureMockMvc
@WithUserDetails(value = "manager1")
public class ManagerControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
    @Test
    public void testIndexEnterprises() throws Exception {
        this.mockMvc.perform(get("/managers/enterprises"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("enterprises"))
                .andExpect(content().string(containsString("Мои предприятия:")));
    }
	
    @Test
    public void testIndexVehicles() throws Exception {
        this.mockMvc.perform(get("/managers/enterprises/{id}/vehicles", 1))
        		.andDo(print())
        		.andExpect(authenticated())
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("vehicles"))
                .andExpect(content().string(containsString("Текущий автопарк")));
    }
    
    @Test
    public void testCreateVehicleWithValidData() throws Exception {
        this.mockMvc.perform(post("/managers/enterprises/{idEnterprise}/vehicles/new", 1)
                .param("brandId", "1")
                .param("registrationNumber", "О123АБ")
                .param("yearOfProduction", "2020")
                .param("price", "1000000")
                .param("mileage", "10"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/managers/enterprises/1/vehicles"));
    }
    
}
