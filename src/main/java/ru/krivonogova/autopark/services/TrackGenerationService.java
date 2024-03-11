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

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.krivonogova.autopark.dto.PointGpsCoord;

@Service
@Transactional(readOnly = true)
public class TrackGenerationService {

	private final String openRouteUrl = "https://api.openrouteservice.org/v2/directions/driving-car?api_key=5b3ce3597851110001cf624881622e92681c4a87909ab330c6345a1e";
	
	public void generateTrack() {
		
		// test data
		double startLong = 50.170580; 
        double startLat = 53.212259;
        double endLong = 50.179861;
        double endLat = 53.212779;
		
		List<PointGpsCoord> track = getRouting(startLong, startLat, endLong, endLat);
		
		
		
	}
	
	public List<PointGpsCoord> getRouting(double startLong, double startLat, double endLong, double endLat) {
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
                String jsonResponse = EntityUtils.toString(entity, "UTF-8"); // Преобразуйте содержимое сущности в строку
                points = parseResponse(jsonResponse);

                for (PointGpsCoord point : points) {
                    System.out.println("Point: " + point.getLongitude() + ", " + point.getLatitude());
                }
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
            }
        } catch (JSONException e) {
            e.printStackTrace();
            // обработка ошибки чтения или парсинга JSON
        }
        return points;
    }
	
}
