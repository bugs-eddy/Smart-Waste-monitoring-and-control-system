package com.example.swmc.Administrator;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.swmc.Database.Database;
import com.example.swmc.Models.User;
import com.example.swmc.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddUser extends AppCompatActivity  implements AdapterView.OnItemSelectedListener {

    private String userTypeSTR;
    EditText userName,password;
    Button submit;
    FirebaseDatabase firebaseDatabase ;
    DatabaseReference databaseReference;
    ProgressDialog loadingBar;

    Database database = new Database(this);
    User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        currentUser = database.getAppUser();
        getSupportActionBar().setTitle(userType(currentUser.getUserType()));
        getSupportActionBar().setSubtitle(currentUser.getUserName());

        userName = findViewById(R.id.title_add_notification);
        password = findViewById(R.id.password_add_user);
        submit =findViewById(R.id.submit_add_user);

        loadingBar = new ProgressDialog(this);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("users");

        Spinner spinner =  findViewById(R.id.user_type_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.user_type_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

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
        String userNameSTR = userName.getText().toString().trim();
        String passwordSTR = password.getText().toString().trim();

        if (TextUtils.isEmpty(userNameSTR)){
            userName.setError("User Name is Required");
            userName.requestFocus();
        }
        else if (TextUtils.isEmpty(passwordSTR)){
            password.setError("Password is Required");
            password.requestFocus();
        }
        else if(password.length()<4){
            password.setError("Min Password Length is 4 Characters");
            password.requestFocus();
        }
        else {
            loadingBar.setTitle("Creating User");
            loadingBar.setMessage("Please Wait...");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();
            User user = new User(userNameSTR,userType(userTypeSTR),userNameSTR,passwordSTR);
            databaseReference.child(userNameSTR).setValue(user);
            loadingBar.dismiss();
            Toast.makeText(AddUser.this, "User Added", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(AddUser.this, Users.class);
            startActivity(intent);
            finish();
        }
    }
    private int userType(String type) {
        int value = 0;
        if (type.equals("Supervisor")){
            value = 2;
        }
        else if (type.equals("User")){
            value = 4;
        }
        return value;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        userTypeSTR = (String) parent.getItemAtPosition(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Use In App Navigation", Toast.LENGTH_SHORT).show();
    }
}