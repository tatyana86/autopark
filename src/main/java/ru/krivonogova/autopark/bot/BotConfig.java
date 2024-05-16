package ru.krivonogova.autopark.bot;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import io.github.cdimascio.dotenv.Dotenv;
import lombok.Data;

@Data
@Configuration
@PropertySource("classpath:application.properties")
public class BotConfig {
	
	@Value("${bot.name}")
    private String botName;
    private String token;

    public BotConfig() {
        Dotenv dotenv = Dotenv.load();
        this.token = dotenv.get("TELEGRAM_BOT_TOKEN"); // Получаем значение из .env
    }

}