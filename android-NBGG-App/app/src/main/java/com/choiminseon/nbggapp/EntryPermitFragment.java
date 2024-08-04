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
import com.choiminseon.nbggapp.model.EntryPermitRequirements;
import com.choiminseon.nbggapp.model.EntryPermitRes;
import com.choiminseon.nbggapp.model.Request;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EntryPermitFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EntryPermitFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EntryPermitFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EntryPermitFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EntryPermitFragment newInstance(String param1, String param2) {
        EntryPermitFragment fragment = new EntryPermitFragment();
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

    TextView txtCountryKorName;
    TextView txtCountryEngName;
    ImageView imgCountryFlag;
    TextView txtHaveYn;
    TextView txtGnrlPsptVisaYn;
    TextView txtGnrlPsptVisaCn;
    TextView txtOfclPsptVisaYn;
    TextView txtOfclPsptVisaCn;
    TextView txtDplmtPsptVisaYn;
    TextView txtDplmtPsptVisaCn;
    TextView txtNvisaEntryEvdcCn;
    TextView txtRemark;
    String countryName;
    String flagImageUrl;
    TextView textView38;
    TextView textView36;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_entry_permit, container, false);

        countryName = this.getArguments().getString("countryName");
        flagImageUrl = this.getArguments().getString("flagImageUrl");

        txtCountryKorName = view.findViewById(R.id.txtCountryKorName);
        txtCountryEngName = view.findViewById(R.id.txtCountryEngName);
        imgCountryFlag = view.findViewById(R.id.imgCountryFlag);
        txtHaveYn = view.findViewById(R.id.txtHaveYn);
        txtGnrlPsptVisaYn = view.findViewById(R.id.txtGnrlPsptVisaYn);
        txtGnrlPsptVisaCn = view.findViewById(R.id.txtGnrlPsptVisaCn);
        txtOfclPsptVisaYn = view.findViewById(R.id.txtOfclPsptVisaYn);
        txtOfclPsptVisaCn = view.findViewById(R.id.txtOfclPsptVisaCn);
        txtDplmtPsptVisaYn = view.findViewById(R.id.txtDplmtPsptVisaYn);
        txtDplmtPsptVisaCn = view.findViewById(R.id.txtDplmtPsptVisaCn);
        txtNvisaEntryEvdcCn = view.findViewById(R.id.txtNvisaEntryEvdcCn);
        txtRemark = view.findViewById(R.id.txtRemark);
        textView38 = view.findViewById(R.id.textView38);
        textView36 = view.findViewById(R.id.textView36);

        Retrofit retrofit = NetworkClient.getRetrofitClient(getActivity());
        CountryApi api = retrofit.create(CountryApi.class);
        Request request = new Request(countryName);

        Call<EntryPermitRes> call = api.getEntryPermit(request);
        call.enqueue(new Callback<EntryPermitRes>() {
            @Override
            public void onResponse(Call<EntryPermitRes> call, Response<EntryPermitRes> response) {
                if (response.isSuccessful()) {
                    EntryPermitRes entryPermitRes = response.body();
                    EntryPermitRequirements entryPermitRequirements = entryPermitRes.entryPermitRequirements.get(0);


                    txtCountryKorName.setText(entryPermitRequirements.countryName);
                    txtCountryEngName.setText("(" + entryPermitRequirements.countryEngName + " )");
                    Glide.with(getActivity()).load(flagImageUrl).into(imgCountryFlag);
                    // 여권소지 여부
                    if (entryPermitRequirements.haveYn.equals("Y")) {
                        txtHaveYn.setText("필수");
                    } else {
                        txtHaveYn.setText("필수 아님");
                    }

                    // 일반여권 입국허가요건 여부
                    if (entryPermitRequirements.gnrlPsptVisaYn.equals("Y")) {
                        txtGnrlPsptVisaYn.setText("가능");
                    } else {
                        txtGnrlPsptVisaYn.setText("불가능");
                    }

                    // 일반여권 입국허가요건 내용
                    if (entryPermitRequirements.gnrlPsptVisaCn.equals("X")) {
                        txtGnrlPsptVisaCn.setVisibility(View.GONE);
                    } else {
                        txtGnrlPsptVisaCn.setText("( " + entryPermitRequirements.gnrlPsptVisaCn + " )");
                    }

                    // 관용여권 입국허가요건 여부
                    if (entryPermitRequirements.ofclpsptVisaYn.equals("Y")) {
                        txtOfclPsptVisaYn.setText("가능");
                    } else {
                        txtOfclPsptVisaYn.setText("불가능");
                    }

                    // 관용여권 입국허가요건 내용
                    if (entryPermitRequirements.ofclpsptVisaCn.equals("X")) {
                        txtOfclPsptVisaCn.setVisibility(View.GONE);
                    } else {
                        txtOfclPsptVisaCn.setText("( " + entryPermitRequirements.ofclpsptVisaCn + " )");
                    }

                    // 외교관여권 입국허가요건 여부
                    if (entryPermitRequirements.dplmtPsptVisaYn.equals("Y")) {
                        txtDplmtPsptVisaYn.setText("가능");
                    } else {
                        txtDplmtPsptVisaCn.setText("불가능");
                    }

                    // # 외교관여권 입국허가요건 내용
                    if (entryPermitRequirements.dplmtPsptVisaCn.equals("X")) {
                        txtDplmtPsptVisaCn.setVisibility(View.GONE);
                    } else {
                        txtDplmtPsptVisaCn.setText("( " + entryPermitRequirements.dplmtPsptVisaCn + " )");
                    }

                    // 무비자 입국근거내용
                    if (entryPermitRequirements.nvisaEntryEvdcCn.equals("X")) {
                        txtNvisaEntryEvdcCn.setVisibility(View.GONE);
                        textView36.setVisibility(View.GONE);
                    } else if (entryPermitRequirements.nvisaEntryEvdcCn.isEmpty()) {
                        txtNvisaEntryEvdcCn.setVisibility(View.GONE);
                        textView36.setVisibility(View.GONE);
                    } else {
                        txtNvisaEntryEvdcCn.setText(entryPermitRequirements.nvisaEntryEvdcCn);
                    }

                    // 비고
                    if (entryPermitRequirements.remark.isEmpty()) {
                        txtRemark.setVisibility(View.GONE);
                        textView38.setVisibility(View.GONE);
                    } else {
                        txtRemark.setText(entryPermitRequirements.remark);
                    }

                }
            }

            @Override
            public void onFailure(Call<EntryPermitRes> call, Throwable throwable) {
                Toast.makeText(getActivity(), "네트워크 연결을 확인하세요.", Toast.LENGTH_SHORT).show();
                Log.e("EntryPermitFragment", "Request failed", throwable);

            }
        });






        return view;
    }
}