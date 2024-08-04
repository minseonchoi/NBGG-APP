package com.choiminseon.nbggapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.choiminseon.nbggapp.api.CountryApi;
import com.choiminseon.nbggapp.api.NetworkClient;
import com.choiminseon.nbggapp.model.Request;
import com.choiminseon.nbggapp.model.TravelAlarm;

import com.choiminseon.nbggapp.model.TravelAlarmRes;



import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TravelAlertFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TravelAlertFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TravelAlertFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment safetyServiceFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TravelAlertFragment newInstance(String param1, String param2) {
        TravelAlertFragment fragment = new TravelAlertFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    String countryName;
    TextView txtCountryKorName;
    TextView txtCountryEngName;
    TextView txtContinent;
    ImageView imgCountryFlag;
    ImageView imgDangMap;
    ImageView imgMap;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_travel_alert, container, false);

        txtCountryKorName = view.findViewById(R.id.txtCountryKorName);
        txtCountryEngName = view.findViewById(R.id.txtCountryEngName);
        txtContinent = view.findViewById(R.id.txtContinent);
        imgCountryFlag = view.findViewById(R.id.imgCountryFlag);
        imgDangMap = view.findViewById(R.id.imgDangMap);
        imgMap = view.findViewById(R.id.imgMap);

        countryName = this.getArguments().getString("countryName");
        Log.i("TRAVEL ALERT FRAGMENT", countryName);

        Retrofit retrofit = NetworkClient.getRetrofitClient(getActivity());
        CountryApi api = retrofit.create(CountryApi.class);

        Request request = new Request(countryName);

        Call<TravelAlarmRes> call = api.getTravelAlarm(request);
        call.enqueue(new Callback<TravelAlarmRes>() {
            @Override
            public void onResponse(Call<TravelAlarmRes> call, Response<TravelAlarmRes> response) {
                if (response.isSuccessful()) {
                    TravelAlarmRes travelAlarmRes = response.body();
                    TravelAlarm travelAlarm = travelAlarmRes.travelAlarm;

                    txtCountryKorName.setText(travelAlarm.countryName);
                    txtCountryEngName.setText("(" + travelAlarm.countryEngName + ")");
                    txtContinent.setText(travelAlarm.continentName);
                    Glide.with(getActivity()).load(travelAlarm.flagUrl).into(imgCountryFlag);
                    Glide.with(getActivity()).load(travelAlarm.dangMapUrl).into(imgDangMap);
                    Glide.with(getActivity()).load(travelAlarm.mapUrl).into(imgMap);


                } else {
                    Log.e("RetrofitError", "Request not successful. Code: " + response.code());
                    Toast.makeText(getActivity(), "응답 실패: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<TravelAlarmRes> call, Throwable throwable) {
                Toast.makeText(getActivity(), "네트워크 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
                Log.e("RetrofitError", "Request Failed", throwable);
            }
        });


        return view;
    }
}