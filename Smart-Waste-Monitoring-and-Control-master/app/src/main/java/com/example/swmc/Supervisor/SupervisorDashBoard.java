package com.example.swmc.Supervisor;

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

import com.example.swmc.Database.Database;
import com.example.swmc.MainActivity;
import com.example.swmc.Models.Bin;
import com.example.swmc.Models.User;
import com.example.swmc.R;
import com.example.swmc.Supervisor.RecyclerViewAdapter.SupervisorDashboardAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SupervisorDashBoard extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    private ArrayList<Bin> binArrayList;
    private RecyclerView recyclerView;
    SupervisorDashboardAdapter supervisorDashboardAdapter;

    private long backPressedTime;
    private Toast toast;

    Database database = new Database(this);
    User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supervisor_dash_board);

        currentUser = database.getAppUser();
        getSupportActionBar().setTitle(userType(currentUser.getUserType()));
        getSupportActionBar().setSubtitle(currentUser.getUserName());

        binArrayList = new ArrayList<>();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("sensorData");

        recyclerView = findViewById(R.id.supervisorDashBoardRecyclerView);
        recyclerView.setHasFixedSize(true);

        bottomNavigationView = findViewById(R.id.supervisorBottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.supervisorDashboard);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.supervisorDashboard:
                        return true;
                    case R.id.supervisorNotifications:
                        startActivity(new Intent(getApplicationContext(), SupervisorNotifications.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        supervisorDashboardAdapter = new SupervisorDashboardAdapter(SupervisorDashBoard.this,binArrayList);
        recyclerView.setAdapter(supervisorDashboardAdapter);

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
                supervisorDashboardAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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
            Toast.makeText(SupervisorDashBoard.this, "Signed Out Successfully!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(SupervisorDashBoard.this, MainActivity.class);
            startActivity(intent);
            finishAffinity();
        }

    }

}