package ru.krivonogova.autopark.bot;

import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import ru.krivonogova.autopark.models.Manager;
import ru.krivonogova.autopark.models.Person;
import ru.krivonogova.autopark.repositories.PeopleRepository;

@Component
public class TelegramBot extends TelegramLongPollingBot {

  private final BotConfig botConfig;
  private final PeopleRepository peopleRepository;
  
  public TelegramBot(BotConfig botConfig, PeopleRepository peopleRepository, PasswordEncoder passwordEncoder) {
	    this.botConfig = botConfig;
	    this.peopleRepository = peopleRepository;
		this.passwordEncoder = passwordEncoder;
  }
   
  private boolean isAuthorized = false;
  private Manager manager = new Manager();
  private final PasswordEncoder passwordEncoder;

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
            
            // если не авторизован, нужно пройти авторизацию
            authorizeUser(messageText, builder);
        } catch (Exception e) {
            //
        }
    
  }
  
  private void authorizeUser(String messageText, SendMessage.SendMessageBuilder builder) throws Exception {

	    switch (messageText) {
	          case "/login": {
	              if(manager.getUsername().isBlank()) {	            	
	                  builder.text("Введите логин:");
	                  execute(builder.build());
	                  return;
	              } 
	              
	              if(manager.getPassword().isBlank()) {
	            	  //System.out.println("4");
	                  builder.text("Введите пароль здесь:");
	                  execute(builder.build());
	                  return;
	              }
	          }
	          
	          default: {
	            if(manager.getUsername().isBlank()) {
	                    manager = new Manager();
	                    manager.setUsername(messageText);
	                    builder.text("Введите пароль тут:  ");
	                    execute(builder.build());
	            } 
	            else if(manager.getPassword().isBlank()) {
	                    manager.setPassword(messageText);
	                    
	                    if(isCredentialsTrue()) {
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
    
  }
    
    private boolean isCredentialsTrue() {
      Optional<Person> person = peopleRepository.findByUsername(manager.getUsername());   
      if(person.isEmpty()) {
    	  return false;
      }
    		  
      if(passwordEncoder.matches(manager.getPassword(), person.get().getPassword())) {
    	  System.out.println("22");
        return true;
      }

      return false;
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