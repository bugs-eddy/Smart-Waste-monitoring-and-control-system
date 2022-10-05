package com.example.swmc.User;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

public class ItemDetails extends AppCompatActivity {
    private ImageView binImage;
    private TextView binLocation,binCapacity,binOnDuty;
    private Button itemSubmitBTN;
    Database database = new Database(this);
    User currentUser;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    Bin bin = new Bin();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("sensorData");

        currentUser = database.getAppUser();
        getSupportActionBar().setTitle(userType(currentUser.getUserType()));
        getSupportActionBar().setSubtitle(currentUser.getUserName());


        Bundle bundle = getIntent().getExtras();
        bin.setLocation(bundle.getString("location"));
        bin.setWasteLevel(bundle.getDouble("capacity"));
        bin.setOnDuty(bundle.getString("onDuty"));
        bin.setSubmitted(bundle.getBoolean("submitted"));

        binImage = findViewById(R.id.user_bin_image_details);
        binLocation = findViewById(R.id.user_bin_location_details);
        binCapacity = findViewById(R.id.user_bin_capacity_details);
        binOnDuty = findViewById(R.id.user_on_duty_details);
        itemSubmitBTN = findViewById(R.id.itemSubmitDetails);

        binCapacityImage(bin.getWasteLevel());
        binLocation.setText(bin.getLocation());
        if (bin.getWasteLevel()>=0 && bin.getWasteLevel()<5){
            binCapacity.setText("100");
        }
        else    {
            binCapacity.setText(String.valueOf(DecimalFormatting.df((50-(bin.getWasteLevel()))*2)));
        }

        binOnDuty.setText(bin.getOnDuty());
        itemSubmitBTN.setVisibility(View.GONE);
        if (bin.getOnDuty().equals(currentUser.getUserName())){
            if (bin.getWasteLevel()> 37.5) {
                if (bin.isSubmitted()==false){
                    itemSubmitBTN.setVisibility(View.VISIBLE);
                }
            }
        }
        itemSubmitBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
                Toast.makeText(ItemDetails.this, "Submitted", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ItemDetails.this,UserDashboard.class));

            }
        });

    }

    private void submit() {
        HashMap map = new HashMap();
        map.put("submitted",true);
        databaseReference.child(bin.getLocation()).updateChildren(map);
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