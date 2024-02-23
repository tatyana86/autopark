package ru.krivonogova.autopark.controllers;

import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import jakarta.validation.Valid;
import ru.krivonogova.autopark.dto.VehicleDTO;
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
	
	@GetMapping("/enterprises/{id}/vehicles")
	public String indexVehicles(@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
								@RequestParam(value = "itemsPerPage", required = false, defaultValue = "10") Integer itemsPerPage,
								@PathVariable("id") int idEnterprise,
								Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
		String username = personDetails.getPerson().getUsername();
		
		Integer idManager = managersService.findByUsername(username).getId();
				
		Page<Vehicle> vehiclesPage = vehiclesService.findForManagerByEnterpriseId(idManager, idEnterprise, page, itemsPerPage);
	    model.addAttribute("vehicles", vehiclesPage.getContent().stream().map(this::convertToVehicleDTO).collect(Collectors.toList()));
	    model.addAttribute("currentPage", vehiclesPage.getNumber() + 1);
	    model.addAttribute("totalPages", vehiclesPage.getTotalPages());
	    model.addAttribute("hasNext", vehiclesPage.hasNext());
	    model.addAttribute("hasPrevious", vehiclesPage.hasPrevious());
	    model.addAttribute("idEnterprise", idEnterprise);
	    
	    
        return "vehicles/index";
	}
	
	@GetMapping("/enterprises/{id}/vehicles/new")
	public String newVehicle(@ModelAttribute("vehicle") Vehicle vehicle,
							@PathVariable("id") int idEnterprise,
							Model model) {
		
		model.addAttribute("brands", brandsService.findAll());
		//model.addAttribute("idEnterprise", idEnterprise);
		model.addAttribute("enterprise", enterprisesService.findOne(idEnterprise));
		
		return "vehicles/new";
	}
	
	@PostMapping("/enterprises/{id}/vehicles/new")
	public String create(@RequestParam("brandId") int brandId,
						@PathVariable("id") int idEnterprise,
						Model model,
						@ModelAttribute("vehicle") @Valid Vehicle vehicle,
						BindingResult bindingResult) {
				
    	if(bindingResult.hasErrors()) {
    		model.addAttribute("brands", brandsService.findAll());
    		model.addAttribute("enterprise", enterprisesService.findOne(idEnterprise));
    		return "vehicles/new";
    	}
		    	    	
		vehiclesService.save(vehicle, brandId, idEnterprise);
		
		return "redirect:/managers/enterprises/" + idEnterprise + "/vehicles";
	}
	
	private VehicleDTO convertToVehicleDTO(Vehicle vehicle) {
		return modelMapper.map(vehicle, VehicleDTO.class);
	}
}
