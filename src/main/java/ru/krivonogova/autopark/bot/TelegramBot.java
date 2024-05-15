package ru.krivonogova.autopark.bot;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import lombok.extern.slf4j.Slf4j;
import ru.krivonogova.autopark.controllers.DatabaseController;
import ru.krivonogova.autopark.dto.ReportRequestDTO;
import ru.krivonogova.autopark.models.Manager;
import ru.krivonogova.autopark.models.Period;
import ru.krivonogova.autopark.models.Person;
import ru.krivonogova.autopark.models.ReportResult;
import ru.krivonogova.autopark.models.Trip;
import ru.krivonogova.autopark.models.TypeReport;
import ru.krivonogova.autopark.repositories.PeopleRepository;
import ru.krivonogova.autopark.services.ReportsService;

@Component
@Slf4j
public class TelegramBot extends TelegramLongPollingBot {

	static final String DAY_BUTTON = "DAY_BUTTON";
    static final String MONTH_BUTTON = "MONTH_BUTTON";
	
    private boolean isAuthorized = false;
	private Manager manager = new Manager();
	private final PasswordEncoder passwordEncoder;
	
	private final BotConfig botConfig;
	private final PeopleRepository peopleRepository;
	private final DatabaseController databaseController;
	private final ReportsService reportsService;
  
	public TelegramBot(BotConfig botConfig, PeopleRepository peopleRepository, PasswordEncoder passwordEncoder, ReportsService reportsService, DatabaseController databaseController) {
		this.botConfig = botConfig;
	    this.peopleRepository = peopleRepository;
		this.passwordEncoder = passwordEncoder;
		this.databaseController = databaseController;
		this.reportsService = reportsService;
		
		List<BotCommand> listofCommands = new ArrayList<>();
        listofCommands.add(new BotCommand("/login", "authorize manager"));
        listofCommands.add(new BotCommand("/report", "get report"));
        
        try {
        	this.execute(new SetMyCommands(listofCommands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            log.error("Error setting bot's command list: " + e.getMessage());
        }
	}
   
	@Override
	public void onUpdateReceived(Update update) {
    	
		if(update.hasMessage() && update.getMessage().hasText()) {
	        SendMessage.SendMessageBuilder builder = SendMessage.builder();
	        String messageText;
	        String chatId;
	        try {
	            if(update.getMessage() != null) {
	                chatId = update.getMessage().getChatId().toString();
	                builder.chatId(chatId);
	                messageText = update.getMessage().getText();
	            } else {
	                chatId = update.getChannelPost().getChatId().toString();
	                builder.chatId(chatId);
	                messageText = update.getChannelPost().getText();
	            }
	            if(isAuthorized) {
	                startBot(messageText, chatId);
	                return;
	            }
	            
	            // если не авторизован, нужно пройти авторизацию
	            authorizeUser(messageText, builder);
	        } catch (Exception e) {
	        	log.error("Error occurred: " + e.getMessage());
	        }
		} else 
		if(update.hasCallbackQuery()) {
            String callbackData = update.getCallbackQuery().getData();
            long messageId = update.getCallbackQuery().getMessage().getMessageId();
            long chatId = update.getCallbackQuery().getMessage().getChatId();
            
            if(callbackData.equals("PERIOD_BUTTON")) {
	            String text = "Введите тип (День или Месяц), id автомобиля и период поиска в формате:\n"
	            			+ "'type id yyyy-MM-dd yyyy-MM-dd'.\n"
	            			+ "Например, 'Месяц 1 2020-01-01 2022-07-30'";
	           
	            //executeEditMessageText(text, chatId, messageId);
	            
	            SendMessage message = new SendMessage();
	            message.setChatId(String.valueOf(chatId));
	            message.setText(text);
	            executeMessage(message);
            } else 
            if(callbackData.equals("ENTERPRISE_BUTTON")) {
                String text = "Нет технической возможности получить отчет.";
                //executeEditMessageText(text, chatId, messageId);
                
	            SendMessage message = new SendMessage();
	            message.setChatId(String.valueOf(chatId));
	            message.setText(text);
	            executeMessage(message);
            } 
        }
	}
	
    private void executeMessage(SendMessage message){
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Error occurred: " + e.getMessage());
        }
    }
	
    private void executeEditMessageText(String text, long chatId, long messageId){
        EditMessageText message = new EditMessageText();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);
        message.setMessageId((int) messageId);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Error occurred: " + e.getMessage());
        }
    }
    
    /*private void executeEditMessageText(String text, long chatId, long messageId, InlineKeyboardButton button1, InlineKeyboardButton button2){
        EditMessageText message = new EditMessageText();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);
        message.setMessageId((int) messageId);
        
        InlineKeyboardMarkup markupInLine = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine = new ArrayList<>();
        
        rowInLine.add(button1);
        rowInLine.add(button2);
        rowsInLine.add(rowInLine);
        
        markupInLine.setKeyboard(rowsInLine);
        message.setReplyMarkup(markupInLine);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Error occurred: " + e.getMessage());
        }
    }*/
  
	private void startBot(String messageText, String chatId) throws Exception {

		SendMessage message = new SendMessage();
        message.setChatId(chatId);
   
        if(messageText.contains("День") || messageText.contains("Месяц")) {
        	getReport(messageText, chatId);
        }
        
		switch(messageText) {
			case "/login": {
				message.setText("Вы авторизовались под пользователем: " + manager.getUsername());
				executeMessage(message);
		  	}
		  	
			case "/report": {
				message.setText("Какой отчет Вам нужен?");
		  		
		  		InlineKeyboardMarkup markupInLine = new InlineKeyboardMarkup();
		        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
		        List<InlineKeyboardButton> rowInLine = new ArrayList<>();
		        
		        var periodReportButton = new InlineKeyboardButton();
		        periodReportButton.setText("Пробег за период");
		        periodReportButton.setCallbackData("PERIOD_BUTTON");

		        var otherReportButton = new InlineKeyboardButton();
		        otherReportButton.setText("Пробег для предприятия");
		        otherReportButton.setCallbackData("ENTERPRISE_BUTTON");

		        rowInLine.add(periodReportButton);
		        rowInLine.add(otherReportButton);
		        rowsInLine.add(rowInLine);
		        
		        markupInLine.setKeyboard(rowsInLine);
		        message.setReplyMarkup(markupInLine);
		        
		        executeMessage(message);
			}
		}
      
		return;
	}
	
	private void getReport(String messageText, String chatId) {
		SendMessage message = new SendMessage();
        message.setChatId(chatId);
		String[] parts = messageText.split(" ");

	    if (parts.length != 4) {
	    	message.setText("Допущена ошибка, попробуйте заново.");
			executeMessage(message);
	    }

	    String periodType = parts[0];
	    Period period = Period.fromString(periodType);
	    int idVehicle = Integer.parseInt(parts[1]);
	    String dateFrom = parts[2];
	    String dateTo = parts[3];

	    DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	    DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");

	    LocalDate dateFromDate = LocalDate.parse(dateFrom, inputFormatter);
	    LocalDate dateToDate = LocalDate.parse(dateTo, inputFormatter);

	    dateFrom = dateFromDate.atStartOfDay().format(outputFormatter);
	    dateTo = dateToDate.atStartOfDay().plusDays(1).minusSeconds(1).format(outputFormatter);

	    ReportRequestDTO request = new ReportRequestDTO(idVehicle, TypeReport.MILEAGE_BY_PERIOD, period, dateFrom, dateTo);
	    List<Trip> trips = databaseController.findAllTripsByTimePeriod(idVehicle, dateFrom, dateTo);	        
        List<ReportResult> result = reportsService.getReport(request, trips);
        
        Collections.sort(result, Comparator.comparing(ReportResult::getTime));
        StringBuilder responseMessage = new StringBuilder();
        responseMessage.append("Результат отчета за " + periodType + ".\n");
        for(ReportResult report : result) {
            responseMessage.append(report.getTime()).append(" - Пробег: ").append(report.getValue()).append(" км.\n");
        }
        
        message.setText(responseMessage.toString());
        executeMessage(message);
	}
	  
    // авторизировать пользователя
	private void authorizeUser(String messageText, SendMessage.SendMessageBuilder builder) throws Exception {

	    switch(messageText) {
	          case "/login": {
	        	  if(manager.getUsername().isBlank()) {	            	
	                  builder.text("Введите логин:");
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
				            manager.setPassword("");
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