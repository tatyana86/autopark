package ru.krivonogova.autopark.controllers.view;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import ru.krivonogova.autopark.controllers.DatabaseController;
import ru.krivonogova.autopark.dao.NplusOne;
import ru.krivonogova.autopark.dao.Transaction;
import ru.krivonogova.autopark.dto.VehicleDTO;
import ru.krivonogova.autopark.models.Brand;
import ru.krivonogova.autopark.models.Enterprise;
import ru.krivonogova.autopark.models.TypeVehicle;
import ru.krivonogova.autopark.models.Vehicle;
import ru.krivonogova.autopark.security.PersonDetails;

@Hidden
@Tag(name = "Секретный контроллер", 
	description = "Контроллер для проверки работы CRUD-репозиториев "
			+ "без авторизации пользователей")
@Slf4j
@RestController
@RequestMapping
public class PrimitiveController {

	private final DatabaseController databaseController;
	private final ModelMapper modelMapper;
	private final Transaction transaction;
	private final NplusOne nplusOne;

	@Autowired
	public PrimitiveController(DatabaseController databaseController, ModelMapper modelMapper, Transaction transaction, NplusOne nplusOne) {
		this.modelMapper = modelMapper;
		this.databaseController = databaseController;
		this.transaction = transaction;
		this.nplusOne = nplusOne;
	}
	
	@GetMapping("/transaction")
	public ModelAndView goToInsideTransaction(Model model) {
		transaction.goInside();

		List<Enterprise> sortedEnterprises = databaseController.findAllEnterprises();
		sortedEnterprises.sort(Comparator.comparing(Enterprise::getId));
		ModelAndView enterprises = new ModelAndView("enterprises/index_preview");
		enterprises.addObject("enterprises", sortedEnterprises);
		return enterprises;
	}
	
	@GetMapping("/n+1")
	public ResponseEntity<String> demonstrate() {

		nplusOne.demonstrate();

		return ResponseEntity.ok("hi, n+1");
	}
	
	// CRUD для брендов
	
	@GetMapping("/brands")
	public String indexBrands(Model model) {
		model.addAttribute("brands", databaseController.findAllBrands());
        return "brands/index";
	}
	
	@GetMapping("/brands/{id}")
	public String showBrand(@PathVariable("id") int id, Model model, 
						@ModelAttribute("vehicle") Brand brand) {
		model.addAttribute("brand", databaseController.findOneBrand(id));
		
		return "brands/show";
	}	
	
	@GetMapping("/brands/new")
	public String newBrand(@ModelAttribute("brand") Brand brand,
							Model model) {	
				
		TypeVehicle[] types = TypeVehicle.values();
		model.addAttribute("types", types);
		
		return "brands/new";
	}
	
    @PostMapping("/brands")
	public String createBrand(@ModelAttribute("brand") @Valid Brand brand,
						BindingResult bindingResult) {
		if(bindingResult.hasErrors()) {
			return "brands/new";
		}
		
		databaseController.saveBrand(brand);
		return "redirect:/brands";
	}
	
    @GetMapping("/brands/{id}/edit")
    public String editBrand(Model model, @PathVariable("id") int id) {
    	
    	TypeVehicle[] types = TypeVehicle.values();
		model.addAttribute("types", types);
    	
    	model.addAttribute("brand", databaseController.findOneBrand(id));
    	
    	return "brands/edit";
    }
	
    @PatchMapping("/brands/{id}")
    public String updateBrand(@ModelAttribute("brand") @Valid Brand brand,
    					BindingResult bindingResult,
    					@PathVariable("id") int id) {
		if(bindingResult.hasErrors()) {
			return "brands/edit";
		}
		
		databaseController.updateBrand(id, brand);
		return "redirect:/brands";
    }
    
    @DeleteMapping("/brands/{id}")
    public String deleteBrand(@PathVariable("id") int id) {
    	databaseController.deleteBrand(id);
    	return "redirect:/brands";
    }
    
    // CRUD для транспорта
    
	@GetMapping("/vehicles")
	public String indexVehicles(@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
						@RequestParam(value = "itemsPerPage", required = false, defaultValue = "10") Integer itemsPerPage,
						Model model) {
		
		Integer idPerson = getPersonId();
		String timezone = getPersonTimezone();
		
		Page<Vehicle> vehiclesPage = databaseController.findAllVehicles(page, itemsPerPage);
		
		model.addAttribute("vehicles", vehiclesPage.getContent().stream().map(vehicle -> convertToVehicleDTO(vehicle, timezone)).collect(Collectors.toList()));
	    model.addAttribute("currentPage", vehiclesPage.getNumber() + 1);
	    model.addAttribute("totalPages", vehiclesPage.getTotalPages());
	    model.addAttribute("hasNext", vehiclesPage.hasNext());
	    model.addAttribute("hasPrevious", vehiclesPage.hasPrevious());

        return "vehicles/index_preview";
	}
	
	@GetMapping("/vehicles/{id}")
	public String showVehicle(@PathVariable("id") int id, Model model, 
						@ModelAttribute("vehicle") Vehicle vehicle) {
		model.addAttribute("vehicle", databaseController.findOneVehicle(id));
		
		return "vehicles/show_old";
	}
	
	@GetMapping("/vehicles/new")
	public String newVehicle(@ModelAttribute("vehicle") Vehicle vehicle,
							Model model) {
		
		model.addAttribute("brands", databaseController.findAllBrands());
		
		return "vehicles/new";
	}
	
	@PostMapping("/vehicles")
	public String createVehicle(@RequestParam("brandId") int brandId,
						Model model,
						@ModelAttribute("vehicle") @Valid Vehicle vehicle,
						BindingResult bindingResult) {
				
    	if(bindingResult.hasErrors()) {
    		model.addAttribute("brands", databaseController.findAllBrands());
    		return "vehicles/new";
    	}
		    	    	
		databaseController.saveVehicle(vehicle, brandId);
		
		return "redirect:/vehicles";
	}
	
	@GetMapping("/vehicles/{id}/edit")
	public String editVehicle(Model model, @PathVariable("id") int id) {
		model.addAttribute("vehicle", databaseController.findOneVehicle(id));
		model.addAttribute("brands", databaseController.findAllBrands());
		
		return "vehicles/edit";
	}
	
	@PatchMapping("/vehicles/{id}")
	public String update(@RequestParam("brandId") int brandId,
						Model model,
						@ModelAttribute("vehicle") @Valid Vehicle vehicle,
						BindingResult bindingResult,
						@PathVariable("id") int id) {
		
		if(bindingResult.hasErrors()) {
			model.addAttribute("brands", databaseController.findAllBrands());
			return "vehicles/edit";
		}
		
		databaseController.updateVehicle(id, vehicle, brandId);
		
		return "redirect:/vehicles";
	}
	
	@DeleteMapping("/vehicles/{id}")
	public String deleteVehicle(@PathVariable("id") int id) {
		databaseController.deleteVehicle(id);
		
		return "redirect:/vehicles";
	}
	
	// вспомогательное
	
	private Integer getPersonId() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
		Integer idPerson = personDetails.getPerson().getId();
		
		return idPerson;
	}
	
	private String getPersonTimezone() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
	    String timezone = personDetails.getPerson().getTimezone();   
	    
	    return timezone;
	}
	
	private VehicleDTO convertToVehicleDTO(Vehicle vehicle, String timezone) {
	    
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
	    LocalDateTime dateOfSale_UTC = LocalDateTime.parse(vehicle.getDateOfSale(), formatter);
	    ZoneOffset timeZone = ZoneOffset.of(timezone);
	    LocalDateTime dateOfSale = dateOfSale_UTC.atZone(ZoneOffset.UTC).withZoneSameInstant(timeZone).toLocalDateTime();
	    String dateOfSaleForManager = dateOfSale.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm"));

	    VehicleDTO vehicleDTO = modelMapper.map(vehicle, VehicleDTO.class);
	    vehicleDTO.setDateOfSaleForManager(dateOfSaleForManager);
	    
	    // System.out.println(vehicleDTO.getDateOfSale());
	    // System.out.println(vehicleDTO.getDateOfSaleForManager());
	    
	    return vehicleDTO;
	}
}
