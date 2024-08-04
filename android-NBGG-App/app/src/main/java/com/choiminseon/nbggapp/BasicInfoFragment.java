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
import com.choiminseon.nbggapp.model.CountryBasicInformationRes;
import com.choiminseon.nbggapp.model.Request;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BasicInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BasicInfoFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public BasicInfoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BasicInfoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BasicInfoFragment newInstance(String param1, String param2) {
        BasicInfoFragment fragment = new BasicInfoFragment();
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

    TextView txtCountryInfo1;
    TextView txtCountryInfo2;
    ImageView imgCountryFlag;
    String countryName;
    String flagImageUrl;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_basic_info, container, false);

        txtCountryInfo1 = view.findViewById(R.id.txtCountryInfo1);
        txtCountryInfo2 = view.findViewById(R.id.txtCountryInfo2);
        imgCountryFlag = view.findViewById(R.id.imgCountryFlag);

        countryName = this.getArguments().getString("countryName");
        flagImageUrl = this.getArguments().getString("flagImageUrl");

        Log.i("FRAGMENT COUNTRY NAME", countryName);
        Log.i("FRAGMENT FLAG IMAGE URL", flagImageUrl);

        Retrofit retrofit = NetworkClient.getRetrofitClient(getActivity());
        CountryApi api = retrofit.create(CountryApi.class);
        Request request = new Request(countryName);
        Call<CountryBasicInformationRes> call = api.getBasicInformation(request);
        call.enqueue(new Callback<CountryBasicInformationRes>() {
            @Override
            public void onResponse(Call<CountryBasicInformationRes> call, Response<CountryBasicInformationRes> response) {
                if (response.isSuccessful()) {
                    CountryBasicInformationRes countryBasicInformationRes = response.body();

                    String countryBasicInformation = countryBasicInformationRes.countryBasicInformation;

                    // 첫 번째 \n의 인덱스 찾기
                    int firstIndex = countryBasicInformation.indexOf('\n');
                    // 첫 번째 \n 이후 두 번째 \n의 인덱스 찾기
                    int secondIndex = countryBasicInformation.indexOf('\n', firstIndex + 1);

                    if (secondIndex != -1) {
                        String countryBasicInformation1 = countryBasicInformation.substring(0, secondIndex);
                        String countryBasicInformation2 = countryBasicInformation.substring(secondIndex + 1);
                        txtCountryInfo1.setText(countryBasicInformation1);
                        txtCountryInfo2.setText(countryBasicInformation2);
                        Glide.with(getActivity()).load(flagImageUrl).into(imgCountryFlag);
                    }
                }
            }

            @Override
            public void onFailure(Call<CountryBasicInformationRes> call, Throwable throwable) {
                Toast.makeText(getActivity(), "네트워크 연결을 확인하세요.", Toast.LENGTH_SHORT).show();
            }
        });


        return view;
    }
}