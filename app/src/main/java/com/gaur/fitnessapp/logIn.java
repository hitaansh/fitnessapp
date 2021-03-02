package com.gaur.fitnessapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

public class logIn extends AppCompatActivity {
    private FirebaseAuth mAuth;
    TextInputLayout emailBox, passwordBox;
    ProgressBar bar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        emailBox = findViewById(R.id.email_login);
        passwordBox = findViewById(R.id.password_login);
        bar = findViewById(R.id.progressBar_login);
    }

    public void signIn(View view) {
        bar.setVisibility(View.VISIBLE);
        String email = emailBox.getEditText().getText().toString();
        String password = passwordBox.getEditText().getText().toString();

        mAuth = FirebaseAuth.getInstance();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(logIn.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {


                            bar.setVisibility(View.INVISIBLE);
                            emailBox.getEditText().setText("");
                            passwordBox.getEditText().setText("");
                            Toast.makeText(getApplicationContext(), "Logged In Successfully", Toast.LENGTH_LONG).show();

                            Intent intent = new Intent(logIn.this, dashboard.class);
                            intent.putExtra("email", mAuth.getCurrentUser().getEmail());
                            intent.putExtra("uid", mAuth.getCurrentUser().getUid());



                            startActivity(intent);

                        } else {
                            bar.setVisibility(View.INVISIBLE);
                            emailBox.getEditText().setText("");
                            passwordBox.getEditText().setText("");
                            Toast.makeText(getApplicationContext(), "Couldn't Log In", Toast.LENGTH_LONG).show();
                        }

                    }
                });

    }
}