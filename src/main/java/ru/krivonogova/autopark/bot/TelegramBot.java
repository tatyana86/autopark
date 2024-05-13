package ru.krivonogova.autopark.bot;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import lombok.extern.slf4j.Slf4j;
import ru.krivonogova.autopark.models.Manager;

@Component
@Slf4j
public class TelegramBot extends TelegramLongPollingBot {

	private final BotConfig botConfig;
	private boolean isAuthorized = false;
	private Manager manager = new Manager();
		
	public TelegramBot(BotConfig botConfig) {
		this.botConfig = botConfig;
	}

	@Override
	public void onUpdateReceived(Update update) {
	
		Message message = update.getMessage();
        if (message == null || !message.hasText()) return;
        SendMessage.SendMessageBuilder builder = SendMessage.builder();
        String messageText;
        String chatId;
        try {
            if (update.getMessage() != null) {
                chatId = update.getMessage().getChatId().toString();
                builder.chatId(chatId);
                messageText = update.getMessage().getText();
            } else {
                chatId = update.getChannelPost().getChatId().toString();
                builder.chatId(chatId);
                messageText = update.getChannelPost().getText();
            }
            if (isAuthorized) {
                //authorizedLogic(messageText, builder);
                return;
            }
            
            authorizeUser(messageText, builder);
        } catch (Exception e) {
            //
        }
		
	}
	
	private void authorizeUser(String messageText, SendMessage.SendMessageBuilder builder) {
		switch (messageText) {
	        case "/login": {
	            if (manager.getUsername().isBlank()) {
	                builder.text("Введите логин:");
	                execute(builder.build());
	                return;
	            } else if (manager.getPassword().isBlank()) {
	                builder.text("Введите пароль:");
	                execute(builder.build());
	                return;
	            }
	        }
	        
	        default: {
	        	if (manager.getUsername().isBlank()) {
                    manager = new Manager();
                    manager.setUsername(messageText);
                    builder.text("Введите пароль:");
                    execute(builder.build());
                } else if (manager.getPassword().isBlank()) {
                    manager.setPassword(messageText);
                    if (validateCredentials()) {
                        isAuthorized = true;
                        builder.text("Вы успешно авторизированы");
                        execute(builder.build());
                    } else {
                        isAuthorized = false;
                        manager = new Manager();
                        builder.text("Неудачная авторизация. Повторите пароль:");
                        execute(builder.build());
                    }
                }
	        }
	}

	@Override
	public String getBotUsername() {
		return botConfig.getBotName();
	}

	@Override
	public String getBotToken() {
		
		return botConfig.getToken();
	}

}
