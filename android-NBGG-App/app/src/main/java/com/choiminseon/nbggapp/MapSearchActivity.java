package com.choiminseon.nbggapp;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class MapSearchActivity extends AppCompatActivity implements OnMapReadyCallback  {

    GoogleMap mMap;
    EditText editSearchCountry;
    Button btnSearch;
    TextView txtLocation;
    Button btnIntent;

    Double lat;
    Double lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_search);

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

        editSearchCountry = findViewById(R.id.txtSearchCountry);
        btnSearch = findViewById(R.id.btnSearch);
        txtLocation = findViewById(R.id.txtLocation);
        btnIntent = findViewById(R.id.btnIntent);

        // 지도 초기화
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync((OnMapReadyCallback) MapSearchActivity.this);
        }

        // 검색 버튼 클릭 리스너
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchText = editSearchCountry.getText().toString().trim();
                if (!searchText.isEmpty()) {
                    searchLocation(searchText);
                } else {
                    Toast.makeText(MapSearchActivity.this, "검색어를 입력하세요", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        // 지도를 클릭하여 위치 선택
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(latLng).title("선택한 위치"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                txtLocation.setText("선택한 위도 : "+ latLng.latitude + ", 경도: " + latLng.longitude);

                lat = latLng.latitude;
                lng = latLng.longitude;

                btnIntent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MapSearchActivity.this, MainActivity.class);
                        intent.putExtra("lat",lat);
                        intent.putExtra("lng",lng);
                        startActivity(intent);

                        finish();
                    }
                });
            }
        });
    }
    private void searchLocation(String searchText) {
        Geocoder geocoder = new Geocoder(this);
        List<Address> addresses;

        try {
            addresses = geocoder.getFromLocationName(searchText, 3);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);

                String addressLine = address.getAddressLine(0);

                txtLocation.setText("선택한 지역 : "+ addressLine);
                LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());

                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(latLng).title(address.getAddressLine(0)));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(15));

                lat = address.getLatitude();
                lng = address.getLongitude();

                btnIntent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MapSearchActivity.this, MainActivity.class);
                        intent.putExtra("lat",lat);
                        intent.putExtra("lng",lng);
                        intent.putExtra("addressLine", addressLine);
                        startActivity(intent);

                        finish();
                    }
                });

            } else {
                Toast.makeText(this, "검색 결과가 없습니다", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "검색 중 오류가 발생했습니다", Toast.LENGTH_SHORT).show();
        }
    }

}