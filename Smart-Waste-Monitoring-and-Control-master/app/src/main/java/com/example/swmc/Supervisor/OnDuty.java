package com.example.swmc.Supervisor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.swmc.Database.Database;
import com.example.swmc.Formatting.DecimalFormatting;
import com.example.swmc.Models.Bin;
import com.example.swmc.Models.User;
import com.example.swmc.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class OnDuty extends AppCompatActivity {
    private ImageView binImage;
    private TextView binLocation,binCapacity;
    private Button itemSubmitBTN;
    private EditText onDuty;
    FirebaseDatabase firebaseDatabase ;
    DatabaseReference databaseReference;
    Bin bin = new Bin();

    Database database = new Database(this);
    User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_duty);

        currentUser = database.getAppUser();
        getSupportActionBar().setTitle(userType(currentUser.getUserType()));
        getSupportActionBar().setSubtitle(currentUser.getUserName());

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("sensorData");


        Bundle bundle = getIntent().getExtras();
        bin.setLocation(bundle.getString("location"));
        bin.setWasteLevel(bundle.getDouble("capacity"));
        bin.setOnDuty(bundle.getString("onDuty"));
        bin.setSubmitted(bundle.getBoolean("submitted"));


        binImage = findViewById(R.id.supervisor_bin_image_details);
        binLocation = findViewById(R.id.supervisor_bin_location_on_duty);
        binCapacity = findViewById(R.id.supervisor_bin_capacity_on_duty);
        onDuty = findViewById(R.id.input_supervisor_user_on_duty);
        itemSubmitBTN = findViewById(R.id.on_duty_submit);

        if (!(bin.getOnDuty().equals("Not Assigned"))){
            onDuty.setText(bin.getOnDuty());
        }

        binCapacityImage(bin.getWasteLevel());
        binLocation.setText(bin.getLocation());
        if (bin.getWasteLevel()>=0 && bin.getWasteLevel()<5){
            binCapacity.setText("100");
        }
        else    {
            binCapacity.setText(String.valueOf(DecimalFormatting.df((50-(bin.getWasteLevel()))*2)));
        }
        itemSubmitBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validation();
            }
        });

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

    private void validation() {
        String onDutySTR = onDuty.getText().toString().trim();


        if (TextUtils.isEmpty(onDutySTR)){
            onDuty.setError("Please Add User On Duty");
            onDuty.requestFocus();
        }
        else {
            HashMap map = new HashMap();
            map.put("onDuty",onDutySTR);
            map.put("submitted",false);

            databaseReference.child(bin.getLocation()).updateChildren(map);

            Toast.makeText(this, "Updated", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(OnDuty.this, SupervisorDashBoard.class));
            finish();
        }
    }

    private void binCapacityImage(double capacity) {
        if (capacity <= 50 && capacity > 37.5){
            binImage.setImageResource(R.drawable.blue);
        }
        else if(capacity <= 37.5 && capacity > 25.0){
            binImage.setImageResource(R.drawable.green);
        }
        else if (capacity <= 25.0  && capacity > 12.5)
        {
            binImage.setImageResource(R.drawable.yellow);
        }
        else if (capacity < 12.5  && capacity >= 0)
        {
            binImage.setImageResource(R.drawable.red);
        }
    }
    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Use In App Navigation", Toast.LENGTH_SHORT).show();
    }
}