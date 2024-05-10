package ru.krivonogova.autopark.services;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.krivonogova.autopark.controllers.DatabaseController;
import ru.krivonogova.autopark.dto.DataGenerationDTO;
import ru.krivonogova.autopark.models.Brand;
import ru.krivonogova.autopark.models.Driver;
import ru.krivonogova.autopark.models.Enterprise;
import ru.krivonogova.autopark.models.Vehicle;

@Service
@Transactional(readOnly = true)
public class DataGenerationService {

	private final DatabaseController databaseController;
	
	@Autowired
	public DataGenerationService(DatabaseController databaseController) {
		this.databaseController = databaseController;
	}

	@Transactional
	public void generate(DataGenerationDTO request) {
		for(Integer enterpriseId : request.getEnterprisesID()) {
			Enterprise enterprise = databaseController.findOneEnterprise(enterpriseId);
			
			List<Vehicle> vehicles = generateVehicles(enterprise, request.getNumberOfVehicle());
			
			List<Driver> drivers = generateDrivers(enterprise, request.getNumberOfDriver());
					
			assignVehicleWithDriver(vehicles, drivers, request.getIndicatorOfActiveVehicle());
			
			databaseController.saveAllDrivers(drivers);
			databaseController.saveAllVehicles(vehicles);		
		}
	}
	
	private void assignVehicleWithDriver(List<Vehicle> vehicles, 
				List<Driver> drivers, int indicatorOfActiveVehicle) {
		int numberActiveVehicles = vehicles.size() / indicatorOfActiveVehicle;
		for(int i = 0; i < numberActiveVehicles; i++) {
			Driver driver = findDisactiveDriver(drivers);
			if(driver != null) {
				Vehicle vehicle = vehicles.get(i * indicatorOfActiveVehicle);
				vehicle.setActiveDriver(driver);
				driver.setActive(true);
				driver.setActiveVehicle(vehicle);
			}
		}
	}
	
	private Driver findDisactiveDriver(List<Driver> drivers) {
		for(Driver driver : drivers) {
			if(! driver.isActive()) {
				return driver;
			}
		}
		return null; // или создать исключение (?)
	}

	private List<Vehicle> generateVehicles(Enterprise enterprise, int numberOfVehicle) {
		List<Brand> brands = databaseController.findAllBrands();
		List<Vehicle> vehicles = new ArrayList<>();
		
		for(int i = 0; i < numberOfVehicle; i ++) {
			Vehicle newRandomVehicle = newRandomVehicle();
			newRandomVehicle.setBrand(brands.get(new Random().nextInt(brands.size())));
			newRandomVehicle.setEnterprise(enterprise);
			vehicles.add(newRandomVehicle);
		}
		return vehicles;
	}
	
	private List<Driver> generateDrivers(Enterprise enterprise, int numberOfDriver) {
		List<Driver> drivers = new ArrayList<>();
		
		for(int i = 0; i < numberOfDriver; i ++) {
			Driver newRandomDriver = newRandomDriver();
			newRandomDriver.setEnterprise(enterprise);
			drivers.add(newRandomDriver);
		}
		return drivers;
	}

	private Vehicle newRandomVehicle() {
		Vehicle vehicle = new Vehicle();
		vehicle.setMileage(generateMileage());
		vehicle.setPrice(generatePrice());
		vehicle.setRegistrationNumber(generateRegistrationNumber());
		vehicle.setYearOfProduction(generateYearOfProduction());
		vehicle.setDateOfSale(generateDateOfSale());
		return vehicle;
	}
	
	private Driver newRandomDriver() {
		Driver driver = new Driver();
		driver.setName(generateName());
		driver.setSalary(generateSalary());
		return driver;
	}
	
	private String generateName() {
		String[] surnames = {"Иванов", "Петров", "Сидоров", "Смирнов", "Кузнецов"};
	    String[] names = {"Иван", "Петр", "Александр", "Дмитрий", "Андрей", "Михаил"};
	    String[] patronymics = {"Иванович", "Петрович", "Александрович", "Дмитриевич", "Андреевич", "Михайлович"};
	    Random random = new Random();
	    
	    String surname = surnames[random.nextInt(surnames.length)];
        String name = names[random.nextInt(names.length)];
        String patronymic = patronymics[random.nextInt(patronymics.length)];
	    
	    return String.format("%s %s. %s.", surname, name.charAt(0), patronymic.charAt(0));
	}
	
	private double generateSalary() {
		double maxPrice = 500000;
		double minPrice = 50000;
		
		Random random = new Random();
		double salary = random.nextDouble(maxPrice - minPrice + 1) + minPrice;
		
		return Math.round(salary);
	}
	
	private double generateMileage() {
		double maxMileage = 400000;
		double minMileage = 1;
		
		Random random = new Random();
		double mileage = random.nextDouble(maxMileage - minMileage + 1) + minMileage;
		
		return Math.round(mileage);
	}
	
	private double generatePrice() {
		double maxPrice = 1000000;
		double minPrice = 100000;
		
		Random random = new Random();
		double price = random.nextDouble(maxPrice - minPrice + 1) + minPrice;
		
		return Math.round(price);
	}
	
	private String generateRegistrationNumber() {
		final String LETTERS = "АВЕКМНОРСТУХ";
	    final int MAX_REGION = 190;
	    
	    Random random = new Random();
	    
	    char firstChar = LETTERS.charAt(random.nextInt(LETTERS.length()));
        char secondChar = LETTERS.charAt(random.nextInt(LETTERS.length()));
        char thirdChar = LETTERS.charAt(random.nextInt(LETTERS.length()));
        int number = random.nextInt(1000);
        int region = random.nextInt(MAX_REGION) + 1;
        return String.format("%c%s%c%c%02d", firstChar, number, secondChar, thirdChar, region);
	}
	
	private int generateYearOfProduction() {
		int minYear = 1950;
		int maxYear = 2024;
		
		Random random = new Random();
		
		return minYear + random.nextInt(maxYear - minYear + 1);
	}
	
	private String generateDateOfSale() {
		// Генерация случайной даты за последние 20 лет
        long twentyYearsInMillis = 20L * 365 * 24 * 60 * 60 * 1000; // количество миллисекунд в 20 годах
        long currentTimeMillis = System.currentTimeMillis(); // текущее время в миллисекундах
        long randomTimeMillis = currentTimeMillis - (long) (Math.random() * twentyYearsInMillis); // случайное время за последние 20 лет
        Date randomDate = new Date(randomTimeMillis);

        // Форматирование даты в строку
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        return dateFormat.format(randomDate);
	}
 }
