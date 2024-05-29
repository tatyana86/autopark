package ru.krivonogova.autopark.react;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.krivonogova.autopark.controllers.DatabaseController;
import ru.krivonogova.autopark.dto.DataGenerationDTO;
import ru.krivonogova.autopark.models.Brand;
import ru.krivonogova.autopark.models.Driver;
import ru.krivonogova.autopark.models.Enterprise;
import ru.krivonogova.autopark.models.Vehicle;

@Service
public class ReactDataGenerationService {

	private final DatabaseController databaseController;
	
	@Autowired
	public ReactDataGenerationService(DatabaseController databaseController) {
		this.databaseController = databaseController;
	}
	
	@Transactional
	public void generate(DataGenerationDTO request) {
	    for(Integer enterpriseId : request.getEnterprisesID()) {
	        Enterprise enterprise = databaseController.findOneEnterprise(enterpriseId);
	        
	        List<Vehicle> vehicles = generateVehicles(enterprise, request.getNumberOfVehicle())
	            .collectList()
	            .block();
	        
	        List<Driver> drivers = generateDrivers(enterprise, request.getNumberOfDriver())
	            .collectList()
	            .block();
	        
	        databaseController.saveAllDrivers(drivers);
	        databaseController.saveAllVehicles(vehicles);    
	    }
	}

	private Flux<Vehicle> generateVehicles(Enterprise enterprise, int numberOfVehicles) {
	    return Flux.defer(() -> {
	        List<Brand> brands = databaseController.findAllBrands();
	        return Flux.range(0, numberOfVehicles)
	            .flatMap(i -> newRandomVehicle()
	                .map(vehicle -> {
	                    vehicle.setBrand(brands.get(new Random().nextInt(brands.size())));
	                    vehicle.setEnterprise(enterprise);
	                    return vehicle;
	                }));
	    });
	}
	
	private Flux<Driver> generateDrivers(Enterprise enterprise, int numberOfDrivers) {
	    return Flux.defer(() -> {
	        return Flux.range(0, numberOfDrivers)
	            .flatMap(i -> newRandomDriver()
	                .map(driver -> {
	                    driver.setEnterprise(enterprise);
	                    return driver;
	                }));
	    });
	}
	
	private Mono<Driver> newRandomDriver() {
	    return Mono.defer(() -> {
	        Driver driver = new Driver();
	        return generateName()
	                .flatMap(name -> {
	                    driver.setName(name);
	                    return generateSalary();
	                })
	                .flatMap(salary -> {
	                    driver.setSalary(salary);
	                    return Mono.just(driver);
	                });
	    });
	}
	
	private Mono<String> generateName() {
	    String[] surnames = {"Иванов", "Петров", "Сидоров", "Смирнов", "Кузнецов"};
	    String[] names = {"Иван", "Петр", "Александр", "Дмитрий", "Андрей", "Михаил"};
	    String[] patronymics = {"Иванович", "Петрович", "Александрович", "Дмитриевич", "Андреевич", "Михайлович"};
	    Random random = new Random();

	    String surname = surnames[random.nextInt(surnames.length)];
	    String name = names[random.nextInt(names.length)];
	    String patronymic = patronymics[random.nextInt(patronymics.length)];

	    return Mono.just(String.format("%s %s. %s.", surname, name.charAt(0), patronymic.charAt(0)));
	}

	private Mono<Double> generateSalary() {
	    double maxPrice = 500000;
	    double minPrice = 50000;

	    return Mono.fromSupplier(() -> {
	        Random random = new Random();
	        double salary = random.nextDouble() * (maxPrice - minPrice) + minPrice;
	        return (double) Math.round(salary);
	    });
	}
	
	//+
	private Mono<Vehicle> newRandomVehicle() {
	    return Mono.defer(() -> {
	        Vehicle vehicle = new Vehicle();

	        return generateMileage()
	                .flatMap(mileage -> {
	                    vehicle.setMileage(mileage);
	                    return generatePrice()
	                            .flatMap(price -> {
	                                vehicle.setPrice(price);
	                                vehicle.setRegistrationNumber(generateRegistrationNumber());
	                                vehicle.setYearOfProduction(generateYearOfProduction());
	                                vehicle.setDateOfSale(generateDateOfSale());
	                                return Mono.just(vehicle);
	                            });
	                });
	    });
	}
	
	private Mono<Double> generateMileage() {
	    double maxMileage = 400000;
	    double minMileage = 1;

	    return Mono.fromSupplier(() -> {
	        Random random = new Random();
	        double mileage = random.nextDouble() * (maxMileage - minMileage) + minMileage;
	        return Math.round(mileage);
	    }).map(mileage -> (double) mileage); // Преобразуем значение в double
	}
	
	private Mono<Double> generatePrice() {
	    double maxPrice = 1000000;
	    double minPrice = 100000;

	    return Mono.fromSupplier(() -> {
	        Random random = new Random();
	        double price = random.nextDouble() * (maxPrice - minPrice) + minPrice;
	        return (double) Math.round(price);
	    });
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
	    return String.format("%c%03d%c%c%02d", firstChar, number, secondChar, thirdChar, region);
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
