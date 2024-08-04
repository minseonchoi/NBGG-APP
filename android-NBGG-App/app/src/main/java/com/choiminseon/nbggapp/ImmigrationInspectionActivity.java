package com.choiminseon.nbggapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.choiminseon.nbggapp.adapter.ImmigrationAdapter;
import com.choiminseon.nbggapp.api.ImmigrationApi;
import com.choiminseon.nbggapp.api.NetworkClient;
import com.choiminseon.nbggapp.model.QuestionAnswer;
import com.choiminseon.nbggapp.model.QuestionAnswerRes;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class ImmigrationInspectionActivity extends AppCompatActivity {
    Button btnCreateAnswer;
    RecyclerView recyclerView;
    ArrayList<QuestionAnswer> questionAnswerArrayList = new ArrayList<>();
    ImmigrationAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_immigration_inspection);

        // 기본 액션바 설정
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setCustomView(R.layout.custom_action_bar);
            actionBar.setElevation(0);

            // customView 가져오기
            View customView = actionBar.getCustomView();

            // 날보고가 누르면 메인으로 이동
            TextView actionBarTitle = customView.findViewById(R.id.actionBarTitle);
            actionBarTitle.setOnClickListener(v -> {
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            });
        }

        btnCreateAnswer = findViewById(R.id.btnCreateAnswer);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(ImmigrationInspectionActivity.this));

        getQuestionAnswerList();

        btnCreateAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ImmigrationInspectionActivity.this, CreatedAnswerActivity.class);
                startActivity(intent);
            }
        });




    }

    private void getQuestionAnswerList() {
        Retrofit retrofit = NetworkClient.getRetrofitClient(ImmigrationInspectionActivity.this);
        ImmigrationApi api = retrofit.create(ImmigrationApi.class);
        Call<QuestionAnswerRes> call = api.getQuestionAnswerList();
        call.enqueue(new Callback<QuestionAnswerRes>() {
            @Override
            public void onResponse(Call<QuestionAnswerRes> call, Response<QuestionAnswerRes> response) {
                if (response.isSuccessful()) {
                    QuestionAnswerRes questionAnswerRes = response.body();
                    questionAnswerArrayList.addAll(questionAnswerRes.questionAnswer);
                    adapter = new ImmigrationAdapter(ImmigrationInspectionActivity.this, questionAnswerArrayList);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<QuestionAnswerRes> call, Throwable throwable) {
                Toast.makeText(ImmigrationInspectionActivity.this, "네트워크 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
            }
        });


    }
}