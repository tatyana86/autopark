package ru.krivonogova.autopark.controllers;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.TimeZone;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import jakarta.validation.Valid;
import ru.krivonogova.autopark.dto.VehicleDTO;
import ru.krivonogova.autopark.models.Enterprise;
import ru.krivonogova.autopark.models.Vehicle;
import ru.krivonogova.autopark.security.PersonDetails;
import ru.krivonogova.autopark.services.BrandsService;
import ru.krivonogova.autopark.services.EnterprisesService;
import ru.krivonogova.autopark.services.ManagersService;
import ru.krivonogova.autopark.services.VehiclesService;

@Controller
@RequestMapping("/managers")
public class ManagersController {

	private final EnterprisesService enterprisesService;
	private final ManagersService managersService;
	private final VehiclesService vehiclesService;
	private final BrandsService brandsService;
	private final ModelMapper modelMapper;
	
	@Autowired	
	public ManagersController(EnterprisesService enterprisesService, ManagersService managersService, VehiclesService vehiclesService, ModelMapper modelMapper, BrandsService brandsService) {
		this.enterprisesService = enterprisesService;
		this.managersService = managersService;
		this.vehiclesService = vehiclesService;
		this.brandsService = brandsService;
		this.modelMapper = modelMapper;
	}

	@GetMapping("/enterprises")
	public ModelAndView indexEnterprisesWOid() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
		String username = personDetails.getPerson().getUsername();
		
		Integer id = managersService.findByUsername(username).getId();
		
		ModelAndView enterprises = new ModelAndView("enterprises/index");
		
		enterprises.addObject("enterprises", enterprisesService.findAllForManager(id));
		
		return enterprises;
	}
	
	@GetMapping("/enterprises/{idEnterprise}/edit")
	public String edit(@PathVariable("idEnterprise") int idEnterprise,
						Model model) {
		
		model.addAttribute("enterprise", enterprisesService.findOne(idEnterprise));
		
		return "enterprises/edit";
	}
	
	@PutMapping("/enterprises/{idEnterprise}")
	public String update(@PathVariable("idEnterprise") int idEnterprise,
						Model model,
						@ModelAttribute("enterprise") @Valid Enterprise enterprise,
						BindingResult bindingResult) {
		
		if(bindingResult.hasErrors()) {
			model.addAttribute("errorMsg", "Введены некорректные данные. Попробуйте еще!");
			model.addAttribute("enterprise", enterprisesService.findOne(idEnterprise));
			return "enterprises/edit";
		}
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
		Integer idManager = personDetails.getPerson().getId();
		
		enterprisesService.update(idManager, idEnterprise, enterprise);
		
		return "redirect:/managers/enterprises";
	}
	
	@GetMapping("/enterprises/{idEnterprise}/vehicles")
	public String indexVehicles(@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
								@RequestParam(value = "itemsPerPage", required = false, defaultValue = "10") Integer itemsPerPage,
								@PathVariable("idEnterprise") int idEnterprise,
								Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
		String username = personDetails.getPerson().getUsername();
		
	    String timezone = personDetails.getPerson().getTimezone();   
		
		Integer idManager = managersService.findByUsername(username).getId();
				
		Page<Vehicle> vehiclesPage = vehiclesService.findForManagerByEnterpriseId(idManager, idEnterprise, page, itemsPerPage);
	    model.addAttribute("vehicles", vehiclesPage.getContent().stream().map(vehicle -> convertToVehicleDTO(vehicle, timezone)).collect(Collectors.toList()));
	    model.addAttribute("currentPage", vehiclesPage.getNumber() + 1);
	    model.addAttribute("totalPages", vehiclesPage.getTotalPages());
	    model.addAttribute("hasNext", vehiclesPage.hasNext());
	    model.addAttribute("hasPrevious", vehiclesPage.hasPrevious());
	    model.addAttribute("idEnterprise", idEnterprise);
	   
        return "vehicles/index";
	}
	
	@GetMapping("/enterprises/{idEnterprise}/vehicles/{idVehicle}")
	public String show(@PathVariable("idEnterprise") int idEnterprise,
						@PathVariable("idVehicle") int idVehicle,
						Model model, 
						@ModelAttribute("vehicle") Vehicle vehicle) {
		
		model.addAttribute("vehicle", vehiclesService.findOne(idVehicle));
		model.addAttribute("enterprise", enterprisesService.findOne(idEnterprise));
		
		return "vehicles/show";
	}
	
	
	@GetMapping("/enterprises/{idEnterprise}/vehicles/new")
	public String newVehicle(@ModelAttribute("vehicle") VehicleDTO vehicle,
							@PathVariable("idEnterprise") int idEnterprise,
							Model model) {
		
		model.addAttribute("brands", brandsService.findAll());
		//model.addAttribute("idEnterprise", idEnterprise);
		model.addAttribute("enterprise", enterprisesService.findOne(idEnterprise));
			
		return "vehicles/new";
	}
	
	@PostMapping("/enterprises/{idEnterprise}/vehicles/new")
	public String create(@RequestParam("brandId") int brandId,
						@PathVariable("idEnterprise") int idEnterprise,
						Model model,
						@ModelAttribute("vehicle") @Valid VehicleDTO vehicle,
						BindingResult bindingResult) {
				
    	if(bindingResult.hasErrors()) {
    		model.addAttribute("brands", brandsService.findAll());
    		model.addAttribute("enterprise", enterprisesService.findOne(idEnterprise));
    		return "vehicles/new";
    	}
    			    	    	
		vehiclesService.save(convertToVehicle(vehicle), brandId, idEnterprise);
		
		return "redirect:/managers/enterprises/" + idEnterprise + "/vehicles";
	}
	
	@GetMapping("/enterprises/{idEnterprise}/vehicles/{idVehicle}/edit")
	public String edit(@PathVariable("idEnterprise") int idEnterprise,
						@PathVariable("idVehicle") int idVehicle,
						Model model) {
		
		model.addAttribute("enterprise", enterprisesService.findOne(idEnterprise));
		model.addAttribute("vehicle", vehiclesService.findOne(idVehicle));
		model.addAttribute("brands", brandsService.findAll());
		
		return "vehicles/edit";
	}
	
	@PutMapping("/enterprises/{idEnterprise}/vehicles/{idVehicle}")
	public String update(@PathVariable("idEnterprise") int idEnterprise,
						@PathVariable("idVehicle") int idVehicle,
						@RequestParam("brandId") int brandId,
						Model model,
						@ModelAttribute("vehicle") @Valid VehicleDTO vehicle,
						BindingResult bindingResult) {
		
		if(bindingResult.hasErrors()) {
			model.addAttribute("errorMsg", "Введены некорректные данные. Попробуйте еще!");
			model.addAttribute("enterprise", enterprisesService.findOne(idEnterprise));
			model.addAttribute("vehicle", vehiclesService.findOne(idVehicle));
			model.addAttribute("brands", brandsService.findAll());
			return "vehicles/edit";
		}
		
		vehiclesService.update(idVehicle, convertToVehicle(vehicle), brandId, idEnterprise);
		
		return "redirect:/managers/enterprises/" + idEnterprise + "/vehicles";
	}
	
	@DeleteMapping("/enterprises/{idEnterprise}/vehicles/{idVehicle}")
	public String delete(@PathVariable("idEnterprise") int idEnterprise,
						@PathVariable("idVehicle") int idVehicle) {
		
		vehiclesService.delete(idVehicle);
		
		return "redirect:/managers/enterprises/" + idEnterprise + "/vehicles";
	}
	
	private Vehicle convertToVehicle(VehicleDTO vehicleDTO) {
		return modelMapper.map(vehicleDTO, Vehicle.class);
	}
	
	private VehicleDTO convertToVehicleDTO(Vehicle vehicle) {
		return modelMapper.map(vehicle, VehicleDTO.class);
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
