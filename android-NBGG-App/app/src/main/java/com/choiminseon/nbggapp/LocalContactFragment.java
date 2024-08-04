package com.choiminseon.nbggapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.choiminseon.nbggapp.api.CountryApi;
import com.choiminseon.nbggapp.api.NetworkClient;
import com.choiminseon.nbggapp.model.LocalContactRes;
import com.choiminseon.nbggapp.model.Request;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LocalContactFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LocalContactFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public LocalContactFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LocalContactFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LocalContactFragment newInstance(String param1, String param2) {
        LocalContactFragment fragment = new LocalContactFragment();
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

    TextView txtLocalContact;
    ImageView imgCountryFlag;
    String countryName;
    String flagImageUrl;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_local_contact, container, false);

        txtLocalContact = view.findViewById(R.id.txtLocalContact);
        imgCountryFlag = view.findViewById(R.id.imgCountryFlag);

        flagImageUrl = this.getArguments().getString("flagImageUrl");
        countryName = this.getArguments().getString("countryName");

        Retrofit retrofit = NetworkClient.getRetrofitClient(getActivity());
        CountryApi api = retrofit.create(CountryApi.class);
        Request request = new Request(countryName);
        Call<LocalContactRes> call = api.getLocalContact(request);
        call.enqueue(new Callback<LocalContactRes>() {
            @Override
            public void onResponse(Call<LocalContactRes> call, Response<LocalContactRes> response) {
                if (response.isSuccessful()) {
                    LocalContactRes localContactRes = response.body();

                    txtLocalContact.setText(localContactRes.localContact);
                    Glide.with(getActivity()).load(flagImageUrl).into(imgCountryFlag);

                }
            }

            @Override
            public void onFailure(Call<LocalContactRes> call, Throwable throwable) {
                Toast.makeText(getActivity(), "네트워크 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
            }
        });



        return view;
    }
}