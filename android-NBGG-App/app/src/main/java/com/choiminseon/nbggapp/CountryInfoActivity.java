package com.choiminseon.nbggapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.tabs.TabLayout;

public class CountryInfoActivity extends AppCompatActivity {

    TabLayout tabLayout;
    FrameLayout tabContentFrame;
    TextView txtCountryName;
    ImageView imgSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country_info);

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
        String countryName = intent.getStringExtra("countryName");
        String flagImageUrl = intent.getStringExtra("flagImageUrl");
        Log.i("COUNTRY NAME", countryName);
        Log.i("FLAG IMAGE URL", flagImageUrl);

        tabLayout = findViewById(R.id.tabLayout);
        tabContentFrame = findViewById(R.id.tabContentFrame);
        txtCountryName = findViewById(R.id.txtCountryKorName);
        imgSearch = findViewById(R.id.imgSearch);

        txtCountryName.setText(countryName);
        txtCountryName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(CountryInfoActivity.this, CountrySearchActivity.class);
                startActivity(intent1);
                finish();
            }
        });

        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(CountryInfoActivity.this, CountrySearchActivity.class);
                startActivity(intent1);
                finish();
            }
        });


        Bundle bundle = new Bundle();
        bundle.putString("countryName", countryName);
        bundle.putString("flagImageUrl", flagImageUrl);

        BasicInfoFragment basicInfoFragment = new BasicInfoFragment();
        basicInfoFragment.setArguments(bundle);
        replaceFragment(basicInfoFragment);


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Fragment selectedFragment = null;
                switch (tab.getPosition()) {
                    case 0:
                        selectedFragment = new BasicInfoFragment();
                        break;
                    case 1:
                        selectedFragment = new TravelAlertFragment();
                        break;
                    case 2:
                        selectedFragment = new EntryPermitFragment();
                        break;
                    case 3:
                        selectedFragment = new LocalContactFragment();
                        break;
                }

                if (selectedFragment != null) {
                    selectedFragment.setArguments(bundle);
                    replaceFragment(selectedFragment);
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        tabLayout.getTabAt(0).select();

    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.tabContentFrame, fragment);
        transaction.commit();
    }

}