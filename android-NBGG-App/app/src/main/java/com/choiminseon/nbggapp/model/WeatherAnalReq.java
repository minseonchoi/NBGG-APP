package com.choiminseon.nbggapp.model;

public class WeatherAnalReq {

    public String currentDescription;
    public double currentTemp;
    public int currentHumidity;
    public int currentClouds;
    public String searchDescription;
    public double searchTemp;
    public int searchHumidity;
    public int searchClouds;


    public WeatherAnalReq(String currentDescription, double currentTemp, int currentHumidity, int currentClouds, String searchDescription, double searchTemp, int searchHumidity, int searchClouds) {
        this.currentDescription = currentDescription;
        this.currentTemp = currentTemp;
        this.currentHumidity = currentHumidity;
        this.currentClouds = currentClouds;
        this.searchDescription = searchDescription;
        this.searchTemp = searchTemp;
        this.searchHumidity = searchHumidity;
        this.searchClouds = searchClouds;
    }
}
