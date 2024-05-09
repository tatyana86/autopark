package ru.krivonogova.autopark.services;

import java.util.List;
import java.util.Optional;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.krivonogova.autopark.models.PointGps;
import ru.krivonogova.autopark.models.Trip;
import ru.krivonogova.autopark.repositories.PointsGpsRepository;

@Service
@Transactional(readOnly = true)
public class PointsGpsService {

	private final PointsGpsRepository pointsGpsRepository;

	@Autowired
	public PointsGpsService(PointsGpsRepository pointsGpsRepository) {
		this.pointsGpsRepository = pointsGpsRepository;
	}
	
	private final String openRouteUrl = "https://api.openrouteservice.org/geocode/reverse?api_key=5b3ce3597851110001cf624881622e92681c4a87909ab330c6345a1e";
	
	//+
	public List<PointGps> findAll() {
		return pointsGpsRepository.findAll();
	}
	
	//+
	public List<PointGps> findAllByVehicleAndTimePeriod(int vehicleId, String dateFrom, String dateTo) {
		return pointsGpsRepository.findAllByVehicleAndTimePeriod(vehicleId, dateFrom, dateTo);
	}
	
	public List<PointGps> findAllByVehicleAndTrip(int vehicleId, List<Trip> trips) {
		
		String dateFrom_upd = trips.get(0).getTimeOfStart();
		String dateTo_upd = trips.get(trips.size() - 1).getTimeOfEnd();
		
		return pointsGpsRepository.findAllByVehicleAndTimePeriod(vehicleId, dateFrom_upd, dateTo_upd);
	}
	

	
    public Optional<PointGps> findById(int id) {
        return pointsGpsRepository.findById(id);
    }
    
    //+
    public Optional<PointGps> findByVehicleAndTime(int vehicleId, String time) {
        return pointsGpsRepository.findFirstByVehicleIdAndTimeOfPointGps(vehicleId, time);
    }
        
    //+
    public String takeAddressOfPointGPS(PointGps pointGps) {
    	
    	String addressOfPointGPS = "";
    	
    	double longitude = pointGps.getLongitude(); 
        double latitude = pointGps.getLatitude();
        
        String requestUrl = openRouteUrl + "&point.lon=" + longitude + "&point.lat=" + latitude;
        
        try {
            HttpClient httpClient = HttpClients.createDefault();
            HttpGet request = new HttpGet(requestUrl);
            request.addHeader("Content-Type", "application/json");

            HttpResponse response = httpClient.execute(request);
            
            //System.out.println(requestUrl);
            
            if (response.getStatusLine().getStatusCode() == 200) {
            	HttpEntity entity = response.getEntity();
                String jsonResponse = EntityUtils.toString(entity, "UTF-8"); // Преобразуйте содержимое сущности в строку
                addressOfPointGPS = parseResponse(jsonResponse);

            } else {
                System.out.println("GET request failed");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return addressOfPointGPS;
    }
    
	private static String parseResponse(String jsonResponse) {
		String addressOfPointGPS = "";
		
        try {
            JSONObject responseObject = new JSONObject(jsonResponse);
            JSONArray features = responseObject.getJSONArray("features");
            JSONObject feature = features.getJSONObject(0);
            JSONObject properties = feature.getJSONObject("properties");
            addressOfPointGPS = properties.getString("label");
            //System.out.println(addressOfPointGPS);
          
        } catch (JSONException e) {
            e.printStackTrace(); 
        }
        return addressOfPointGPS;
    }
    
    @Transactional
    public void saveAll(List<PointGps> pointsGps) {
    	pointsGpsRepository.saveAll(pointsGps);
    }
    
    @Transactional
    public void save(PointGps pointsGps) {
    	pointsGpsRepository.save(pointsGps);
    }

}
