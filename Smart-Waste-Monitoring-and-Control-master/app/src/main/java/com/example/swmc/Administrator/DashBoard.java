package com.example.swmc.Administrator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.swmc.Administrator.RecyclerViewAdapter.DashboardAdapter;
import com.example.swmc.Database.Database;
import com.example.swmc.MainActivity;
import com.example.swmc.Models.Bin;
import com.example.swmc.Models.User;
import com.example.swmc.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DashBoard extends AppCompatActivity {
    private long backPressedTime;
    private Toast toast;

    BottomNavigationView bottomNavigationView;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    private RecyclerView recyclerView;
    DashboardAdapter dashboardAdapter;
    private ArrayList<Bin> binArrayList;
    Database database = new Database(this);
    User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);


        currentUser = database.getAppUser();
        getSupportActionBar().setTitle(userType(currentUser.getUserType()));
        getSupportActionBar().setSubtitle(currentUser.getUserName());

        binArrayList = new ArrayList<>();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("sensorData");


        recyclerView = findViewById(R.id.dashboardRecyclerView);
        recyclerView.setHasFixedSize(true);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.dashboard);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.dashboard:
                        return true;
                    case R.id.notifications:
                        startActivity(new Intent(getApplicationContext(), Notifications.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.users:
                        startActivity(new Intent(getApplicationContext(), Users.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        dashboardAdapter = new DashboardAdapter(DashBoard.this,binArrayList);
        recyclerView.setAdapter(dashboardAdapter);

        getSensorData();
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

    private void getSensorData() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                binArrayList.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Bin bin = dataSnapshot.getValue(Bin.class);
                    binArrayList.add(bin);
                }

                dashboardAdapter.notifyDataSetChanged();

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
            Toast.makeText(DashBoard.this, "Signed Out Successfully!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(DashBoard.this, MainActivity.class);
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