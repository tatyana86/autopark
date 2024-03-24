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
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.krivonogova.autopark.dto.PointGpsCoord;
import ru.krivonogova.autopark.dto.TrackGenerationDTO;
import ru.krivonogova.autopark.models.PointGps;
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

	double centerLongitude = 50.157566;
    double centerLatitude = 53.228851;
    double radius = 4; // в км
    
	private final String openRouteUrl = "https://api.openrouteservice.org/v2/directions/driving-car?api_key=5b3ce3597851110001cf624881622e92681c4a87909ab330c6345a1e";
	
	@Transactional
	public void generateTrack(TrackGenerationDTO request) {
		
		// test data
		/*double startLong = 50.170580; 
        double startLat = 53.212259;
        double endLong = 50.179861;
        double endLat = 53.212779;*/
		
		int lengthOfTrack = request.getLengthOfTrack();
                
        PointGpsCoord start = generateStart();
        PointGpsCoord end = generateEnd(start, lengthOfTrack);
        
        double startLong = start.getLongitude(); 
        double startLat = start.getLatitude();
        double endLong = end.getLongitude();
        double endLat = end.getLatitude();
		
		List<PointGpsCoord> track = getRouting(startLong, startLat, endLong, endLat);
		
		List<PointGps> points = new ArrayList<>();
		Vehicle vehicle = vehiclesService.findOne(request.getIdVehicle());

		//блок сохранения gps-точек в таблицу track
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
                TimeUnit.SECONDS.sleep(10); // Делаем паузу в конце каждой итерации
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
		}

		// pointsGpsService.saveAll(points); // тогда будет одно время сохранения
		
		// блок сохранения отдельной поездки в таблицу trip
		String timeOfStart = points.get(0).getTimeOfPointGps();
		String timeOfEnd = points.get(points.size()-1).getTimeOfPointGps();
		double distance = getDistance(startLong, startLat, endLong, endLat);
		
		Trip trip = new Trip(vehicle, timeOfStart, timeOfEnd, distance);
		
		tripService.save(trip);
		
	}
	
	public String getTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());

        return dateFormat.format(date);
    }
	
	public PointGpsCoord generateEnd(PointGpsCoord start, int lengthOfTrack) {
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
	}
	
	public PointGpsCoord generateStart() {
		Random random = new Random();

        // Генерация случайного угла и радиуса в полярных координатах
        double angle = 2 * Math.PI * random.nextDouble();
        double r = radius * Math.sqrt(random.nextDouble());

        // Преобразование полярных координат в декартовы
        double longitude = centerLongitude + r * Math.cos(angle) / (111.32 * Math.cos(centerLatitude));
        double latitude = centerLatitude + r * Math.sin(angle) / 111.32;
        
        return new PointGpsCoord(longitude, latitude);
	}
	
	public double getDistance(double startLong, double startLat, double endLong, double endLat) {
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
                System.out.println("GET request failed");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return distance;
	}
	
	private static double parseResponseForDistance(String jsonResponse) {
		double distance = 0;
        try {
            JSONObject responseObject = new JSONObject(jsonResponse);
            JSONArray features = responseObject.getJSONArray("features");
            for (int i = 0; i < features.length(); i++) {
                JSONObject feature = features.getJSONObject(i);
                JSONObject properties = feature.getJSONObject("properties");
                JSONObject summary = feature.getJSONObject("summary");
                distance = summary.getDouble("distance");
                System.out.println("Длина трека: " + distance);          
            }
        } catch (JSONException e) {
            e.printStackTrace();
            // обработка ошибки чтения или парсинга JSON
        }
        return distance;
    }
		
	public List<PointGpsCoord> getRouting(double startLong, double startLat, double endLong, double endLat) {
		List<PointGpsCoord> points = new ArrayList<>();	
        String requestUrl = openRouteUrl + "&start=" + startLong + "," + startLat + "&end=" + endLong + "," + endLat;
        //System.out.println(requestUrl);
        
        try {
            HttpClient httpClient = HttpClients.createDefault();
            HttpGet request = new HttpGet(requestUrl);
            request.addHeader("Content-Type", "application/json");

            HttpResponse response = httpClient.execute(request);
            
            //System.out.println(response);

            if (response.getStatusLine().getStatusCode() == 200) {
            	HttpEntity entity = response.getEntity();
                String jsonResponse = EntityUtils.toString(entity, "UTF-8");
                points = parseResponse(jsonResponse);

                /*for (PointGpsCoord point : points) {
                    System.out.println("Point: " + point.getLongitude() + ", " + point.getLatitude());
                }*/
            } else {
                System.out.println("GET request failed");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return points;
	}
	
	private static List<PointGpsCoord> parseResponse(String jsonResponse) {
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
    }
	
}
