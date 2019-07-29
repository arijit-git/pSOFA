package com.ray.apps.psofa.activities;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.ray.apps.psofa.R;
import com.squareup.picasso.Picasso;

public class ProfileActivity extends BaseActivity {


    private static final String TAG = "pSOFA/Profile";
    TextView textName, FBUserId, textEmail, text_verify, et_phone;
    private ImageView mProfileImageView;

    private Button updatePhone;
    private static final int RC_SIGN_IN = 007;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        setTitle("My Profile");


        FBUserId = findViewById(R.id.tv_user_id_val);
        textName = findViewById(R.id.textpNameVal);
        textEmail = findViewById(R.id.textpEmailVal);
        text_verify = findViewById(R.id.text_verify);
        mProfileImageView = findViewById(R.id.imageProfile);
        et_phone = findViewById(R.id.et_phone);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // perform whatever you want on back arrow click
                finish();
            }
        });
        //updatePhone = findViewById(R.id.buttonpSave);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        // Check if user is signed in (non-null) and update UI accordingly.
        final FirebaseUser currentUser = mAuth.getCurrentUser();

        /*updatePhone.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               updatePhone();
               *//*UserProfileChangeRequest userProfileChangeRequest = new UserProfileChangeRequest();
               currentUser.updateProfile()updateProfile({
                       displayName: "Jane Q. User",
                       photoURL: "https://example.com/jane-q-user/profile.jpg"
               }).then(function() {
                   // Update successful.
               }).catch(function(error) {
                   // An error happened.
               });*//*
           }
        });*/

        text_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!text_verify.getText().toString().equalsIgnoreCase("verified"))
                {
                    Log.d(TAG, "onClick: Verify : " + " userName :" + currentUser.getDisplayName());

                    /*currentUser.updateEmail().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                        }
                    });*/

                    currentUser.sendEmailVerification();
                    Toast.makeText(ProfileActivity.this, "Verification email has been sent to your registered email id.",
                            Toast.LENGTH_LONG).show();

            }
            }
        });

        if(currentUser != null) {
            updateUI(currentUser);

            Log.d(TAG, "Current user : " + currentUser.getUid());
        }
    }

    public void updateUI(FirebaseUser user){
        //userName = findViewById(R.id.);

        if (user != null) {
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            String email = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();
            String phone = user.getPhoneNumber();
            for (UserInfo profile : user.getProviderData()) {
                // Id of the provider (ex: google.com)
                String providerId = profile.getProviderId();

                // UID specific to the provider
                String uid = profile.getUid();

                // Name, email address, and profile photo Url
                name = profile.getDisplayName();
                email = profile.getEmail();
                photoUrl = profile.getPhotoUrl();
                phone = profile.getPhoneNumber();
            }

            String uid = user.getUid();

            // Check if user's email is verified
            boolean emailVerified = user.isEmailVerified();
            Log.d(TAG, "Email Verified : " + emailVerified);

            FBUserId.setText(uid);
            textName.setText(name);
            textEmail.setText(email);
            if(phone != null) {
                et_phone.setText(phone);
            }
            if (emailVerified) {
                text_verify.setText("Verified");
                text_verify.setTextColor(Color.GREEN);
            }

            if (photoUrl != null) {
                Picasso.get()
                        .load(photoUrl)
                        .resize(300, 300)
                        .placeholder(android.R.drawable.sym_def_app_icon)
                        .error(android.R.drawable.sym_def_app_icon)
                        .into(mProfileImageView);

                // The user's ID, unique to the Firebase project. Do NOT use this value to
                // authenticate with your backend server, if you have one. Use
                // FirebaseUser.getIdToken() instead.

            }
        }
    }

    public void updatePassword(String emailAddress)
    {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        //String newPassword = "SOME-SECURE-PASSWORD";
        auth.sendPasswordResetEmail(emailAddress)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Email sent.");
                            Toast.makeText(ProfileActivity.this, "An email with password reset link has been sent to your registered email id.",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });


       /* user.updatePassword(newPassword)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User password updated.");
                        }
                    }
                });*/
    }

    public void updatePhone()
    {

    }




}
