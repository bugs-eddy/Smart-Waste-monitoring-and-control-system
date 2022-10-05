package com.example.swmc.Administrator;

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
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.swmc.Administrator.RecyclerViewAdapter.UsersAdapter;
import com.example.swmc.Database.Database;
import com.example.swmc.MainActivity;
import com.example.swmc.Models.User;
import com.example.swmc.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class Users extends AppCompatActivity  {
    BottomNavigationView bottomNavigationView;
    private long backPressedTime;
    private Toast toast;

    Button addUser;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    private RecyclerView recyclerView;
    UsersAdapter usersAdapter;
    private ArrayList<User> userList;
    Database database = new Database(this);
    User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);


        currentUser = database.getAppUser();
        getSupportActionBar().setTitle(userType(currentUser.getUserType()));
        getSupportActionBar().setSubtitle(currentUser.getUserName());

        addUser = findViewById(R.id.addUser);
        userList = new ArrayList<>();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("users");

        recyclerView = findViewById(R.id.usersRecyclerView);
        recyclerView.setHasFixedSize(true);



        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.users);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.users:
                        return true;
                    case R.id.dashboard:
                        startActivity(new Intent(getApplicationContext(), DashBoard.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.notifications:
                        startActivity(new Intent(getApplicationContext(), Notifications.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });

        addUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Users.this, AddUser.class);
                startActivity(intent);
                finish();
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        getUsers();


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

    private void getUsers() {
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                userList.add(snapshot.getValue(User.class));
                usersAdapter = new UsersAdapter(Users.this,userList);
                recyclerView.setAdapter(usersAdapter);
                usersAdapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                usersAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                usersAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                usersAdapter.notifyDataSetChanged();
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
            Toast.makeText(Users.this, "Signed Out Successfully!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Users.this, MainActivity.class);
            startActivity(intent);
            finishAffinity();
        }

    }

}