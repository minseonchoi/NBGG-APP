package com.choiminseon.nbggapp.api;

import com.choiminseon.nbggapp.model.CountryBasicInformationRes;
import com.choiminseon.nbggapp.model.CountryRes;
import com.choiminseon.nbggapp.model.EntryPermitRes;
import com.choiminseon.nbggapp.model.LocalContactRes;
import com.choiminseon.nbggapp.model.Request;
import com.choiminseon.nbggapp.model.TravelAlarmRes;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface CountryApi {

    // 국가 검색창에 표시될 국가 리스트 불러오는 API
    @GET("/country")
    Call<CountryRes> getCountryList();

    // 국가 기본정보 API
    @POST("/country/basicInformation")
    Call<CountryBasicInformationRes> getBasicInformation(@Body Request request);

    // 여행 경보 API
    @POST("/country/travelAlarm")
    Call<TravelAlarmRes> getTravelAlarm(@Body Request request);

    // 현지 연락처 API
    @POST("country/contack")
    Call<LocalContactRes> getLocalContact(@Body Request request);

    // 입국 허가요건 가져오는 API
    @POST("country/entry")
    Call<EntryPermitRes> getEntryPermit(@Body Request request);

}
