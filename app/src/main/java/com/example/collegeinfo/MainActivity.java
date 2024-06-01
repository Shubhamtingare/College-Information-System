package com.example.collegeinfo;

import static android.content.ContentValues.TAG;

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

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class MainActivity extends AppCompatActivity {
    private Button register, login;
    private TextInputLayout email, password;
    private ProgressBar progressbar;
    private FirebaseAuth mAuth;
    private FirebaseAuth firebaseAuth;
    GoogleSignInClient mGoogleSignInClient;
    private int RC_SIGN_IN = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        final Intent i = new Intent(this, SignUp.class);
        register = findViewById(R.id.button2);
        email = findViewById(R.id.editText);
        login = findViewById(R.id.button);
        password = findViewById(R.id.editText2);
        mAuth = FirebaseAuth.getInstance();
        progressbar = findViewById(R.id.progressBar);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(i);
            }
        });
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                // .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        findViewById(R.id.googleSign).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signinintent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signinintent, RC_SIGN_IN);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String Strpassword = password.getEditText().getText().toString();
                final String Stremail = email.getEditText().getText().toString().trim();
                if (TextUtils.isEmpty(Stremail)) {
                    Toast.makeText(getApplicationContext(),
                            "Please enter email!!",
                            Toast.LENGTH_LONG)
                            .show();
                    return;
                }

                if (TextUtils.isEmpty(Strpassword)) {
                    Toast.makeText(getApplicationContext(),
                            "Please enter password!!",
                            Toast.LENGTH_LONG)
                            .show();
                    return;
                }
                else{
                    loginuser(Stremail,Strpassword);

                }
            }
        });

    }
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
//        if (requestCode == RC_SIGN_IN) {
//            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
//            try {
//                // Google Sign In was successful, authenticate with Firebase
//                GoogleSignInAccount account = task.getResult(ApiException.class);
//                firebaseAuthWithGoogle(account);
//            } catch (ApiException e) {
//                // Google Sign In failed, update UI appropriately
//                Log.w(TAG, "Google sign in failed", e);
//                Toast.makeText(this, "Google sign in failed"+e.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
//
//    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
//        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
//
//        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
//        firebaseAuth.signInWithCredential(credential)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            // Sign in success, update UI with the signed-in user's information
//
//                            boolean newuser = task.getResult().getAdditionalUserInfo().isNewUser();
//                            if (newuser) {
//
//                                startActivity(new Intent(getApplicationContext(), SignUp.class));
//
//                            } else {
//
//                                Log.d(TAG, "signInWithCredential:success");
//                                FirebaseUser user = firebaseAuth.getCurrentUser();
//                                Toast.makeText(getApplicationContext(), user.getDisplayName(), Toast.LENGTH_SHORT).show();
//                                //SaveSharedPreference.setUserName(getApplicationContext(), user.getEmail());
//                                startActivity(new Intent(getApplicationContext(), Navigation.class));
//                            }
//                            finish();
//
//                        } else {
//                            // If sign in fails, display a message to the user.
//                            Log.w(TAG, "signInWithCredential:failure", task.getException());
////                            Toast.makeText(MainActivity.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
//                            Snackbar.make(findViewById(R.id.googleSign), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
////                            updateUI(null);
//                        }
//
//                    }
//                });
//    }

    private void loginuser(String stremail, String strpassword) {
        progressbar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(stremail, strpassword)
                .addOnCompleteListener(
                        new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(
                                    @NonNull Task<AuthResult> task)
                            {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(),
                                            "Login successful!!",
                                            Toast.LENGTH_LONG)
                                            .show();

                                    // hide the progress bar
                                    progressbar.setVisibility(View.GONE);

                                    // if sign-in is successful
                                    // intent to home activity
                                    Intent intent
                                            = new Intent(MainActivity.this,
                                            Homepage.class);
                                    startActivity(intent);
                                }

                                else {

                                    // sign-in failed
                                    Toast.makeText(getApplicationContext(),
                                            "Login failed!!",
                                            Toast.LENGTH_LONG)
                                            .show();

                                    // hide the progress bar
                                    progressbar.setVisibility(View.GONE);
                                }
                            }
                        });
    }
}