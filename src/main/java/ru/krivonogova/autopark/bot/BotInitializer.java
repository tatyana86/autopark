package ru.krivonogova.autopark.bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class BotInitializer {

  @Autowired
  TelegramBot telegramBot;
  
    @EventListener({ContextRefreshedEvent.class})
    public void init() throws TelegramApiException {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        
        try {
            telegramBotsApi.registerBot(telegramBot);
        } catch (TelegramApiRequestException e) {
            log.error("Error occurred: " + e.getMessage());
        }
        
    }
  
}