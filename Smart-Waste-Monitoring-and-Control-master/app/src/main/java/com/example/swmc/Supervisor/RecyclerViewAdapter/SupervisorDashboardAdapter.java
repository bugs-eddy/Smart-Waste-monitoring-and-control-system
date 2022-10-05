package com.example.swmc.Supervisor.RecyclerViewAdapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.swmc.Formatting.DecimalFormatting;
import com.example.swmc.Models.Bin;
import com.example.swmc.R;
import com.example.swmc.Supervisor.OnDuty;

import java.util.ArrayList;

public class SupervisorDashboardAdapter extends RecyclerView.Adapter<SupervisorDashboardAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<Bin> binArrayList;

    public SupervisorDashboardAdapter(Context context, ArrayList<Bin> binArrayList) {
        this.context = context;
        this.binArrayList = binArrayList;
    }

    @NonNull
    @Override
    public SupervisorDashboardAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.supervisor_dashboard_row,parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SupervisorDashboardAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.location.setText(String.valueOf(binArrayList.get(position).getLocation()));
        if (binArrayList.get(position).getWasteLevel()>= 0 && binArrayList.get(position).getWasteLevel() < 5){
            holder.capacity.setText("100");
        }
        else {
            holder.capacity.setText(String.valueOf(DecimalFormatting.df((50-(binArrayList.get(position).getWasteLevel()))*2)));
        }
        holder.onDuty.setText(String.valueOf(binArrayList.get(position).getOnDuty()));
        binToggle(binArrayList.get(position).isSubmitted(),holder);
        binCapacityImage(binArrayList.get(position).getWasteLevel(),holder);

        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( context, OnDuty.class);
                Bundle bundle = new Bundle();
                bundle.putString("location",binArrayList.get(position).getLocation());
                bundle.putDouble("capacity",binArrayList.get(position).getWasteLevel());
                bundle.putString("onDuty",binArrayList.get(position).getOnDuty());
                bundle.putBoolean("submitted", binArrayList.get(position).isSubmitted());
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });

    }

    private void binToggle(boolean submitted, MyViewHolder holder) {
        if (submitted==true){
            holder.toggleBtn.setImageResource(R.drawable.tgreen);
        }
        else {
            holder.toggleBtn.setImageResource(R.drawable.tred);
        }
    }

    private void binCapacityImage(double capacity, MyViewHolder holder) {
        if (capacity <= 50 && capacity > 37.5){
            holder.binImage.setImageResource(R.drawable.blue);
        }
        else if(capacity <= 37.5 && capacity > 25.0){
            holder.binImage.setImageResource(R.drawable.green);
        }
        else if (capacity <= 25.0  && capacity > 12.5)
        {
            holder.binImage.setImageResource(R.drawable.yellow);
        }
        else if (capacity < 12.5  && capacity >= 0)
        {
            holder.binImage.setImageResource(R.drawable.red);
        }
    }

    @Override
    public int getItemCount() {
        return binArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView location, capacity,onDuty;
        ImageView binImage,toggleBtn;
        ConstraintLayout constraintLayout;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            location = itemView.findViewById(R.id.supervisor_bin_location);
            capacity = itemView.findViewById(R.id.supervisor_bin_capacity);
            onDuty = itemView.findViewById(R.id.supervisor_user_on_duty);
            binImage = itemView.findViewById(R.id.supervisor_bin_image);
            toggleBtn = itemView.findViewById(R.id.toggle_btn);
            constraintLayout = itemView.findViewById(R.id.itemOnClickConstraintLayoutSupervisor);

        }
    }
}
