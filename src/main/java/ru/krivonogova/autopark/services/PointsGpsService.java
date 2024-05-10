package ru.krivonogova.autopark.services;

import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import ru.krivonogova.autopark.models.PointGps;

@Service
public class PointsGpsService {

	private final String openRouteUrl = "https://api.openrouteservice.org/geocode/reverse?api_key=5b3ce3597851110001cf624881622e92681c4a87909ab330c6345a1e";
		
	public String generateMapRequest(List<PointGps> points) {
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
    
}
