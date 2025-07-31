package com.rishi.complaintsystem.models;

public class WeatherResult {
    private String description;
    private double temp;

    public WeatherResult(String description, double temp){
        this.description = description;
        this.temp = temp;
    }

    public String getDescription(){
        return this.description;
    }

    public double getTemp(){
        return  this.temp;
    }
}
