package com.foodio.catering.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.foodio.catering.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private Button loginButton, registerButton;
    private EditText emailEditText, passwordEditText;
    private FirebaseAuth auth;

    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inisialisasi Firebase
        FirebaseApp.initializeApp(this);

        setContentView(R.layout.activity_login);

        // Inisialisasi FirebaseAuth
        auth = FirebaseAuth.getInstance();

        // Inisialisasi komponen UI
        loginButton = findViewById(R.id.btnLogin);
        emailEditText = findViewById(R.id.inputEmail);
        passwordEditText = findViewById(R.id.inputPassword);
        registerButton = findViewById(R.id.btnRegister);

        // Tombol register
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

        // Tombol login
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });
    }

    private void loginUser() {
        String userEmail = emailEditText.getText().toString().trim();
        String userPassword = passwordEditText.getText().toString().trim();

        if (TextUtils.isEmpty(userEmail)) {
            Toast.makeText(this, "Email is Empty!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(userPassword)) {
            Toast.makeText(this, "Password is Empty!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (userPassword.length() < 6) {
            Toast.makeText(this, "Password must be at least 6 characters!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Debugging FirebaseAuth
        if (auth == null) {
            Log.e(TAG, "FirebaseAuth instance is null.");
            Toast.makeText(this, "Firebase is not initialized!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Login User
        auth.signInWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                            // Pindah ke MainActivity setelah login berhasil
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        } else {
                            // Tampilkan pesan error dari Firebase
                            String errorMessage = task.getException() != null ? task.getException().getMessage() : "Unknown error";
                            Log.e(TAG, "Login failed: " + errorMessage);
                            Toast.makeText(LoginActivity.this, "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
