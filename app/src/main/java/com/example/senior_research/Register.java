package com.example.senior_research;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Register extends AppCompatActivity {
    EditText email, password, confirmPassword, username, name;
    Button register;
    TextView alreadyRegistered;
    FirebaseAuth fAuth;
    DatabaseReference reference;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirmpass);
        register = findViewById(R.id.register);
        alreadyRegistered = findViewById(R.id.alreadyRegistered);
        username = findViewById(R.id.username);
        name = findViewById(R.id.name);
        fAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar);

        /*if(fAuth.getCurrentUser()!=null){
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }*/
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userEmail = email.getText().toString().trim();
                String userPassword = password.getText().toString().trim();
                String confirm = confirmPassword.getText().toString().trim();
                String usern = username.getText().toString().trim();
                String enteredName = name.getText().toString().trim();


                if(TextUtils.isEmpty(userEmail)){
                    email.setError("Email is required.");
                    return;
                }
                if(TextUtils.isEmpty(enteredName)){
                    email.setError("name is required.");
                    return;
                }
                if(TextUtils.isEmpty(userPassword)){
                    password.setError("Password is required.");
                    return;
                }
                if(TextUtils.isEmpty(usern)){
                    username.setError("Username is required.");
                    return;
                }
                if(TextUtils.isEmpty(confirm)){
                    confirmPassword.setError("Confirm your password.");
                    return;
                }
                if(!userPassword.equals(confirm)){
                    confirmPassword.setError("Passwords don't match.");
                    return;
                }
                if(password.length()<=6){
                    password.setError("Password length must be >=6");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                //register the user in firebase
                fAuth.createUserWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseAuth auth = FirebaseAuth.getInstance();
                            FirebaseUser user = auth.getCurrentUser();
                            String userid = user.getUid();

                            reference = FirebaseDatabase.getInstance().getReference().child("Users").child(userid);

                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("id", userid);
                            hashMap.put("username", username.getText().toString().trim().toLowerCase());
                            hashMap.put("name", name.getText().toString().trim().toLowerCase());
                            hashMap.put("bio", "");
                            hashMap.put("imageurl", "");

                            reference.setValue(hashMap);

                            user.sendEmailVerification()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(Register.this, "Registered successfully. Please verify your email address.", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(getApplicationContext(), Login.class));
                                            }
                                            else{
                                                Toast.makeText(Register.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                            //Toast.makeText(Register.this, "User Created.", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(Register.this, "Error" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });
        alreadyRegistered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Login.class));
            }
        });
    }
}