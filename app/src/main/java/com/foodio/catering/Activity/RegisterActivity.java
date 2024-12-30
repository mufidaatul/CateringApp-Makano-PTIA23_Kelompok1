package com.foodio.catering.Activity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.foodio.catering.R;
import com.foodio.catering.Model.UserModel;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import android.content.Intent;

public class RegisterActivity extends AppCompatActivity {

    private Button btnRegister;
    private EditText inputEmail, inputUser, inputPassword;
    private FirebaseAuth auth;
    private FirebaseDatabase database; // Deklarasi database Firebase

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Inisialisasi Firebase Auth dan Database
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance(); // Inisialisasi database

        // Hubungkan variabel dengan ID di layout XML
        btnRegister = findViewById(R.id.btnRegister);
        inputEmail = findViewById(R.id.inputEmail);
        inputUser = findViewById(R.id.inputUser);
        inputPassword = findViewById(R.id.inputPassword);

        // Set OnClickListener untuk tombol Register
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createUser(); // Panggil fungsi untuk membuat user baru
            }
        });
    }

    private void createUser() {
        String userEmail = inputEmail.getText().toString().trim();
        String userName = inputUser.getText().toString().trim();
        String userPassword = inputPassword.getText().toString().trim();

        // Validasi input
        if (TextUtils.isEmpty(userEmail)) {
            Toast.makeText(this, "Email is Empty!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(userName)) {
            Toast.makeText(this, "Username is Empty!", Toast.LENGTH_SHORT).show();
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

        // Buat user baru dengan email dan password
        auth.createUserWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Masukkan data user ke Firebase Database
                            UserModel userModel = new UserModel(userEmail, userName, userPassword);
                            String id = task.getResult().getUser().getUid();
                            database.getReference().child("Users").child(id).setValue(userModel);

                            Toast.makeText(RegisterActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();

                            // Kembali ke LoginActivity setelah registrasi berhasil
                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish(); // Menutup RegisterActivity
                        } else {
                            Toast.makeText(RegisterActivity.this, "Registration Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
