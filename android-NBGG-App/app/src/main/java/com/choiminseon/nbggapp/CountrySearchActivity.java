package com.choiminseon.nbggapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.choiminseon.nbggapp.adapter.CountrySearchAdapter;
import com.choiminseon.nbggapp.api.CountryApi;
import com.choiminseon.nbggapp.api.NetworkClient;
import com.choiminseon.nbggapp.model.Countries;
import com.choiminseon.nbggapp.model.CountryRes;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class CountrySearchActivity extends AppCompatActivity {

    ImageView imgSearch;
    EditText editCountry;
    RecyclerView recyclerView;
    CountrySearchAdapter adapter;
    ArrayList<Countries> countriesArrayList = new ArrayList<>();
    ArrayList<Countries> filteredCountriesArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country_search);

        imgSearch = findViewById(R.id.imgSearch);
        editCountry = findViewById(R.id.editCountry);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(CountrySearchActivity.this));

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

        adapter = new CountrySearchAdapter(CountrySearchActivity.this, filteredCountriesArrayList);
        recyclerView.setAdapter(adapter);

        editCountry.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterCountries(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        getCountryList();

    }

    private void getCountryList() {
        Retrofit retrofit = NetworkClient.getRetrofitClient(CountrySearchActivity.this);
        CountryApi api = retrofit.create(CountryApi.class);
        Call<CountryRes> call = api.getCountryList();
        call.enqueue(new Callback<CountryRes>() {
            @Override
            public void onResponse(Call<CountryRes> call, Response<CountryRes> response) {
                if (response.isSuccessful()) {
                    CountryRes countryRes = response.body();
                    countriesArrayList.addAll(countryRes.countries);
                    filteredCountriesArrayList.addAll(countryRes.countries);
                    adapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onFailure(Call<CountryRes> call, Throwable throwable) {
                Toast.makeText(CountrySearchActivity.this, "네트워크 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void filterCountries(String query) {
        filteredCountriesArrayList.clear();
        if (query.isEmpty()) {
            filteredCountriesArrayList.addAll(countriesArrayList);
        } else {
            for (Countries country : countriesArrayList) {
                if (country.koreanName.toLowerCase().contains(query.toLowerCase()) ||
                        country.englishName.toLowerCase().contains(query.toLowerCase())) {
                    filteredCountriesArrayList.add(country);
                }
            }
        }
        adapter.notifyDataSetChanged();
    }

}