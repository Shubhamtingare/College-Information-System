package com.example.collegeinfo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUp extends AppCompatActivity {
    private TextInputLayout password, name, email;
    private FirebaseAuth mAuth;
    private ProgressBar progressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        password = findViewById(R.id.textpass);
        name = findViewById(R.id.textname);
        email = findViewById(R.id.textemail);
        Button button = findViewById(R.id.stepOneButton);
        mAuth = FirebaseAuth.getInstance();
        progressbar = findViewById(R.id.progressbar);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String Strpassword = password.getEditText().getText().toString();
                final String Strname = name.getEditText().getText().toString();
                final String Stremail = email.getEditText().getText().toString().trim();
                if (Stremail.isEmpty() && Strpassword.isEmpty() && Strname.isEmpty()) {

                    password.setError("Enter a valid password");
                    name.setError("Enter your Full name");
                    email.setError("Enter your email address");
                } else if (Stremail.isEmpty()) {
                    email.setError("Enter your Email address");
                } else if (Strpassword.isEmpty()) {
                    password.setError("Enter a valid password");
                } else if (Strname.isEmpty()) {
                    name.setError("Enter your name");
                } else {
                    register(Stremail, Strpassword);
                }
            }

        });
    }

    private void register(String stremail, String strpassword) {

        progressbar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(stremail, strpassword)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(),
                                    "Registration successful!",
                                    Toast.LENGTH_LONG)
                                    .show();

                            // hide the progress bar
                            progressbar.setVisibility(View.GONE);

                            // if the user created intent to login activity
                            Intent intent
                                    = new Intent(SignUp.this,
                                    MainActivity.class);
                            startActivity(intent);
                        } else {

                            // Registration failed
                            Toast.makeText(
                                    getApplicationContext(),
                                    "Registration failed!!"
                                            + " Please try again later",
                                    Toast.LENGTH_LONG)
                                    .show();

                            // hide the progress bar
                            progressbar.setVisibility(View.GONE);
                        }
                    }
                });

//                public static boolean isEmailValid(String email) {
//                    String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
//                    Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
//                    Matcher matcher = pattern.matcher(email);
//                    return matcher.matches();
//                }
    }
}



