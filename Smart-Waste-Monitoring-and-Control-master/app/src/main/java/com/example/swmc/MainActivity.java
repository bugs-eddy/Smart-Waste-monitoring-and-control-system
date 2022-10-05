package com.example.swmc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.swmc.Administrator.DashBoard;
import com.example.swmc.Database.Database;
import com.example.swmc.Models.User;
import com.example.swmc.Supervisor.SupervisorDashBoard;
import com.example.swmc.User.UserDashboard;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    EditText userName, password;
    Button signInBTN;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    User user;
    ProgressDialog loadingBar;
    Database database = new Database(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userName = findViewById(R.id.user_name);
        password = findViewById(R.id.password);
        signInBTN = findViewById(R.id.sign_in);

        loadingBar = new ProgressDialog(this);

        user = new User();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("users");

        signInBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this, UserDashboard.class);
//                startActivity(intent);
                validation();
            }
        });
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
                loadingBar.setTitle("Signing In");
                loadingBar.setMessage("Please Wait...");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();
                databaseReference = firebaseDatabase.getReference("users").child(userNameSTR);
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        
                        user  = snapshot.getValue(User.class);
                        if (user != null){
                            if (passwordSTR.equals(user.getPassword())){
                                database.deleteUser();
                                boolean insert = database.addUser(user);
                                loadingBar.dismiss();
                                if (insert==true){
                                    int userType = (user.getUserType());
                                    switch (userType) {
                                        case 0:
                                            loadingBar.dismiss();
                                            Toast.makeText(MainActivity.this, "Welcome Administrator : " + user.getUserName(), Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(MainActivity.this, DashBoard.class));
                                            finishAffinity();
                                            break;
                                        case 2:
                                            loadingBar.dismiss();
                                            Toast.makeText(MainActivity.this, "Welcome Supervisor : " + user.getUserName(), Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(MainActivity.this, SupervisorDashBoard.class));
                                            finishAffinity();
                                            break;
                                        case 4:
                                            loadingBar.dismiss();
                                            Toast.makeText(MainActivity.this, "Welcome User : " + user.getUserName(), Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(MainActivity.this, UserDashboard.class));
                                            finishAffinity();
                                            break;
                                    }
                                }
                                else {
                                    Toast.makeText(MainActivity.this, "Error Inserting In Database Please Clear Cache And Storage", Toast.LENGTH_SHORT).show();
                                }

                            }
                            else {
                                loadingBar.dismiss();
                                Toast.makeText(MainActivity.this, "Invalid Password", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                            loadingBar.dismiss();
                            Toast.makeText(MainActivity.this, "User Does N ot Exist", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
    }
}