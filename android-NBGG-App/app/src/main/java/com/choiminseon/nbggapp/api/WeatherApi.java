package com.choiminseon.nbggapp.api;

import com.choiminseon.nbggapp.model.WeatherAnalReq;
import com.choiminseon.nbggapp.model.WeatherAnalRes;
import com.choiminseon.nbggapp.model.WeatherReq;
import com.choiminseon.nbggapp.model.WeatherRes;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface WeatherApi {
    @POST("/weather/location")
    Call<WeatherRes> getWeatherInfo(@Body WeatherReq weatherReq);

    @POST("/weather/gemini")
    Call<WeatherAnalRes> getWeatherAnalyzeInfo(@Body WeatherAnalReq weatherAnalReq);
}
