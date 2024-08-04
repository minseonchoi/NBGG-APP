package com.choiminseon.nbggapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.choiminseon.nbggapp.CountryInfoActivity;
import com.choiminseon.nbggapp.R;
import com.choiminseon.nbggapp.model.Countries;

import java.util.ArrayList;

public class CountrySearchAdapter extends RecyclerView.Adapter<CountrySearchAdapter.ViewHolder>{

    Context context;
    ArrayList<Countries> countryArrayList;

    public CountrySearchAdapter(Context context, ArrayList<Countries> countryArrayList) {
        this.context = context;
        this.countryArrayList = countryArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.country_list_row, parent, false);
        return new CountrySearchAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Countries countries = countryArrayList.get(position);

        Glide.with(context).load(countries.flagImageUrl).into(holder.imgCountryFlag);
        holder.txtCountryName.setText(countries.koreanName);
        holder.txtCountryNameEng.setText("(" + countries.englishName + ")");

    }

    @Override
    public int getItemCount() {
        return countryArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgCountryFlag;
        TextView txtCountryName;
        TextView txtCountryNameEng;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgCountryFlag = itemView.findViewById(R.id.imgCountryFlag);
            txtCountryName = itemView.findViewById(R.id.txtCountryKorName);
            txtCountryNameEng = itemView.findViewById(R.id.txtCountryNameEng);
            cardView = itemView.findViewById(R.id.cardView);

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int index = getAdapterPosition();
                    Countries countries = countryArrayList.get(index);

                    Intent intent = new Intent(context, CountryInfoActivity.class);
                    intent.putExtra("countryName", countries.koreanName);
                    intent.putExtra("flagImageUrl", countries.flagImageUrl);
                    context.startActivity(intent);
                }
            });
        }
    }


}
