package ru.krivonogova.autopark.controllers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import jakarta.validation.Valid;
import ru.krivonogova.autopark.dto.ReportRequestDTO;
import ru.krivonogova.autopark.dto.TripDTO;
import ru.krivonogova.autopark.dto.TripDTO_forAPI;
import ru.krivonogova.autopark.dto.VehicleDTO;
import ru.krivonogova.autopark.models.Enterprise;
import ru.krivonogova.autopark.models.Period;
import ru.krivonogova.autopark.models.PointGps;
import ru.krivonogova.autopark.models.ReportResult;
import ru.krivonogova.autopark.models.Trip;
import ru.krivonogova.autopark.models.TypeReport;
import ru.krivonogova.autopark.models.Vehicle;
import ru.krivonogova.autopark.security.PersonDetails;
import ru.krivonogova.autopark.services.BrandsService;
import ru.krivonogova.autopark.services.EnterprisesService;
import ru.krivonogova.autopark.services.ManagersService;
import ru.krivonogova.autopark.services.PointsGpsService;
import ru.krivonogova.autopark.services.ReportsService;
import ru.krivonogova.autopark.services.TripService;
import ru.krivonogova.autopark.services.VehiclesService;

@Controller
@RequestMapping("/managers")
public class ManagersController {

	private final EnterprisesService enterprisesService;
	private final ManagersService managersService;
	private final VehiclesService vehiclesService;
	private final BrandsService brandsService;
	private final TripService tripService;
	private final PointsGpsService pointsGpsService;
	private final ReportsService reportsService;
	private final ModelMapper modelMapper;
	
	@Autowired	
	public ManagersController(EnterprisesService enterprisesService, ManagersService managersService, VehiclesService vehiclesService, ModelMapper modelMapper, BrandsService brandsService, TripService tripService, PointsGpsService pointsGpsService, ReportsService reportsService) {
		this.enterprisesService = enterprisesService;
		this.managersService = managersService;
		this.vehiclesService = vehiclesService;
		this.brandsService = brandsService;
		this.tripService = tripService;
		this.pointsGpsService = pointsGpsService;
		this.reportsService = reportsService;
		this.modelMapper = modelMapper;
	}

	@GetMapping("/enterprises")
	public ModelAndView indexEnterprisesWOid() {

		Integer idManager = getManagerId();
		
		ModelAndView enterprises = new ModelAndView("enterprises/index");
		
		enterprises.addObject("enterprises", enterprisesService.findAllForManager(idManager));
		
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
		
		Integer idManager = getManagerId();
		
		enterprisesService.update(idManager, idEnterprise, enterprise);
		
		return "redirect:/managers/enterprises";
	}
	
	@GetMapping("/enterprises/{idEnterprise}/vehicles")
	public String indexVehicles(@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
								@RequestParam(value = "itemsPerPage", required = false, defaultValue = "10") Integer itemsPerPage,
								@PathVariable("idEnterprise") int idEnterprise,
								Model model) {

		Integer idManager = getManagerId();
		String timezone = getManagerTimezone();   
				
		Page<Vehicle> vehiclesPage = vehiclesService.findForManagerByEnterpriseId(idManager, idEnterprise, page, itemsPerPage);
	    
		model.addAttribute("vehicles", vehiclesPage.getContent().stream().map(vehicle -> convertToVehicleDTO(vehicle, timezone)).collect(Collectors.toList()));
	    model.addAttribute("currentPage", vehiclesPage.getNumber() + 1);
	    model.addAttribute("totalPages", vehiclesPage.getTotalPages());
	    model.addAttribute("hasNext", vehiclesPage.hasNext());
	    model.addAttribute("hasPrevious", vehiclesPage.hasPrevious());
	    model.addAttribute("idEnterprise", idEnterprise);
	   
        return "vehicles/index";
	}
	
	
	@GetMapping("/report")
	public String create(@RequestParam(value = "idVehicle", required = false) Integer idVehicle,
						@RequestParam(value = "typeReport", required = false) TypeReport typeReport,
						@RequestParam(value = "period", required = false) Period period,
						@RequestParam(value = "dateFrom", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFrom,
                        @RequestParam(value = "dateTo", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateTo,
						Model model) {
		
		model.addAttribute("vehicles", vehiclesService.findAllForManager(getManagerId()));
		model.addAttribute("types", TypeReport.values());
		model.addAttribute("periods", Period.values());
		
		if(idVehicle != null) {
			
			System.out.println(dateFrom+ " " + dateTo);
			LocalDateTime dateTimeFrom = LocalDateTime.of(dateFrom, LocalTime.MIDNIGHT);
	        LocalDateTime dateTimeTo = LocalDateTime.of(dateTo, LocalTime.of(23, 59, 59));
	        
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
	        String formattedDateTimeFrom = dateTimeFrom.format(formatter);
	        String formattedDateTimeTo = dateTimeTo.format(formatter);
	        
	        System.out.println(formattedDateTimeFrom + " " + formattedDateTimeTo);
	        
	        ReportRequestDTO request = new ReportRequestDTO(idVehicle, typeReport, period, formattedDateTimeFrom, formattedDateTimeTo);
	        List<Trip> trips = tripService.findAllByTimePeriod(idVehicle, formattedDateTimeFrom, formattedDateTimeTo);
	        
	        for(Trip trip : trips) {
	        	System.out.println(trip.getId());
	        }
	        
	        List<ReportResult> result = reportsService.getReport(request, trips);
	        model.addAttribute("result", result);
		}
		
		return "report/new";		
	}
	
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
		
		model.addAttribute("dateFrom", formattedFrom);
		model.addAttribute("dateTo", formattedTo);
		model.addAttribute("vehicle", vehiclesService.findOne(idVehicle));
		model.addAttribute("enterprise", enterprisesService.findOne(idEnterprise));
		
	    String timezone = getManagerTimezone();   
	    
		List<Trip> trips = tripService.findAllByTimePeriod(idVehicle, dateFrom, dateTo);
		List<TripDTO> tripList = trips.stream()
			    .map(trip -> convertToTripDTO(trip, timezone))
			    .collect(Collectors.toList());
		model.addAttribute("trips", tripList);
		//model.addAttribute("trips", trips.stream().map(trip -> convertToTripDTO(trip, timezone)));
				
		if(idTrip != null) {
			List<PointGps> points = pointsGpsService.findAllByVehicleAndTrip(idVehicle, Arrays.asList(tripService.findOne(idTrip)));
			String request = generateMapRequest(points);
			System.out.println(request);
			model.addAttribute("mapUrl", request);
			model.addAttribute("idTrip", idTrip);
		}
		
		if(showAll) {
			List<PointGps> points = pointsGpsService.findAllByVehicleAndTrip(idVehicle, trips);
			String request = generateMapRequest(points);
			System.out.println(request);
			model.addAttribute("mapUrlForAll", request);
		}
		
		return "vehicles/show";
	}
	
	/*@GetMapping("/enterprises/{idEnterprise}/vehicles/{idVehicle}/trips/{idTrip}")
	public String showTrip(@PathVariable("idEnterprise") int idEnterprise,
						@PathVariable("idVehicle") int idVehicle,
						@PathVariable("idTrip") int idTrip,
						Model model) {
		
		List<PointGps> points = pointsGpsService.findAllByVehicleAndTrip(idVehicle, Arrays.asList(tripService.findOne(idTrip)));
		
		String request = generateMapRequest(points);
		
		System.out.println(request);
		
		model.addAttribute("mapUrl", request);
		
		return "trips/show";
	}*/
	
	private String generateMapRequest(List<PointGps> points) {
		String API_KEY = "4bc5b115-13bb-4a08-9e29-88347ed6207a";
		StringBuilder request = new StringBuilder("https://static-maps.yandex.ru/v1?");

        // Добавляем параметр pl
        StringBuilder plBuilder = new StringBuilder("pl=c:8822DDC0,w:5");
        for (PointGps point : points) {
            plBuilder.append(",").append(point.getLongitude()).append(",").append(point.getLatitude());
        }
        request.append(plBuilder.toString());

        // Добавляем API ключ
        request.append("&apikey=").append(API_KEY);

        return request.toString();
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
		Optional<PointGps> pointOfStart = pointsGpsService.findByVehicleAndTime(trip.getVehicle().getId(), timeOfStart);
		Optional<PointGps> pointOfEnd = pointsGpsService.findByVehicleAndTime(trip.getVehicle().getId(), timeOfEnd);
		
		String addressOfStart = pointsGpsService.takeAddressOfPointGPS(pointOfStart.get());
		String addressOfEnd = pointsGpsService.takeAddressOfPointGPS(pointOfEnd.get());

		tripDTO.setAddressOfStart(addressOfStart);
		tripDTO.setAddressOfEnd(addressOfEnd);
		
	    tripDTO.setTimeOfStartForManager(dateOfStartForEnterprise);
	    tripDTO.setTimeOfEndForManager(dateOfEndForEnterprise);
				
		return tripDTO;
		
	}
	
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
