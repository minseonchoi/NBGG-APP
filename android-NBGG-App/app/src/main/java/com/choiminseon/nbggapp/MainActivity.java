package com.choiminseon.nbggapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.choiminseon.nbggapp.api.NetworkClient;
import com.choiminseon.nbggapp.api.WeatherApi;
import com.choiminseon.nbggapp.model.WeatherAnalReq;
import com.choiminseon.nbggapp.model.WeatherAnalRes;
import com.choiminseon.nbggapp.model.WeatherReq;
import com.choiminseon.nbggapp.model.WeatherRes;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    Button btnCountryInfo;
    Button btnQnA;
    Button btnTranslate;
    ImageView imgWeather;
    TextView txtTemperature;
    TextView txtMaxTemperature;
    TextView txtMinTemperature;
    TextView txtPrecipitationProbability;
    TextView txtHumidity;
    double latitude;
    double longitude;
    TextView txtFeelsLike;
    TextView txtSearchCountry;
    ImageView imgSearchWeather;
    TextView txtSearchTemperature;
    TextView txtSearchMaxTemperature;
    TextView txtSearchMinTemperature;
    TextView txtSearchPrecipitationProbability;
    TextView txtSearchHumidity;
    TextView txtSearchFeelsLike;
    TextView txtAnalyzeWeather;

    // MapSearchActivity 에서 받는 데이터
    Double lat;
    Double lng;
    String addressLine;

    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private FusedLocationProviderClient fusedLocationClient;

    LottieAnimationView animation;
    LinearLayout layoutWeatherInfo;
    CardView cardViewSearchWeather;
    ImageView imgSearch;
    LottieAnimationView weatherSearchAnimation;
    LinearLayout layoutSearchWeatherInfo;

    // 날씨 분석 비교를 위한 멤버 변수
    String currentDescription;
    double currentTemp;
    int currentClouds;
    int currentHumidity;
    String searchDescription;
    double searchTemp;
    int searchClouds;
    int searchHumidity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        Intent intent = getIntent();
        if (intent != null) {
            lat = intent.getDoubleExtra("lat", 0.0);
            lng = intent.getDoubleExtra("lng", 0.0);
            addressLine = intent.getStringExtra("addressLine");
        }

        btnCountryInfo = findViewById(R.id.btnCountryInfo);
        btnQnA = findViewById(R.id.btnQnA);
        btnTranslate = findViewById(R.id.btnTranslate);
        imgWeather = findViewById(R.id.imgWeather);
        txtTemperature = findViewById(R.id.txtTemperature);
        txtMaxTemperature = findViewById(R.id.txtMaxTemperature);
        txtMinTemperature = findViewById(R.id.txtMinTemperature);
        txtPrecipitationProbability = findViewById(R.id.txtPrecipitationProbability);
        txtHumidity = findViewById(R.id.txtHumidity);
        txtFeelsLike = findViewById(R.id.txtFeelsLike);
        animation = findViewById(R.id.otherMicAnimation);
        layoutWeatherInfo = findViewById(R.id.layoutWeatherInfo);
        txtSearchCountry = findViewById(R.id.txtSearchCountry);
        imgSearchWeather = findViewById(R.id.imgSearchWeather);
        txtSearchTemperature = findViewById(R.id.txtSearchTemperature);
        txtSearchMaxTemperature = findViewById(R.id.txtSearchMaxTemperature);
        txtSearchMinTemperature = findViewById(R.id.txtSearchMinTemperature);
        txtSearchPrecipitationProbability = findViewById(R.id.txtSearchPrecipitationProbability);
        txtSearchHumidity = findViewById(R.id.txtSearchHumidity);
        txtSearchFeelsLike = findViewById(R.id.txtSearchFeelsLike);
        cardViewSearchWeather = findViewById(R.id.cardViewSearchWeather);
        imgSearch = findViewById(R.id.imgSearch);
        weatherSearchAnimation = findViewById(R.id.weatherSearchAnimation);
        layoutSearchWeatherInfo = findViewById(R.id.layoutSearchWeatherInfo);
        txtAnalyzeWeather = findViewById(R.id.txtAnalyzeWeather);


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if (EasyPermissions.hasPermissions(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            getLocation();
        } else {
            EasyPermissions.requestPermissions(
                    this,
                    "위치 권한 허용이 필요합니다.",
                    REQUEST_LOCATION_PERMISSION,
                    Manifest.permission.ACCESS_FINE_LOCATION);
        }

        btnCountryInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CountrySearchActivity.class);
                startActivity(intent);
            }
        });


        btnQnA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ImmigrationInspectionActivity.class);
                startActivity(intent);
            }
        });


        btnTranslate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DialogueTranslationActivity.class);
                startActivity(intent);
            }
        });


        txtSearchCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MapSearchActivity.class);
                startActivity(intent);
            }
        });

        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MapSearchActivity.class);
                startActivity(intent);

            }
        });


        if (lat != 0.0 && lng != 0.0) {
            getSearchWeatherInfo();
            weatherSearchAnimation.setVisibility(View.GONE);
            layoutSearchWeatherInfo.setVisibility(View.VISIBLE);
        }


    }

    private void getSearchWeatherInfo() {
        Retrofit retrofit = NetworkClient.getRetrofitClient(MainActivity.this);
        WeatherApi api = retrofit.create(WeatherApi.class);
        WeatherReq weatherReq = new WeatherReq(lat, lng);
        Call<WeatherRes> call = api.getWeatherInfo(weatherReq);
        call.enqueue(new Callback<WeatherRes>() {
            @Override
            public void onResponse(Call<WeatherRes> call, Response<WeatherRes> response) {
                if (response.isSuccessful()) {
                    WeatherRes weatherRes = response.body();
                    Glide.with(MainActivity.this).load(weatherRes.icon).into(imgSearchWeather);

                    txtSearchTemperature.setText((int)weatherRes.temp + "º");
                    txtSearchMaxTemperature.setText((int)weatherRes.tempMax + "º");
                    txtSearchMinTemperature.setText((int)weatherRes.tempMin + "º");
                    txtSearchFeelsLike.setText("체감온도: " + (int)weatherRes.feelsLike);
                    txtSearchPrecipitationProbability.setText("강수확률: " + weatherRes.clouds + "%");
                    txtSearchHumidity.setText("습도: " + weatherRes.humidity + "%");

                    searchDescription = weatherRes.description;
                    searchTemp = weatherRes.temp;
                    searchClouds = weatherRes.clouds;
                    searchHumidity = weatherRes.humidity;
                    Log.i("SEARCH DESCRIPTION", searchDescription);
                    Log.i("SEARCH TEMP", searchTemp + "");
                    Log.i("SEARCH CLOUDS", searchClouds + "");
                    Log.i("SEARCH HUMIDITY", searchHumidity + "");


                }
            }

            @Override
            public void onFailure(Call<WeatherRes> call, Throwable throwable) {
                Toast.makeText(MainActivity.this, "네트워크 연결을 확인하세요.", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void getWeatherInfo() {

        Retrofit retrofit = NetworkClient.getRetrofitClient(MainActivity.this);
        WeatherApi api = retrofit.create(WeatherApi.class);
        WeatherReq weatherReq = new WeatherReq(latitude, longitude);

        Call<WeatherRes> call = api.getWeatherInfo(weatherReq);
        call.enqueue(new Callback<WeatherRes>() {
            @Override
            public void onResponse(Call<WeatherRes> call, Response<WeatherRes> response) {
                if (response.isSuccessful()) {
                    WeatherRes weatherRes = response.body();
                    Glide.with(MainActivity.this).load(weatherRes.icon).into(imgWeather);

                    txtTemperature.setText((int)weatherRes.temp + "º");
                    txtMaxTemperature.setText((int)weatherRes.tempMax + "º");
                    txtMinTemperature.setText((int)weatherRes.tempMin + "º");
                    txtFeelsLike.setText("체감온도: " + (int)weatherRes.feelsLike);
                    txtPrecipitationProbability.setText("강수확률: " + weatherRes.clouds + "%");
                    txtHumidity.setText("습도: " + weatherRes.humidity + "%");

                    currentDescription = weatherRes.description;
                    currentTemp = weatherRes.temp;
                    currentClouds = weatherRes.clouds;
                    currentHumidity = weatherRes.humidity;

                    Log.i("CURRENT DESCRIPTION", currentDescription);
                    Log.i("CURRENT TEMP", currentTemp + "");
                    Log.i("CURRENT CLOUDS", currentClouds + "");
                    Log.i("CURRENT HUMIDITY", currentHumidity + "");

                    getWeatherAnalyzeInfo();

                    animation.setVisibility(View.GONE);
                    layoutWeatherInfo.setVisibility(View.VISIBLE);
                } else {
                    animation.setVisibility(View.GONE);
                    Toast.makeText(MainActivity.this, "날씨 정보를 가져올 수 없습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<WeatherRes> call, Throwable throwable) {
                animation.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, "네트워크 오류", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void getLocation() {
        animation.setVisibility(View.VISIBLE);
        layoutWeatherInfo.setVisibility(View.GONE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            Location location = task.getResult();
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                            Log.i("MAIN LAT", latitude + "");
                            Log.i("MAIN LON", longitude + "");
                            getWeatherInfo();
                        } else {
                            Toast.makeText(MainActivity.this, "Unable to get location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void getWeatherAnalyzeInfo() {

        Retrofit retrofit = NetworkClient.getRetrofitClient(MainActivity.this);
        WeatherApi api = retrofit.create(WeatherApi.class);
        WeatherAnalReq weatherAnalReq = new WeatherAnalReq(currentDescription, currentTemp, currentHumidity, currentClouds, searchDescription, searchTemp, searchHumidity, searchClouds);

        Call<WeatherAnalRes> call = api.getWeatherAnalyzeInfo(weatherAnalReq);

        call.enqueue(new Callback<WeatherAnalRes>() {
            @Override
            public void onResponse(Call<WeatherAnalRes> call, Response<WeatherAnalRes> response) {
                if (response.isSuccessful()) {
                    WeatherAnalRes weatherAnalRes = response.body();
                    if (weatherAnalRes != null) {
                        txtAnalyzeWeather.setText(weatherAnalRes.response);
                    } else {
                        Toast.makeText(MainActivity.this, "빈 응답을 받았습니다.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // 에러 코드와 메시지 확인
                    String errorMessage = "에러 코드: " + response.code();
                    try {
                        String errorBody = response.errorBody() != null ? response.errorBody().string() : "에러 본문 없음";
                        errorMessage += "\n에러 본문: " + errorBody;
                    } catch (IOException e) {
                        errorMessage += "\n에러 본문을 읽는 도중 오류 발생: " + e.getMessage();
                    }
                }
            }

            @Override
            public void onFailure(Call<WeatherAnalRes> call, Throwable throwable) {

                Toast.makeText(MainActivity.this, "네트워크 오류", Toast.LENGTH_SHORT).show();

            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            getLocation();
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        Toast.makeText(this, "위치 권한 거부", Toast.LENGTH_SHORT).show();
    }
}