package com.example.swmc.Supervisor.RecyclerViewAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.swmc.Models.Notification;
import com.example.swmc.R;
import com.example.swmc.User.RecyclerViewAdapter.UserNotificationsAdapter;

import java.util.ArrayList;

public class SupNotAdapter extends RecyclerView.Adapter<SupNotAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<Notification> notificationArrayList;

    public SupNotAdapter(Context context, ArrayList<Notification> notificationArrayList) {
        this.context = context;
        this.notificationArrayList = notificationArrayList;
    }

    @NonNull
    @Override
    public SupNotAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.supervisor_notifications_row,parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SupNotAdapter.MyViewHolder holder, int position) {
        holder.title.setText(String.valueOf(notificationArrayList.get(position).getTitle()));
        holder.body.setText(String.valueOf(notificationArrayList.get(position).getBody()));

    }

    @Override
    public int getItemCount() {
        return notificationArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title, body;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.supervisor_notification_title);
            body = itemView.findViewById(R.id.supervisor_notification_body);
        }
    }
}
