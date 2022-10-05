package com.example.swmc.Supervisor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.swmc.Administrator.DashBoard;
import com.example.swmc.Administrator.Notifications;
import com.example.swmc.Administrator.Users;
import com.example.swmc.Database.Database;
import com.example.swmc.MainActivity;
import com.example.swmc.Models.Notification;
import com.example.swmc.Models.User;
import com.example.swmc.R;
import com.example.swmc.Supervisor.RecyclerViewAdapter.SupNotAdapter;
import com.example.swmc.User.RecyclerViewAdapter.UserNotificationsAdapter;
import com.example.swmc.User.UserNotifications;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class SupervisorNotifications extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    private long backPressedTime;
    private Toast toast;

    private RecyclerView recyclerView;
    SupNotAdapter supNotAdapter;
    private ArrayList<Notification> notificationArrayList;

    Database database = new Database(this);
    User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supervisor_notifications);

        currentUser = database.getAppUser();
        getSupportActionBar().setTitle(userType(currentUser.getUserType()));
        getSupportActionBar().setSubtitle(currentUser.getUserName());

        notificationArrayList = new ArrayList<>();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("notifications");

        recyclerView = findViewById(R.id.supervisorNotificationRecyclerView);
        recyclerView.setHasFixedSize(true);

        bottomNavigationView = findViewById(R.id.supervisorBottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.supervisorNotifications);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.supervisorNotifications:
                        return true;
                    case R.id.supervisorDashboard:
                        startActivity(new Intent(getApplicationContext(), SupervisorDashBoard.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        supNotAdapter = new SupNotAdapter(SupervisorNotifications.this,notificationArrayList);
        recyclerView.setAdapter(supNotAdapter);
        getNotifications();
    }

    private String userType(int user) {
        String value = "Administrator";
        if (user==2){
            value = "Supervisor";
        }
        else if (user==4){
            value = "User";
        }
        return value;
    }

    private void getNotifications() {
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                notificationArrayList.add(snapshot.getValue(Notification.class));
                supNotAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                supNotAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                supNotAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                supNotAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.sign_out:
                signOut();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void signOut() {
        boolean check =  database.deleteUser();
        if (check == true) {
            Toast.makeText(SupervisorNotifications.this, "Signed Out Successfully!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(SupervisorNotifications.this, MainActivity.class);
            startActivity(intent);
            finishAffinity();
        }

    }
    public void onBackPressed() {
        if (backPressedTime + 2000>System.currentTimeMillis()){
            toast.cancel();
            Toast.makeText(this, ":-)", Toast.LENGTH_SHORT).show();
            finishAffinity();
        }
        else {
            toast = Toast.makeText(this, "Press Again To Exit", Toast.LENGTH_SHORT);
            toast.show();
        }
        backPressedTime = System.currentTimeMillis();
    }
}