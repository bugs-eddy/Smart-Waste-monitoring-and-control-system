package com.example.swmc.User.RecyclerViewAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.swmc.Administrator.RecyclerViewAdapter.NotificationsAdapter;
import com.example.swmc.Models.Notification;
import com.example.swmc.R;

import java.util.ArrayList;

public class UserNotificationsAdapter extends RecyclerView.Adapter<UserNotificationsAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<Notification> notificationArrayList;

    public UserNotificationsAdapter(Context context, ArrayList<Notification> notificationArrayList) {
        this.context = context;
        this.notificationArrayList = notificationArrayList;
    }

    @NonNull
    @Override
    public UserNotificationsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_notification_row,parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserNotificationsAdapter.MyViewHolder holder, int position) {
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
            title = itemView.findViewById(R.id.user_notification_title);
            body = itemView.findViewById(R.id.user_notification_body);
        }
    }
}
