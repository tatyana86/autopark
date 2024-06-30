package ru.krivonogova.autopark.config;

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

@SpringBootTest
@AutoConfigureMockMvc
public class SecurityConfigTest {

	@Autowired
	private MockMvc mockMvc;
	
	@Test
	void shouldReturnDefaultMessage() throws Exception {
		this.mockMvc.perform(get("/auth/login"))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(content().string(containsString("Введите логин:")));
	}
	
    @Test
    public void accessDeniedTest() throws Exception {
        this.mockMvc.perform(get("/managers/enterprises"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/auth/login"));
    }
       
    @Test
    public void correctLoginTest() throws Exception {
        this.mockMvc.perform(post("/process_login")
                .param("username", "manager1")
                .param("password", "pass111"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/managers/enterprises"));
    }
    
    @Test
    public void badCredentials() throws Exception {
        this.mockMvc.perform(post("/process_login")
                .param("username", "manager1")
                .param("password", "pass11"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/auth/login?error"));
    }
    
    @Test
    public void emptyPassword() throws Exception {
        this.mockMvc.perform(post("/process_login")
                .param("username", "manager1"))
        		.andExpect(status().is3xxRedirection())
        		.andExpect(redirectedUrl("/auth/login?error"));
    }
    
}
