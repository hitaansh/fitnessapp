package com.gaur.fitnessapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;


    TextInputLayout emailBox, passwordBox;
    ProgressBar bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        emailBox = findViewById(R.id.email);
        passwordBox = findViewById(R.id.password);
        bar = findViewById(R.id.progressBar);
    }

    public void signUp(View view) {
        bar.setVisibility(View.VISIBLE);
        String email = emailBox.getEditText().getText().toString();
        String password = passwordBox.getEditText().getText().toString();

        mAuth = FirebaseAuth.getInstance();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            bar.setVisibility(View.INVISIBLE);
                            emailBox.getEditText().setText("");
                            passwordBox.getEditText().setText("");
                            Toast.makeText(getApplicationContext(), "Registered Successfully", Toast.LENGTH_LONG).show();

                        } else {
                            Log.w("EMAILPASSWORD", "signInWithEmail:failure", task.getException());
                            bar.setVisibility(View.INVISIBLE);
                            emailBox.getEditText().setText("");
                            passwordBox.getEditText().setText("");
                            Toast.makeText(getApplicationContext(), "Registration Failure", Toast.LENGTH_LONG).show();
                        }

                    }
                });

    }
}