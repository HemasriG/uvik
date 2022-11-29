package com.example.uvik.adapter;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.uvik.activity.MainActivity;
import com.example.uvik.R;
import com.example.uvik.model.GetResponse;
import com.example.uvik.model.MainModelData;

import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    Activity activity;
    List<MainModelData> modelData;
    GetResponse response;

    public MainAdapter(MainActivity mainActivity,List<MainModelData> data,GetResponse getResponse){
        this.activity=mainActivity;
        this.modelData=data;
        this.response=getResponse;
    }

    @NonNull
    @Override
    public MainAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewHolder viewHolder = new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.main_adapter,parent,false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MainAdapter.ViewHolder holder, int position) {
        MainModelData data = modelData.get(position);
        holder.officeName.setText(data.getOfficeName());
        holder.officialName.setText(data.getOfficialName()+" ("+data.getParty()+")");
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)activity).goToOfficialActivity(response,data);
            }
        });
    }

    @Override
    public int getItemCount() {
        return modelData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView officeName,officialName;
        CardView cardView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            officeName = (TextView) itemView.findViewById(R.id.office);
            officialName = (TextView) itemView.findViewById(R.id.official_name);
            cardView = (CardView) itemView.findViewById(R.id.card_view);
        }
    }
}