package com.choiminseon.nbggapp.api;

import com.choiminseon.nbggapp.model.TranslateReq;
import com.choiminseon.nbggapp.model.TranslateRes;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface TranslateApi {

    // 한국어 -> 외국어
    @POST("/translate/aws")
    Call<TranslateRes> translateAnswer(@Body TranslateReq translateReq);

    // 외국어 -> 한국어
    @POST("/translate/foreign/aws")
    Call<TranslateRes> translateForeignAnswer(@Body TranslateReq translateReq);
}
