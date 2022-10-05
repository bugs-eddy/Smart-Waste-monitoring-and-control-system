package com.example.swmc.Administrator.RecyclerViewAdapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.swmc.Administrator.Notifications;
import com.example.swmc.Models.Notification;
import com.example.swmc.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<Notification> notificationArrayList;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    public NotificationsAdapter(Context context, ArrayList<Notification> notificationArrayList) {
        this.context = context;
        this.notificationArrayList = notificationArrayList;
    }

    @NonNull
    @Override
    public NotificationsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.notification_row,parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationsAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.title.setText(String.valueOf(notificationArrayList.get(position).getTitle()));
        holder.body.setText(String.valueOf(notificationArrayList.get(position).getBody()));

        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                firebaseDatabase = FirebaseDatabase.getInstance();
                databaseReference = firebaseDatabase.getReference("notifications").child(notificationArrayList.get(position).getTitle());
                databaseReference.removeValue();
                Toast.makeText(context, "Notification Deleted", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, Notifications.class);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return notificationArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title, body;
        ConstraintLayout constraintLayout;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.notification_title);
            body = itemView.findViewById(R.id.notification_body);
            constraintLayout = itemView.findViewById(R.id.delete_notification);
        }
    }
}
