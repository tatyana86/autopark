package ru.krivonogova.autopark.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;

@Schema(description = "Предприятие с полями для пользователя")
public class VehicleDTO {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private int id;
	
    @NotEmpty(message = "Обязательное поле")
    @Schema(description = "Регистрационный номер", example = "А123ЕС163")
    private String registrationNumber;
    
    @Min(value = 1950, message = "Допускается добавление ТС старше 1950 г.")
    @Max(value = 2024, message = "Кажется, Вы ошиблись :)")
    @Schema(description = "Год производтва", example = "2024")
    private int yearOfProduction;
	
    @Min(value = 0)
    @Schema(description = "Стоимость, руб", example = "500000")
    private double price;

    @Min(value = 0)
    @Schema(description = "Пробег, км", example = "100000")
    private double mileage;
    
    @Schema(description = "Дата продажи, UTC=0", example = "2024/06/30 07:00")
    private String dateOfSale;
    
    @Schema(description = "Бренд")
    private BrandDTO brand;
    
    @Schema(description = "Предприятие")
    private EnterpriseDTO enterprise;
    
    @Schema(description = "Дата продажи в таймзоне менеджера", example = "2024/06/30 08:00")
    private String dateOfSaleForManager;
    
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getRegistrationNumber() {
		return registrationNumber;
	}

	public void setRegistrationNumber(String registrationNumber) {
		this.registrationNumber = registrationNumber;
	}

	public int getYearOfProduction() {
		return yearOfProduction;
	}

	public void setYearOfProduction(int yearOfProduction) {
		this.yearOfProduction = yearOfProduction;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public double getMileage() {
		return mileage;
	}

	public void setMileage(double mileage) {
		this.mileage = mileage;
	}

	public String getDateOfSale() {
		return dateOfSale;
	}

	public void setDateOfSale(String dateOfSale) {
		this.dateOfSale = dateOfSale;
	}
	
	public BrandDTO getBrand() {
		return brand;
	}

	public void setBrand(BrandDTO brand) {
		this.brand = brand;
	}

	public EnterpriseDTO getEnterprise() {
		return enterprise;
	}

	public void setEnterprise(EnterpriseDTO enterprise) {
		this.enterprise = enterprise;
	}

	public String getDateOfSaleForManager() {
		return dateOfSaleForManager;
	}

	public void setDateOfSaleForManager(String dateOfSaleForManager) {
		this.dateOfSaleForManager = dateOfSaleForManager;
	}
    
}
