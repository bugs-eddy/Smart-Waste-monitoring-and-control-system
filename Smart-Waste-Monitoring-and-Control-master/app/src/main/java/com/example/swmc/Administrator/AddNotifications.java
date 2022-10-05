package com.example.swmc.Administrator;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.swmc.Database.Database;
import com.example.swmc.Models.Notification;
import com.example.swmc.Models.User;
import com.example.swmc.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddNotifications extends AppCompatActivity {

    EditText title,body;
    Button submit;
    FirebaseDatabase firebaseDatabase ;
    DatabaseReference databaseReference;
    ProgressDialog loadingBar;

    Database database = new Database(this);
    User currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notifications);

        currentUser = database.getAppUser();
        getSupportActionBar().setTitle(userType(currentUser.getUserType()));
        getSupportActionBar().setSubtitle(currentUser.getUserName());

        title = findViewById(R.id.title_add_notification);
        body = findViewById(R.id.body_add_notification);
        submit = findViewById(R.id.submit_add_notification);

        loadingBar = new ProgressDialog(this);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("notifications");

        submit.setOnClickListener(new View.OnClickListener() {
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
        String titleSTR = title.getText().toString().trim();
        String bodySTR = body.getText().toString().trim();

        if (TextUtils.isEmpty(titleSTR)){
            title.setError("Notification Title Can Not Be Empty");
            title.requestFocus();
        }
        else if (TextUtils.isEmpty(bodySTR)){
            body.setError("Notification Body Is Required");
            body.requestFocus();
        }
        else {
            loadingBar.setTitle("Signing In");
            loadingBar.setMessage("Please Wait...");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();
            Notification notification = new Notification(titleSTR,bodySTR);
            databaseReference.child(titleSTR).setValue(notification);
            loadingBar.dismiss();
            Toast.makeText(AddNotifications.this, "Notification Added", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(AddNotifications.this, Notifications.class);
            startActivity(intent);
            finish();
        }
    }
    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Use In App Navigation", Toast.LENGTH_SHORT).show();
    }
}