package ru.krivonogova.autopark.services;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
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
import ru.krivonogova.autopark.models.ReactDriver;
import ru.krivonogova.autopark.models.ReactVehicle;
import ru.krivonogova.autopark.models.Vehicle;

@Service
@Transactional(readOnly = true)
public class ReactDataGenerationService {

	private final DatabaseController databaseController;
	
	@Autowired
	public ReactDataGenerationService(DatabaseController databaseController) {
		this.databaseController = databaseController;
	}
	
	@Transactional
	public void generate(DataGenerationDTO request) {
	    for (Integer enterpriseId : request.getEnterprisesID()) {
	        Enterprise enterprise = databaseController.findOneEnterprise(enterpriseId);

	        List<ReactVehicle> vehicles = generateVehicles(enterprise, request.getNumberOfVehicle()).collectList().block();
	        List<ReactDriver> drivers = generateDrivers(enterprise, request.getNumberOfDriver()).collectList().block();

	        assignVehicleWithDriver(vehicles, drivers, request.getIndicatorOfActiveVehicle());

	        List<ReactDriver> convertedDrivers = drivers.stream()
	                .map(driver -> {
	                    ReactDriver convertedDriver = new ReactDriver();
	                    convertedDriver.setActive(driver.isActive());
	                    convertedDriver.setEnterprise(driver.getEnterprise());
	                    convertedDriver.setActiveVehicle(driver.getActiveVehicle());
	                    return convertedDriver;
	                })
	                .collect(Collectors.toList());

	        List<ReactVehicle> convertedVehicles = vehicles.stream()
	                .map(vehicle -> {
	                    ReactVehicle convertedVehicle = new ReactVehicle();
	                    convertedVehicle.setBrand(vehicle.getBrand());
	                    convertedVehicle.setEnterprise(vehicle.getEnterprise());
	                    convertedVehicle.setActiveDriver(vehicle.getActiveDriver());
	                    return convertedVehicle;
	                })
	                .collect(Collectors.toList());

	        //databaseController.saveAllDrivers(convertedDrivers);
	        //databaseController.saveAllVehicles(convertedVehicles);
	    }
	}
	
	private void assignVehicleWithDriver(List<ReactVehicle> vehicles, 
            List<ReactDriver> drivers, int indicatorOfActiveVehicle) {
		
		int numberActiveVehicles = vehicles.size() / indicatorOfActiveVehicle;
		
		Flux.range(0, numberActiveVehicles)
			.flatMap(i -> {
				ReactDriver driver = findDisactiveDriver(drivers);
				if (driver != null) {
					ReactVehicle vehicle = vehicles.get(i * indicatorOfActiveVehicle);
					vehicle.setActiveDriver(driver);
					driver.setActive(true);
					driver.setActiveVehicle(vehicle);
					return Flux.just(vehicle); 
				}				
				return Flux.empty(); 
			})
			.subscribe();
		
	}
	
	private Flux<ReactVehicle> generateVehicles(Enterprise enterprise, int numberOfVehicle) {
	    return Flux.range(0, numberOfVehicle)
	            .flatMap(i -> newRandomVehicle()
	                    .flatMap(vehicle -> {
	                        List<Brand> brands = databaseController.findAllBrands();
	                        vehicle.setBrand(brands.get(new Random().nextInt(brands.size())));
	                        vehicle.setEnterprise(enterprise);
	                        return Mono.just(vehicle);
	                    }));
	}
	
	private Flux<ReactDriver> generateDrivers(Enterprise enterprise, int numberOfDriver) {
	    return Flux.range(0, numberOfDriver)
	            .flatMap(i -> newRandomDriver()
	                    .flatMap(driver -> {
	                        driver.setEnterprise(enterprise);
	                        return Mono.just(driver);
	                    }));
	}
	
	private ReactDriver findDisactiveDriver(List<ReactDriver> drivers) {
		for(ReactDriver driver : drivers) {
			if(! driver.isActive()) {
				return driver;
			}
		}
		return null;
	}
		
	private Mono<ReactDriver> newRandomDriver() {
	    return Mono.fromCallable(() -> {
	        ReactDriver driver = new ReactDriver();
	        driver.setName(generateName());
	        driver.setSalary(generateSalary());
	        return driver;
	    });
	}
	
	private Mono<String> generateName() {
	    String[] surnames = {"Иванов", "Петров", "Сидоров", "Смирнов", "Кузнецов"};
	    String[] names = {"Иван", "Петр", "Александр", "Дмитрий", "Андрей", "Михаил"};
	    String[] patronymics = {"Иванович", "Петрович", "Александрович", "Дмитриевич", "Андреевич", "Михайлович"};
	    Random random = new Random();
	    
	    return Mono.just(String.format("%s %s. %s.", surnames[random.nextInt(surnames.length)], names[random.nextInt(names.length)].charAt(0), patronymics[random.nextInt(patronymics.length)].charAt(0)));
	}

	private Mono<Long> generateSalary() {
	    double maxPrice = 500000;
	    double minPrice = 50000;
	    
	    Random random = new Random();
	    double salary = random.nextDouble(maxPrice - minPrice) + minPrice;
	    
	    return Mono.just(Math.round(salary));
	}
	
	private Mono<ReactVehicle> newRandomVehicle() {
	    return Mono.fromCallable(() -> {
	        ReactVehicle vehicle = new ReactVehicle();
	        vehicle.setMileage(generateMileage());
	        vehicle.setPrice(generatePrice());
	        vehicle.setRegistrationNumber(generateRegistrationNumber());
	        vehicle.setYearOfProduction(generateYearOfProduction());
	        vehicle.setDateOfSale(generateDateOfSale());
	        return vehicle;
	    });
	}
	
	private Mono<Long> generateMileage() {
	    double maxMileage = 400000;
	    double minMileage = 1;
	    return Mono.fromCallable(() -> {
	        Random random = new Random();
	        double mileage = random.nextDouble() * (maxMileage - minMileage) + minMileage;
	        return Math.round(mileage);
	    });
	}

	private Mono<Long> generatePrice() {
	    double maxPrice = 1000000;
	    double minPrice = 100000;
	    return Mono.fromCallable(() -> {
	        Random random = new Random();
	        double price = random.nextDouble() * (maxPrice - minPrice) + minPrice;
	        return Math.round(price);
	    });
	}

	private Mono<String> generateRegistrationNumber() {
	    final String LETTERS = "АВЕКМНОРСТУХ";
	    final int MAX_REGION = 190;
	    
	    return Mono.fromCallable(() -> {
	        Random random = new Random();
	        char firstChar = LETTERS.charAt(random.nextInt(LETTERS.length()));
	        char secondChar = LETTERS.charAt(random.nextInt(LETTERS.length()));
	        char thirdChar = LETTERS.charAt(random.nextInt(LETTERS.length()));
	        int number = random.nextInt(1000);
	        int region = random.nextInt(MAX_REGION) + 1;
	        return String.format("%c%d%c%c%02d", firstChar, number, secondChar, thirdChar, region);
	    });
	}

	private Mono<Integer> generateYearOfProduction() {
	    int minYear = 1950;
	    int maxYear = 2024;
	    return Mono.fromCallable(() -> {
	        Random random = new Random();
	        return minYear + random.nextInt(maxYear - minYear + 1);
	    });
	}

	private Mono<String> generateDateOfSale() {
	    long twentyYearsInMillis = 20L * 365 * 24 * 60 * 60 * 1000; // количество миллисекунд в 20 годах
	    long currentTimeMillis = System.currentTimeMillis(); // текущее время в миллисекундах
	    
	    return Mono.fromCallable(() -> {
	        Random random = new Random();
	        long randomTimeMillis = currentTimeMillis - (long) (random.nextDouble() * twentyYearsInMillis);
	        Date randomDate = new Date(randomTimeMillis);
	        
	        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
	        return dateFormat.format(randomDate);
	    });
	}
	
}
