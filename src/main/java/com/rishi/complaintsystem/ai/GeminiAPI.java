package com.rishi.complaintsystem.ai;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.rishi.complaintsystem.utils.ConfigReader;
import okhttp3.*;

import java.io.IOException;

public class GeminiAPI {
    private static final String API_KEY = ConfigReader.getValue("gemini.api.key");
    private static final String URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=" + API_KEY;
    OkHttpClient client = new OkHttpClient();
    Gson gson = new Gson();

    public String getSolution(String problem){
        JsonObject part = new JsonObject();


        String prompt = "You are an assistant helping users file public complaints. Given the following complaint text: \n " +
                        problem + "\n" +
                        "Return a suggestion that helps the user stay informed or safe while the issue is handled by the authorities. Don't tell them to fix the issue themselves.\n" +
                        "Based on this, provide the user what things he/she can do  that helps the user stay safe or cautious until the issue is resolved";
        part.addProperty("text", prompt);

        JsonObject content = new JsonObject();
        JsonArray partsArray = new JsonArray();
        partsArray.add(part);
        content.add("parts",partsArray);

        JsonObject contentsObj = new JsonObject();
        JsonArray contentsArray = new JsonArray();
        contentsArray.add(content);
        contentsObj.add("contents",contentsArray);

        RequestBody body = RequestBody.create(contentsObj.toString(), MediaType.get("application/json"));

        Request request = new Request.Builder()
                .url(URL)
                .post(body)
                .build();

        try(Response response = client.newCall(request).execute()) {
            String responseBody = response.body().string();
            return parseResponse(responseBody);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    private String parseResponse(String responseBody) {
        JsonObject obj = gson.fromJson(responseBody, JsonObject.class);
        return obj.getAsJsonArray("candidates")
                .get(0).getAsJsonObject()
                .getAsJsonObject("content")
                .getAsJsonArray("parts")
                .get(0).getAsJsonObject()
                .get("text").getAsString();
    }

    // for testing purpose
//    public static void main(String[] args) {
//        GeminiAPI api = new GeminiAPI();
//        String problem = " Our Locality is facing network connectivity issues";
//        String solution = api.getSolution(problem);
//        System.out.println("AI Suggestion: " + solution);
//    }
}
