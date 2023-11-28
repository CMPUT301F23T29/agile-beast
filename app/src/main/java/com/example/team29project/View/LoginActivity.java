package com.example.team29project.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.team29project.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.mindrot.jbcrypt.BCrypt;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    private static final String CUSTOM_STATIC_SALT = "$2a$12$8hFH8e9HQptmvxJ9ZHnzRu";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        EditText usernameEdt = findViewById(R.id.usernameEdt);
        EditText passwordEdt = findViewById(R.id.passwordEdt);
        Button signupBtn = findViewById(R.id.sign_up_button);
        Button loginBtn = findViewById(R.id.login_button);
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        //signing up
        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEdt.getText().toString();
                String password = passwordEdt.getText().toString();
                String encryptPw= hashPassword(password);
                db.collection("Users").document(username).get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    // Username is already taken
                                    Toast.makeText(LoginActivity.this, "Username is already taken", Toast.LENGTH_SHORT).show();
                                } else {
                                    // Username is available, proceed with signup
                                    // You may want to add additional validation checks for the password, etc.
                                    Map<String, Object> userData = new HashMap<>();
                                    userData.put("password", encryptPw);

                                    // Add the new user to the "Users" collection
                                    db.collection("Users").document(username)
                                            .set(userData)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    // Signup successful
                                                    Toast.makeText(LoginActivity.this, "Signup successful", Toast.LENGTH_SHORT).show();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {

                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(LoginActivity.this, "Signup failed. Please try again.", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                            }
                        });

            }
        });


        // Login
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEdt.getText().toString();
                String password = passwordEdt.getText().toString(); // Assuming you have an EditText for password
                String encryptPw= hashPassword(password);

                db.collection("Users").document(username).get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    // Username found, check password
                                    String storedPassword = document.getString("password");
                                    if (encryptPw.equals(storedPassword)) {
                                        Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();

                                        Intent mainPage = new Intent(LoginActivity.this, MainPageActivity.class);
                                        mainPage.putExtra("userId" , username);
                                        startActivity(mainPage);

                                    } else {

                                        Toast.makeText(LoginActivity.this, storedPassword, Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    // Username not found
                                    Toast.makeText(LoginActivity.this, "Username not found", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
        });
        }
    private String hashPassword(String plainTextPassword) {
        // Define the strength of the hash (work factor)

        return BCrypt.hashpw(plainTextPassword,  CUSTOM_STATIC_SALT);
    }
}