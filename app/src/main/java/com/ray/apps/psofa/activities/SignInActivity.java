package com.ray.apps.psofa.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.ray.apps.psofa.R;


public class SignInActivity extends BaseActivity {

    private static final String TAG = "pSOFALogin";
    private static final int RC_SIGN_IN = 006;
    private static final int RC_SIGN_IN_G = 007;

    private ProgressBar mProgressBar;
    private Button btnLogin;
    SignInButton gBtnSignIn;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    FirebaseUser current_user;

    private GoogleApiClient mGoogleApiClient;
    GoogleSignInAccount acct;
    private TextView textViewLinkRegister;
    private EditText inputEmail, inputPassword;
    String idTokenString = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

//

        textViewLinkRegister = findViewById(R.id.textViewLinkRegister);
        btnLogin = findViewById(R.id.signInBtn);
        inputEmail = findViewById(R.id.emailInput);
        inputPassword = findViewById(R.id.passwordInput);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                current_user = firebaseAuth.getCurrentUser();
                if (current_user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + current_user.getUid());
                    Intent nav = new Intent(SignInActivity.this, NavActivity.class);
                    startActivity(nav);
                    finish();
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };

        textViewLinkRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to RegisterActivity
                Intent intentRegister = new Intent(SignInActivity.this, RegisterActivity.class);
                startActivity(intentRegister);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isConnected()) {


                    //sign in
                    if (inputVerified()) {
                        //showProgressDialog();
                        mProgressBar.setVisibility(View.VISIBLE);
                        signInWithEmail(inputEmail.getText().toString(), inputPassword.getText().toString());
                    }
                }
                else
                {
                    //Display message/aleart "No internet connection" to user
                    Toast.makeText(SignInActivity.this, "No internet connection",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.google_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* Activity */, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toast.makeText(SignInActivity.this, "Google API Client Connection failed.",
                                Toast.LENGTH_SHORT).show();
                    }
                } /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        gBtnSignIn = findViewById(R.id.sign_in_button);
        gBtnSignIn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN_G);
            }
        });
    }

    public boolean inputVerified()
    {
        String email = inputEmail.getText().toString();
        String password = inputPassword.getText().toString();

        if(email.isEmpty())
        {
            Toast.makeText(getApplicationContext(),
                    "Email address should not be blank", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(password.isEmpty())
        {
            Toast.makeText(getApplicationContext(),
                    "Password should not be blank", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    @Override
    public void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(mAuthListener);
    }

    private void signInWithEmail(String email, String password) {

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                           /* if(current_user != null && !current_user.isEmailVerified())
                            {
                                //current_user.sendEmailVerification();
                                Toast.makeText(SignInActivity.this, "A verfication email has been sent to your email id.",
                                        Toast.LENGTH_SHORT).show();
                            }*/
                            mProgressBar.setVisibility(View.GONE);
                            Intent nav = new Intent(SignInActivity.this, NavActivity.class);
                            startActivity(nav);
                        } else {
                            // If sign in fails, display a message to the user.
                            mProgressBar.setVisibility(View.GONE);
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(SignInActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // ...
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN_G) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase

                GoogleSignInAccount account = task.getResult(ApiException.class);
                if(account != null)
                {
                    Log.w(TAG, "Google sign in success");
                    Log.w(TAG, "Linking the google account with Firebase");
                    firebaseAuthWithGoogle(account);
                }
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // ...
            }
        }

    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "Google User Id :" + acct.getId());

        // --------------------------------- //
        // BELOW LINE GIVES YOU JSON WEB TOKEN, (USED TO GET ACCESS TOKEN) :
        Log.d(TAG, "Google JWT : " + acct.getIdToken());
        // --------------------------------- //

        // Save this JWT in global String :
        idTokenString = acct.getIdToken();

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        if(task.isSuccessful()){
                            // --------------------------------- //
                            // BELOW LINE GIVES YOU FIREBASE TOKEN ID :
                            Log.d(TAG, "Firebase User Access Token : " + task.getResult().getUser());
                            // --------------------------------- //
                            //linkUserToGoogle(credential);
                        }
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        else {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(SignInActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    public void linkUserToGoogle(AuthCredential credential )
    {
        mAuth.getCurrentUser().linkWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "linkWithCredential:success");

                        } else {
                            Log.w(TAG, "linkWithCredential:failure", task.getException());
                            Toast.makeText(SignInActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // ...
                    }
                });
    }

}