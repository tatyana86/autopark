package ru.krivonogova.autopark.bot;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import lombok.Data;

@Data
@Configuration
@PropertySource("classpath:application.properties")
public class BotConfig {
	
    @Value("${bot.name}")
    String botName;

    @Value("${bot.token}")
    String token;

}