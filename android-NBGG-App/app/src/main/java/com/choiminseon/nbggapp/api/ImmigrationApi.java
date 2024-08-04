package com.choiminseon.nbggapp.api;

import com.choiminseon.nbggapp.model.QuestionAnswerRes;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ImmigrationApi {
    // 입국 심사 질문 답변 리스트 가져오는 API
    @GET("/immigration")
    Call<QuestionAnswerRes> getQuestionAnswerList();
}
