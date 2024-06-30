package ru.krivonogova.autopark.controllers.view;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import ru.krivonogova.autopark.controllers.DatabaseController;
import ru.krivonogova.autopark.dto.ReportRequestDTO;
import ru.krivonogova.autopark.dto.TripDTO;
import ru.krivonogova.autopark.dto.VehicleDTO;
import ru.krivonogova.autopark.models.Enterprise;
import ru.krivonogova.autopark.models.Period;
import ru.krivonogova.autopark.models.PointGps;
import ru.krivonogova.autopark.models.ReportResult;
import ru.krivonogova.autopark.models.Trip;
import ru.krivonogova.autopark.models.TypeReport;
import ru.krivonogova.autopark.models.Vehicle;
import ru.krivonogova.autopark.security.PersonDetails;
import ru.krivonogova.autopark.services.PointsGpsService;
import ru.krivonogova.autopark.services.ReportsService;

@Slf4j
@Controller
@RequestMapping("/managers")
public class ManagerController {
	private final DatabaseController databaseController;
	private final PointsGpsService pointsGpsService;
	private final ReportsService reportsService;
	private final ModelMapper modelMapper;
	
	@Autowired
	public ManagerController(DatabaseController databaseController, ModelMapper modelMapper, PointsGpsService pointsGpsService, ReportsService reportsService) {
		this.databaseController = databaseController;
		this.pointsGpsService = pointsGpsService;
		this.reportsService = reportsService;
		this.modelMapper = modelMapper;
	}
	
	// CRUD для предприятий
	
	@GetMapping("/enterprises")
	public ModelAndView indexEnterprises() {
		Integer idManager = getManagerId();
		ModelAndView enterprises = new ModelAndView("enterprises/index");
		enterprises.addObject("enterprises", databaseController.findAllEnterprisesForManager(idManager));
		return enterprises;
	}
	
	@GetMapping("/enterprises/{idEnterprise}/edit")
	public String edit(@PathVariable("idEnterprise") int idEnterprise,
						Model model) {
		model.addAttribute("enterprise", databaseController.findOneEnterprise(idEnterprise));
		return "enterprises/edit";
	}
	
	@PutMapping("/enterprises/{idEnterprise}")
	public String update(@PathVariable("idEnterprise") int idEnterprise,
						Model model,
						@ModelAttribute("enterprise") @Valid Enterprise enterprise,
						BindingResult bindingResult) {
		
		if(bindingResult.hasErrors()) {
			model.addAttribute("errorMsg", "Введены некорректные данные. Попробуйте еще!");
			model.addAttribute("enterprise", databaseController.findOneEnterprise(idEnterprise));
			return "enterprises/edit";
		}
		
		Integer idManager = getManagerId();
		
		databaseController.updateEnterprise(idManager, idEnterprise, enterprise);
		
		return "redirect:/managers/enterprises";
	}
	
	// CRUD для транспорта
	
	@GetMapping("/enterprises/{idEnterprise}/vehicles")
	public String indexVehicles(@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
								@RequestParam(value = "itemsPerPage", required = false, defaultValue = "10") Integer itemsPerPage,
								@PathVariable("idEnterprise") int idEnterprise,
								Model model) {

		Integer idManager = getManagerId();
		String timezone = getManagerTimezone();   
				
		Page<Vehicle> vehiclesPage = databaseController.findForManagerByEnterpriseId(idManager, idEnterprise, page, itemsPerPage);
	    
		model.addAttribute("vehicles", vehiclesPage.getContent().stream().map(vehicle -> convertToVehicleDTO(vehicle, timezone)).collect(Collectors.toList()));
	    model.addAttribute("currentPage", vehiclesPage.getNumber() + 1);
	    model.addAttribute("totalPages", vehiclesPage.getTotalPages());
	    model.addAttribute("hasNext", vehiclesPage.hasNext());
	    model.addAttribute("hasPrevious", vehiclesPage.hasPrevious());
	    model.addAttribute("idEnterprise", idEnterprise);
	   
        return "vehicles/index";
	}
	
	// проверим кэш
	@GetMapping("/enterprises/{idEnterprise}/vehicles/{idVehicle}")
	public String show(@PathVariable("idEnterprise") int idEnterprise,
						@PathVariable("idVehicle") int idVehicle,
						@RequestParam(value = "dateFrom", defaultValue = "") String dateFrom,
						@RequestParam(value = "dateTo", defaultValue = "") String dateTo,
						@RequestParam(value = "idTrip", required = false) Integer idTrip,
						@RequestParam(value = "showAll", defaultValue = "false") boolean showAll,
						Model model) {
			
		DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		
		LocalDate dateFromDate = dateFrom.isEmpty() ? LocalDate.now().minusMonths(1) : LocalDate.parse(dateFrom, inputFormatter);
		LocalDate dateToDate = dateTo.isEmpty() ? LocalDate.now() : LocalDate.parse(dateTo, inputFormatter);
		
		dateFrom = dateFromDate.atStartOfDay().format(outputFormatter);
		dateTo = dateToDate.atStartOfDay().plusDays(1).minusSeconds(1).format(outputFormatter);
		
		// для представления
		String formattedFrom = dateFromDate.atStartOfDay().format(inputFormatter);
		String formattedTo = dateToDate.atStartOfDay().format(inputFormatter);
		
		// test
		//log.debug("Start check vehicle cache for vehicle by id: {}", idVehicle);
		Vehicle vehicle = databaseController.findOneVehicle(idVehicle);
		//log.debug("End check vehicle cache for vehicle by id: {}", idVehicle);
		
		model.addAttribute("dateFrom", formattedFrom);
		model.addAttribute("dateTo", formattedTo);
		model.addAttribute("vehicle", vehicle);
		model.addAttribute("enterprise", databaseController.findOneEnterprise(idEnterprise));
		
	    String timezone = getManagerTimezone();   
	    
	    //log.debug("Start check trips cache for vehicle by id: {}", idVehicle);
		List<Trip> trips = databaseController.findAllTripsByTimePeriod(idVehicle, dateFrom, dateTo);
		//log.debug("End check trips cache for vehicle by id: {}", idVehicle);
		List<TripDTO> tripList = trips.stream()
			    .map(trip -> convertToTripDTO(trip, timezone))
			    .collect(Collectors.toList());
		model.addAttribute("trips", tripList);
		//model.addAttribute("trips", trips.stream().map(trip -> convertToTripDTO(trip, timezone)));
				
		if(idTrip != null) {
			log.debug("Start check points cache for trip by id: {}", idTrip);
			List<PointGps> points = databaseController.findAllByVehicleAndTrip(idVehicle, idTrip);
			log.debug("End check points cache for trip by id: {}", idTrip);
			String request = pointsGpsService.generateMapRequest(points);
			model.addAttribute("mapUrl", request);
			model.addAttribute("idTrip", idTrip);
		}
		
		if(showAll) {
			List<PointGps> points = databaseController.findAllByVehicleAndTrip(idVehicle, trips);
			String request = pointsGpsService.generateMapRequest(points);
			model.addAttribute("mapUrlForAll", request);
		}
		
		return "vehicles/show";
	}
	
	@GetMapping("/enterprises/{idEnterprise}/vehicles/new")
	public String newVehicle(@ModelAttribute("vehicle") VehicleDTO vehicle,
							@PathVariable("idEnterprise") int idEnterprise,
							Model model) {
		
		model.addAttribute("brands", databaseController.findAllBrands());
		//model.addAttribute("idEnterprise", idEnterprise);
		model.addAttribute("enterprise", databaseController.findOneEnterprise(idEnterprise));
			
		return "vehicles/new";
	}
	
	@PostMapping("/enterprises/{idEnterprise}/vehicles/new")
	public String create(@RequestParam("brandId") int brandId,
						@PathVariable("idEnterprise") int idEnterprise,
						Model model,
						@ModelAttribute("vehicle") @Valid VehicleDTO vehicle,
						BindingResult bindingResult) {
				
    	if(bindingResult.hasErrors()) {
    		model.addAttribute("brands", databaseController.findAllBrands());
    		model.addAttribute("enterprise", databaseController.findOneEnterprise(idEnterprise));
    		return "vehicles/new";
    	}
    			    	    	
		databaseController.saveVehicle(convertToVehicle(vehicle), brandId, idEnterprise);
		
		return "redirect:/managers/enterprises/" + idEnterprise + "/vehicles";
	}
	
	@GetMapping("/enterprises/{idEnterprise}/vehicles/{idVehicle}/edit")
	public String edit(@PathVariable("idEnterprise") int idEnterprise,
						@PathVariable("idVehicle") int idVehicle,
						Model model) {
		
		model.addAttribute("enterprise", databaseController.findOneEnterprise(idEnterprise));
		model.addAttribute("vehicle", databaseController.findOneVehicle(idVehicle));
		model.addAttribute("brands", databaseController.findAllBrands());
		
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
			model.addAttribute("enterprise", databaseController.findOneEnterprise(idEnterprise));
			model.addAttribute("vehicle", databaseController.findOneVehicle(idVehicle));
			model.addAttribute("brands", databaseController.findAllBrands());
			return "vehicles/edit";
		}
		
		databaseController.updateVehicle(idVehicle, convertToVehicle(vehicle), brandId, idEnterprise);
		
		log.warn("Vehicle by id: {} was updated", idVehicle);
		
		return "redirect:/managers/enterprises/" + idEnterprise + "/vehicles";
	}
	
	@DeleteMapping("/enterprises/{idEnterprise}/vehicles/{idVehicle}")
	public String delete(@PathVariable("idEnterprise") int idEnterprise,
						@PathVariable("idVehicle") int idVehicle) {
		
		databaseController.deleteVehicle(idVehicle);
		
		return "redirect:/managers/enterprises/" + idEnterprise + "/vehicles";
	}
	
	// Формирование отчета
	
	@GetMapping("/report")
	public String create(@RequestParam(value = "idVehicle", required = false) Integer idVehicle,
						@RequestParam(value = "typeReport", required = false) TypeReport typeReport,
						@RequestParam(value = "period", required = false) Period period,
						@RequestParam(value = "dateFrom", defaultValue = "") String dateFrom,
						@RequestParam(value = "dateTo", defaultValue = "") String dateTo,
						Model model) {
		
		DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
		
		LocalDate dateFromDate = dateFrom.isEmpty() ? LocalDate.now().minusWeeks(1) : LocalDate.parse(dateFrom, inputFormatter);
		LocalDate dateToDate = dateTo.isEmpty() ? LocalDate.now() : LocalDate.parse(dateTo, inputFormatter);
		
		dateFrom = dateFromDate.atStartOfDay().format(outputFormatter);
		dateTo = dateToDate.atStartOfDay().plusDays(1).minusSeconds(1).format(outputFormatter);
		
		// для представления
		String formattedFrom = dateFromDate.atStartOfDay().format(inputFormatter);
		String formattedTo = dateToDate.atStartOfDay().format(inputFormatter);
		
		model.addAttribute("dateFrom", formattedFrom);
		model.addAttribute("dateTo", formattedTo);		
		model.addAttribute("vehicles", databaseController.findAllVehiclesForManager(getManagerId()));
		model.addAttribute("types", TypeReport.values());
		model.addAttribute("periods", Period.values());
		
		if(idVehicle != null) {       	        
	        ReportRequestDTO request = new ReportRequestDTO(idVehicle, typeReport, period, dateFrom, dateTo);
	        List<Trip> trips = databaseController.findAllTripsByTimePeriod(idVehicle, dateFrom, dateTo);	        
	        List<ReportResult> result = reportsService.getReport(request, trips);
	        model.addAttribute("result", result);
		}
		
		return "report/new";		
	}
	
	@GetMapping("/report/show")
	public String show(@RequestParam(value = "idVehicle", required = false) Integer idVehicle,
						@RequestParam(value = "typeReport", required = false) TypeReport typeReport,
						@RequestParam(value = "period", required = false) Period period,
						@RequestParam(value = "dateFrom", defaultValue = "") String dateFrom,
						@RequestParam(value = "dateTo", defaultValue = "") String dateTo,
						Model model) {
		
		DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
		
		LocalDate dateFromDate = dateFrom.isEmpty() ? LocalDate.now().minusWeeks(1) : LocalDate.parse(dateFrom, inputFormatter);
		LocalDate dateToDate = dateTo.isEmpty() ? LocalDate.now() : LocalDate.parse(dateTo, inputFormatter);
		
		dateFrom = dateFromDate.atStartOfDay().format(outputFormatter);
		dateTo = dateToDate.atStartOfDay().plusDays(1).minusSeconds(1).format(outputFormatter);
		
		// для представления
		String formattedFrom = dateFromDate.atStartOfDay().format(inputFormatter);
		String formattedTo = dateToDate.atStartOfDay().format(inputFormatter);
		
		model.addAttribute("dateFrom", formattedFrom);
		model.addAttribute("dateTo", formattedTo);		
		model.addAttribute("vehicle", databaseController.findOneVehicle(idVehicle));
		model.addAttribute("type", typeReport);
		model.addAttribute("period", period);
		     	        
        ReportRequestDTO request = new ReportRequestDTO(idVehicle, typeReport, period, dateFrom, dateTo);
        List<Trip> trips = databaseController.findAllTripsByTimePeriod(idVehicle, dateFrom, dateTo);	        
        List<ReportResult> result = reportsService.getReport(request, trips);
        model.addAttribute("result", result);
		
        log.info("Report for vehicle by id: {} was created", idVehicle);
        
		return "report/show";
	}
	
	// Вспомогательное
	
	private Integer getManagerId() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
		Integer idManager = personDetails.getPerson().getId();
		
		return idManager;
	}
	
	private String getManagerTimezone() {
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
	
	private TripDTO convertToTripDTO(Trip trip, String timezone) {
		
		TripDTO tripDTO = modelMapper.map(trip, TripDTO.class);
		
		// установка времени
		String timeOfStart = trip.getTimeOfStart();
		String timeOfEnd = trip.getTimeOfEnd();
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
	    LocalDateTime dateOfStart_UTC = LocalDateTime.parse(timeOfStart, formatter);
	    LocalDateTime dateOfEnd_UTC = LocalDateTime.parse(timeOfEnd, formatter);
		
	    ZoneOffset timeZone = ZoneOffset.of(timezone);
	    
	    LocalDateTime dateOfStart = dateOfStart_UTC.atZone(ZoneOffset.UTC).withZoneSameInstant(timeZone).toLocalDateTime();
	    LocalDateTime dateOfEnd = dateOfEnd_UTC.atZone(ZoneOffset.UTC).withZoneSameInstant(timeZone).toLocalDateTime();
	    
	    String dateOfStartForEnterprise = dateOfStart.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
	    String dateOfEndForEnterprise = dateOfEnd.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
	 	    
		// установка адресов		
		Optional<PointGps> pointOfStart = databaseController.findByVehicleAndTime(trip.getVehicle().getId(), timeOfStart);
		Optional<PointGps> pointOfEnd = databaseController.findByVehicleAndTime(trip.getVehicle().getId(), timeOfEnd);
		
		String addressOfStart = pointsGpsService.takeAddressOfPointGPS(pointOfStart.get());
		String addressOfEnd = pointsGpsService.takeAddressOfPointGPS(pointOfEnd.get());

		tripDTO.setAddressOfStart(addressOfStart);
		tripDTO.setAddressOfEnd(addressOfEnd);
		
	    tripDTO.setTimeOfStartForManager(dateOfStartForEnterprise);
	    tripDTO.setTimeOfEndForManager(dateOfEndForEnterprise);
				
		return tripDTO;
	}
	
	private Vehicle convertToVehicle(VehicleDTO vehicleDTO) {
		return modelMapper.map(vehicleDTO, Vehicle.class);
	}
}
