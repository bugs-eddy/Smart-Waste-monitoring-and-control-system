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

import com.example.swmc.Administrator.Users;
import com.example.swmc.Models.User;
import com.example.swmc.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<User> userList;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    public UsersAdapter(Context context, ArrayList<User> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public UsersAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.users_row,parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UsersAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.userName.setText(String.valueOf(userList.get(position).getUserName()));
        holder.accountType.setText(String.valueOf(userType(position)));


        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                firebaseDatabase = FirebaseDatabase.getInstance();
                databaseReference = firebaseDatabase.getReference("users").child(userList.get(position).getUserName());
                databaseReference.removeValue();
                Toast.makeText(context, "User Deleted", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, Users.class);
                context.startActivity(intent);
            }
        });
    }

    private String userType(int position) {
        String value = "Administrator";
        int user = userList.get(position).getUserType();
        if (user==2){
            value = "Supervisor";
        }
        else if (user==4){
            value = "User";
        }
        return value;
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView userName, accountType;
        ConstraintLayout constraintLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.notification_title);
            accountType = itemView.findViewById(R.id.user_notification_body);
            constraintLayout = itemView.findViewById(R.id.delete_user);
        }
    }
}
