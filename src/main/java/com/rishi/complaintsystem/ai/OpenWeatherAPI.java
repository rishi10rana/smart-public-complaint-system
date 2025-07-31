package com.rishi.complaintsystem.ai;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.rishi.complaintsystem.models.WeatherResult;
import com.rishi.complaintsystem.utils.ConfigReader;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class OpenWeatherAPI {
    private static final String API_KEY = ConfigReader.getValue("openweather.api.key");

    public static WeatherResult getCurrentWeather(String city) throws IOException {

        String URL = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + API_KEY;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(URL).build();

        try{
            Response response = client.newCall(request).execute();
            if(!response.isSuccessful()){
                System.out.println("Failed to fetch weather: " + response);
                throw new IOException("Unexpected Response: " + response);
            }

            JsonObject json = JsonParser.parseString(response.body().string()).getAsJsonObject();

            String description = json.getAsJsonArray("weather").get(0).getAsJsonObject().get("description").getAsString();
            double temp = json.getAsJsonObject("main").get("temp").getAsDouble();
            return new WeatherResult(description, temp);
        }catch(IOException e){
            e.printStackTrace();
            return null;
        }
    }

    // Testing
//    public static void main(String[] args) {
//        String condition = OpenWeatherAPI.getCurrentWeather("Dehradun").trim();
//
//        if(condition.equalsIgnoreCase("Clear") || condition.equalsIgnoreCase("Clouds")){
//            System.out.println("We can do outside work." + condition);
//        }
//        else{
//            System.out.println("weather is not suitable for outside work" + condition);
//        }
//    }
}
