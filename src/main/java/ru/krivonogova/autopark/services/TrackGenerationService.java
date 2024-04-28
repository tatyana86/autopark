package ru.krivonogova.autopark.services;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.krivonogova.autopark.dto.BigDataGenerationDTO;
import ru.krivonogova.autopark.dto.PointGpsCoord;
import ru.krivonogova.autopark.dto.TrackGenerationDTO;
import ru.krivonogova.autopark.models.PointGps;
import ru.krivonogova.autopark.models.RouteResponse;
import ru.krivonogova.autopark.models.Trip;
import ru.krivonogova.autopark.models.Vehicle;

@Service
@Transactional(readOnly = true)
public class TrackGenerationService {
	
	private final VehiclesService vehiclesService;
	private final PointsGpsService pointsGpsService;
	private final TripService tripService;
	
	@Autowired
    public TrackGenerationService(VehiclesService vehiclesService, PointsGpsService pointsGpsService, TripService tripService) {
		this.vehiclesService = vehiclesService;
		this.pointsGpsService = pointsGpsService;
		this.tripService = tripService;
	}

	// вариант 1 с радиусом
	double centerLongitude = 50.157566;
    double centerLatitude = 53.228851;
    //double radius = 4; // в км
    
    // вариант 2
	double minLatitude = 53.15;
	double maxLatitude = 53.30;
	double minLongitude = 50.05;
	double maxLongitude = 50.25;
    
	private final String openRouteUrl = "https://api.openrouteservice.org/v2/directions/driving-car?api_key=5b3ce3597851110001cf624881622e92681c4a87909ab330c6345a1e";
	
	@Transactional
	public void generateTrack(TrackGenerationDTO request) {
		
		// test data
		/*double startLong = 50.170580; 
        double startLat = 53.212259;
        double endLong = 50.179861;
        double endLat = 53.212779;*/
		
		int lengthOfTrack = request.getLengthOfTrack();
		double distance = 0;
		
		double startLong = 0; 
        double startLat = 0;
        double endLong = 0;
        double endLat = 0;
        
        RouteResponse routeResponse = new RouteResponse();
		
		while(distance == 0) {
	        PointGpsCoord start = generateStart();
	        PointGpsCoord end = generateEnd(start, lengthOfTrack);
	        
	        startLong = start.getLongitude(); 
	        startLat = start.getLatitude();
	        endLong = end.getLongitude();
	        endLat = end.getLatitude();
	        
	        routeResponse = getRouteResponse(startLong, startLat, endLong, endLat);
	        distance = routeResponse.getDistance();
		}
				
		Vehicle vehicle = vehiclesService.findOne(request.getIdVehicle());
		saveTrackAndTrip(routeResponse.getTrack(), vehicle, routeResponse.getDistance());
		
	}
	
	@Transactional
	public void saveTrackAndTrip(List<PointGpsCoord> track, Vehicle vehicle, double distance) {
		
		List<PointGps> points = saveTrack(track, vehicle);

		// блок сохранения отдельной поездки в таблицу trip
		String timeOfStart = points.get(0).getTimeOfPointGps();
		String timeOfEnd = points.get(points.size()-1).getTimeOfPointGps();
		Trip trip = new Trip(vehicle, timeOfStart, timeOfEnd, distance);
		tripService.save(trip);
	}
	
	// сохранениe gps-точек в таблицу track "в реальном времени"
	@Transactional
	public List<PointGps> saveTrack(List<PointGpsCoord> track, Vehicle vehicle) {
		
		List<PointGps> points = new ArrayList<>();
		
		for (PointGpsCoord pointOfTrack : track) {
		    PointGps point = new PointGps();
		    
		    point.setVehicle(vehicle);
		    
		    GeometryFactory geometryFactory = new GeometryFactory();
	        Coordinate coordinate = new Coordinate(pointOfTrack.getLongitude(), pointOfTrack.getLatitude());
	        Point coord = geometryFactory.createPoint(coordinate);
		    point.setCoordinates(coord);
		    
		    point.setTimeOfPointGps(getTime());
		    
		    points.add(point);
		    
		    pointsGpsService.save(point);
		    
            try {
                TimeUnit.SECONDS.sleep(1); // Делаем паузу в конце каждой итерации
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
		}
		
		return points;
	}
	
	// сохранениe gps-точек в таблицу track в течении последних месяцев
	@Transactional
	public List<PointGps> saveTrack(List<PointGpsCoord> track, Vehicle vehicle, int numberOfMonth) {
		
	    // определение даты начала периода
	    Calendar calendar = Calendar.getInstance();
	    Date endTime = calendar.getTime();
	    calendar.add(Calendar.MONTH, -numberOfMonth);
	    Date startTime = calendar.getTime();

	    // определение случайного времени начала поездки
	    Random random = new Random();
	    long startMillis = startTime.getTime();
	    long endMillis = endTime.getTime();
	    long randomMillis = startMillis + (long)(random.nextDouble() * (endMillis - startMillis));
	    Date randomTime = new Date(randomMillis);
		
	    List<PointGps> points = new ArrayList<>();
	    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	    
		for (PointGpsCoord pointOfTrack : track) {
		    PointGps point = new PointGps();
		    point.setVehicle(vehicle);
		    
		    GeometryFactory geometryFactory = new GeometryFactory();
	        Coordinate coordinate = new Coordinate(pointOfTrack.getLongitude(), pointOfTrack.getLatitude());
	        Point coord = geometryFactory.createPoint(coordinate);
		    point.setCoordinates(coord);
		    
		    point.setTimeOfPointGps(dateFormat.format(randomTime));
		    points.add(point);
		    pointsGpsService.save(point);
		    
		    // увеличение временной метки на 1 минуту для следующей точки
            randomTime.setTime(randomTime.getTime() + 60000);
		}
		
		return points;
	}
	
	@Transactional
	public void saveTrackAndTrip(List<PointGpsCoord> track, Vehicle vehicle, double distance, int numberOfMonth) {
		List<PointGps> points = saveTrack(track, vehicle, numberOfMonth);
				
		// блок сохранения отдельной поездки в таблицу trip
		String timeOfStart = points.get(0).getTimeOfPointGps();
		String timeOfEnd = points.get(points.size()-1).getTimeOfPointGps();
		Trip trip = new Trip(vehicle, timeOfStart, timeOfEnd, distance);
		tripService.save(trip);
	}
	
	@Transactional
	public void generateTrack(Vehicle vehicle, int numberOfMonth) throws InterruptedException {
		
		int lengthOfTrack = new Random().nextInt(5) + 1;
        
		double distance = 0;
		
		double startLong = 0; 
        double startLat = 0;
        double endLong = 0;
        double endLat = 0;
        
        RouteResponse routeResponse = new RouteResponse();
		
		while(distance == 0) {
	        PointGpsCoord start = generateStart();
	        PointGpsCoord end = generateEnd(start, lengthOfTrack);
	        
	        startLong = start.getLongitude(); 
	        startLat = start.getLatitude();
	        endLong = end.getLongitude();
	        endLat = end.getLatitude();
	        
	        routeResponse = getRouteResponse(startLong, startLat, endLong, endLat);
	        distance = routeResponse.getDistance();
	        
	        //Thread.sleep(6000); // Пауза на 1 секунду (1000 миллисекунд)
		}
			
		saveTrackAndTrip(routeResponse.getTrack(), vehicle, routeResponse.getDistance(), numberOfMonth);
	}
	
	@Transactional
	public void generateBigData(BigDataGenerationDTO request) throws InterruptedException {
		
		int numberOfTrack = request.getNumberOfTrack();
		int numberOfMonth = request.getNumberOfMonth();
		
		List<Vehicle> allVehicles = vehiclesService.findAll();
		
		for(int i = 0; i < numberOfTrack; i++) {
			Vehicle randomVehicle = allVehicles.get(new Random().nextInt(allVehicles.size()));
	
			generateTrack(randomVehicle, numberOfMonth);
		}
	}
	
	private String getTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());

        return dateFormat.format(date);
    }
	
	/*private PointGpsCoord generateStart() {
		Random random = new Random();

        // генерация случайного угла и радиуса в полярных координатах
        double angle = 2 * Math.PI * random.nextDouble();
        double r = radius * Math.sqrt(random.nextDouble());

        // преобразование полярных координат в декартовы
        double longitude = centerLongitude + r * Math.cos(angle) / (111.32 * Math.cos(centerLatitude));
        double latitude = centerLatitude + r * Math.sin(angle) / 111.32;
        
        return new PointGpsCoord(longitude, latitude);
	}*/
	
	/*private PointGpsCoord generateEnd(PointGpsCoord start, int lengthOfTrack) {
		// Константа для преобразования расстояния в градусы долготы
	    double degreesPerKm = 0.0089 / 1.0; // Наилучший подход к получению примерной очертания сферы
	
	    // Генерация случайного угла в радианах
	    double angle = Math.random() * 2 * Math.PI;
	
	    // Преобразование расстояния в градусы долготы и широты
	    double deltaLatitude = lengthOfTrack * degreesPerKm * Math.cos(angle);
	    double deltaLongitude = lengthOfTrack * degreesPerKm * Math.sin(angle);
	
	    // Вычисление конечной координаты
	    double latitude = start.getLatitude() + deltaLatitude;
	    double longitude = start.getLongitude() + deltaLongitude;
	
	    return new PointGpsCoord(longitude, latitude);
	}*/
	
	private PointGpsCoord generateStart() {
		Random random = new Random();

		double latitude = minLatitude + (maxLatitude - minLatitude) * random.nextDouble();
		double longitude = minLongitude + (maxLongitude - minLongitude) * random.nextDouble();

		return new PointGpsCoord(longitude, latitude);
		}
	
	private PointGpsCoord generateEnd(PointGpsCoord start, int lengthOfTrack) {
	    Random random = new Random();
	    double degreesPerKm = 0.0089 / 1.0;
	    double latitude;
	    double longitude;

	    do {
	        double angle = Math.random() * 2 * Math.PI;
	        double deltaLatitude = lengthOfTrack * degreesPerKm * Math.cos(angle);
	        double deltaLongitude = lengthOfTrack * degreesPerKm * Math.sin(angle);
	        latitude = start.getLatitude() + deltaLatitude;
	        longitude = start.getLongitude() + deltaLongitude;
	    } while (latitude < 53.15 || latitude > 53.30 || longitude < 50.05 || longitude > 50.25);

	    return new PointGpsCoord(longitude, latitude);
	}
			
	/*private double getDistance(double startLong, double startLat, double endLong, double endLat) {
		double distance = 0;			
        String requestUrl = openRouteUrl + "&start=" + startLong + "," + startLat + "&end=" + endLong + "," + endLat;
        
        try {
            HttpClient httpClient = HttpClients.createDefault();
            HttpGet request = new HttpGet(requestUrl);
            request.addHeader("Content-Type", "application/json");

            HttpResponse response = httpClient.execute(request);
            
            if (response.getStatusLine().getStatusCode() == 200) {
            	HttpEntity entity = response.getEntity();
                String jsonResponse = EntityUtils.toString(entity, "UTF-8");
                distance = parseResponseForDistance(jsonResponse);
            } else {
                System.out.println("GET request failed111");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return distance;
	}*/
	
	/*private static double parseResponseForDistance(String jsonResponse) {
		double distance = 0;
        try {
            JSONObject responseObject = new JSONObject(jsonResponse);
            JSONArray features = responseObject.getJSONArray("features");
            for (int i = 0; i < features.length(); i++) {
                JSONObject feature = features.getJSONObject(i);
                JSONObject properties = feature.getJSONObject("properties");
                JSONObject summary = properties.getJSONObject("summary");
                distance = summary.getDouble("distance");
                System.out.println("Длина трека: " + distance);          
            }
        } catch (JSONException e) {
            e.printStackTrace();
            // обработка ошибки чтения или парсинга JSON
        }
        return distance;
    }*/
		
	/*private List<PointGpsCoord> getRouting(double startLong, double startLat, double endLong, double endLat) {
		List<PointGpsCoord> points = new ArrayList<>();	
        String requestUrl = openRouteUrl + "&start=" + startLong + "," + startLat + "&end=" + endLong + "," + endLat;
        System.out.println(requestUrl);
        
        try {
            HttpClient httpClient = HttpClients.createDefault();
            HttpGet request = new HttpGet(requestUrl);
            request.addHeader("Content-Type", "application/json");

            HttpResponse response = httpClient.execute(request);
            
            System.out.println(response);

            if (response.getStatusLine().getStatusCode() == 200) {
            	HttpEntity entity = response.getEntity();
                String jsonResponse = EntityUtils.toString(entity, "UTF-8");
                points = parseResponse(jsonResponse);

                for (PointGpsCoord point : points) {
                    System.out.println("Point: " + point.getLongitude() + ", " + point.getLatitude());
                }
            } else {
                System.out.println("GET request failed222");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return points;
	}*/
	
	/*private static List<PointGpsCoord> parseResponse(String jsonResponse) {
        List<PointGpsCoord> points = new ArrayList<>();
        try {
            JSONObject responseObject = new JSONObject(jsonResponse);
            JSONArray features = responseObject.getJSONArray("features");
            for (int i = 0; i < features.length(); i++) {
                JSONObject feature = features.getJSONObject(i);
                JSONObject geometry = feature.getJSONObject("geometry");
                JSONArray coordinates = geometry.getJSONArray("coordinates");
                for (int j = 0; j < coordinates.length(); j++) {
                    JSONArray point = coordinates.getJSONArray(j);
                    double longitude = point.getDouble(0);
                    double latitude = point.getDouble(1);
                    points.add(new PointGpsCoord(longitude, latitude));
                }
                
                double distance = feature.getJSONObject("properties").getJSONObject("summary").getDouble("distance");
                System.out.println("Длина трека: " + distance);
                
            }
        } catch (JSONException e) {
            e.printStackTrace();
            // обработка ошибки чтения или парсинга JSON
        }
        return points;
    }*/
	
	private RouteResponse getRouteResponse(double startLong, double startLat, double endLong, double endLat) {
		
		RouteResponse routeResponse = new RouteResponse();
		
        String requestUrl = openRouteUrl + "&start=" + startLong + "," + startLat + "&end=" + endLong + "," + endLat;
        System.out.println(requestUrl); // запрос
        
        try {
            HttpClient httpClient = HttpClients.createDefault();
            HttpGet request = new HttpGet(requestUrl);
            request.addHeader("Content-Type", "application/json");

            HttpResponse response = httpClient.execute(request);
            System.out.println(response); // ответ

            if (response.getStatusLine().getStatusCode() == 200) {
            	HttpEntity entity = response.getEntity();
                String jsonResponse = EntityUtils.toString(entity, "UTF-8");
                routeResponse = parseResponse(jsonResponse);
               
                System.out.println("Number of GPS-points: " + routeResponse.getTrack().size());
                /*for (PointGpsCoord point : routeResponse.getTrack()) {
                    System.out.println("Point: " + point.getLongitude() + ", " + point.getLatitude());
                }*/
            } else if (response.getStatusLine().getStatusCode() == 503) {
            	Thread.sleep(4500);
            } else if (response.getStatusLine().getStatusCode() == 429) {
            	Thread.sleep(10000);
            }
            else {
                System.out.println("GET request failed");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return routeResponse;
	}
	
	private static RouteResponse parseResponse(String jsonResponse) {
		
		RouteResponse routeResponse = new RouteResponse();
        List<PointGpsCoord> points = new ArrayList<>();
        
        try {
            JSONObject responseObject = new JSONObject(jsonResponse);
            JSONArray features = responseObject.getJSONArray("features");
            for (int i = 0; i < features.length(); i++) {
                JSONObject feature = features.getJSONObject(i);
                JSONObject geometry = feature.getJSONObject("geometry");
                JSONArray coordinates = geometry.getJSONArray("coordinates");
                for (int j = 0; j < coordinates.length(); j++) {
                    JSONArray point = coordinates.getJSONArray(j);
                    double longitude = point.getDouble(0);
                    double latitude = point.getDouble(1);
                    points.add(new PointGpsCoord(longitude, latitude));
                }
                
                double distance = feature.getJSONObject("properties").getJSONObject("summary").getDouble("distance");
                System.out.println("Длина трека: " + distance); //
                
                routeResponse.setTrack(points);
                routeResponse.setDistance(distance);
                
            }
        } catch (JSONException e) {
            e.printStackTrace(); 
        }
        
        return routeResponse;
    }
	
}
