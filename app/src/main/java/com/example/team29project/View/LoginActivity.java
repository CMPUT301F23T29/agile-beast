package com.example.team29project.View;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.team29project.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import org.mindrot.jbcrypt.BCrypt;
import java.util.HashMap;
import java.util.Map;

/**
 * Activity that is used for login/sign up to main page
 */

public class LoginActivity extends AppCompatActivity {

    // Salt Value that is used to hash the password
    private static final String CUSTOM_STATIC_SALT = "$2a$12$8hFH8e9HQptmvxJ9ZHnzRu";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        EditText usernameEdt = findViewById(R.id.usernameEdt);
        EditText passwordEdt = findViewById(R.id.passwordEdt);
        usernameEdt.setText("");
        passwordEdt.setText("");
        Button signupBtn = findViewById(R.id.sign_up_button);
        Button loginBtn = findViewById(R.id.login_button);
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        //signing up
        signupBtn.setOnClickListener(v -> {
            signupBtn.setEnabled(false);
            loginBtn.setEnabled(false);
            String username = usernameEdt.getText().toString();
            String password = passwordEdt.getText().toString();
            String encryptPw = hashPassword(password);
            if(!username.isEmpty()){
                db.collection("Users").document(username).get()
                        .addOnCompleteListener(task -> {
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
                                        .addOnSuccessListener(aVoid -> {
                                            // Signup successful
                                            Toast.makeText(LoginActivity.this, "Signup successful", Toast.LENGTH_SHORT).show();
                                        })
                                        .addOnFailureListener(e ->{

                                                Toast.makeText(LoginActivity.this, "Signup failed. Please try again.", Toast.LENGTH_SHORT).show();
                                        });
                            }
                        });
        }
            signupBtn.setEnabled(true);
            loginBtn.setEnabled(true);
        });


        // Login
        loginBtn.setOnClickListener(v -> {
            signupBtn.setEnabled(false);
            loginBtn.setEnabled(false);
            String username = usernameEdt.getText().toString();
            String password = passwordEdt.getText().toString(); // Assuming you have an EditText for password
            String encryptPw = hashPassword(password);
            username.replaceAll("\\s+", "");
            if(!username.isEmpty()){
            db.collection("Users").document(username).get()
                    .addOnCompleteListener(task -> {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            // Username found, check password
                            String storedPassword = document.getString("password");
                            if (encryptPw.equals(storedPassword)) {
                                Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();

                                Intent mainPage = new Intent(LoginActivity.this, MainPageActivity.class);
                                mainPage.putExtra("userId", username);
                                startActivity(mainPage);

                            } else {

                                Toast.makeText(LoginActivity.this, "Wrong password", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            // Username not found
                            Toast.makeText(LoginActivity.this, "Username not found", Toast.LENGTH_SHORT).show();
                            ;
                        }
                    });
        }
            signupBtn.setEnabled(true);
            loginBtn.setEnabled(true);

        });
        }

    /**
     * this function encrypts a password for login
      * @param plainTextPassword password of what user want to have
     * @return return encrypted password
     */
    private String hashPassword(String plainTextPassword) {
        // Define the strength of the hash (work factor)
        return BCrypt.hashpw(plainTextPassword,  CUSTOM_STATIC_SALT);
    }
}