package com.ray.apps.psofa.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ray.apps.psofa.R;
import com.ray.apps.psofa.other.SharedPrefs;

public class SettingsActivity extends AppCompatActivity {


    private static final String TAG = "pSOFAAccountSettings";
    private Button btnDelAcct, btnUpdatePassword, btnResetPassword;
    private FirebaseAuth mAuth;
    private FirebaseUser current_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btnDelAcct = findViewById(R.id.btnDelAccount);
        //btnUpdatePassword = findViewById(R.id.btnUpdatePswd);
        btnResetPassword = findViewById(R.id.btnResetPswd);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Window window = this.getWindow();
        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        // finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.colorPrimary));

        setTitle("Account Settings");

        // firebase
        mAuth = FirebaseAuth.getInstance();
        current_user = mAuth.getCurrentUser();

        btnDelAcct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteUserAccount();
            }
        });
        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth auth = FirebaseAuth.getInstance();
                String emailAddress = current_user.getEmail();

                if (emailAddress != null) {
                    auth.sendPasswordResetEmail(emailAddress)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(SettingsActivity.this, "A password reset link sent to your registered email.",
                                                Toast.LENGTH_LONG).show();
                                        Log.d(TAG, "Password Reset Email sent.");
                                    }
                                }
                            });
                }
            }
        });

/*        btnUpdatePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    showUpdatePasswordLayout();
            }
        });*/
    }


    private void deleteUserAccount(){

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        // set title
        alertDialogBuilder.setTitle("Delete Account?");
        // set dialog message
        alertDialogBuilder
                .setMessage("All the account data will be deleted.\nAre you sure you want to completely remove this account?")
                .setCancelable(false)
                .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, close
                        // current activity
                        //SignInActivity.this.finish();
                        AuthUI.getInstance()
                                .delete(getApplicationContext())
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            // Deletion succeeded
                                            Log.d(TAG, "Delete Account : Success");
                                            Toast.makeText(SettingsActivity.this, "Account deleted !",
                                                    Toast.LENGTH_LONG).show();
                                            signOut();
                                        } else {
                                            // Deletion failed
                                            Log.d(TAG, "Delete Account : Failed");
                                            Toast.makeText(SettingsActivity.this, "Could not delete account. Error: " + task.getException(),
                                                    Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                        // delete account data
                        DatabaseReference mPatientsReference = FirebaseDatabase.getInstance().getReference("users").child(current_user.getUid());
                        mPatientsReference.removeValue(new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                Log.d(TAG, "Account data remove : success for User : " + current_user.getDisplayName());
                            }
                        });
                        //Toast.makeText(NavActivity.this, "Account deleted !", Toast.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton("No",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        dialog.cancel();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    //method to logout
    private void signOut(){
        new SharedPrefs(this).clear();
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        // user is now signed out
                        startActivity(new Intent(SettingsActivity.this, SignInActivity.class));
                        finish();
                    }
                });
    }

    /*public void showUpdatePasswordLayout()
    {
        LayoutInflater factory = LayoutInflater.from(this);

        //text_entry is an Layout XML file containing two text field to display in alert dialog
        final View textEntryView = factory.inflate(R.layout.dialog_update_password, null);

        final EditText input1 = (EditText) textEntryView.findViewById(R.id.editTextOldPass);
        final EditText input2 = (EditText) textEntryView.findViewById(R.id.editTextNewPass);


        input1.setText("", TextView.BufferType.EDITABLE);
        input2.setText("", TextView.BufferType.EDITABLE);

        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Update Password").setView(textEntryView);
        alert.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int whichButton) {

                        Log.i("AlertDialog","TextEntry 1 Entered "+input1.getText().toString());
                        Log.i("AlertDialog","TextEntry 2 Entered "+input2.getText().toString());
                        *//* User clicked OK so do some stuff *//*

                       updatePassword(input1.getText().toString(), input2.getText().toString(), current_user.getProviderId());
                    }
                }).setNegativeButton("CANCEL",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int whichButton) {
                        *//*
                         * User clicked cancel so do some stuff
                         *//*
                    }
                });
        alert.show();
    }*/

    /*public void updatePassword(String oldPass, String newPass, String providerId)
    {
        GoogleSignInAccount acct = null;
        AuthCredential credential = null;
        if(providerId.contains("google")) {
            //AuthCredential credential = EmailAuthProvider.getCredential(current_user.getEmail(), oldPass);
            // Get the account
            acct = GoogleSignIn.getLastSignedInAccount(this);
            if (acct != null) {
                credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
                }
        if(providerId.contains("firebase")) {
            credential = EmailAuthProvider.getCredential(current_user.getEmail(), oldPass);
        }
        current_user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                            showUpdatePasswordLayout();
                            current_user.updatePassword(newPass).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d(TAG, "Password updated");
                                        Toast.makeText(SettingsActivity.this, "Password updated successfully !",
                                                Toast.LENGTH_LONG).show();
                                    } else {
                                        Log.e(TAG, "Error. password not updated");
                                    }
                                }
                            });
                            } else {
                                Log.e(TAG, "Error auth failed");
                            }
                        }
                    });

        }
        // Prompt the user to re-provide their sign-in credentials

    }*/


    public boolean isReAuthenticatedUser(String oldPass)
    {
        boolean authenticated = false;
        GoogleSignInAccount acct = null;
        AuthCredential credential = null;
        String providerId = current_user.getProviderId();

        if(providerId.contains("google")) {
            //AuthCredential credential = EmailAuthProvider.getCredential(current_user.getEmail(), oldPass);
            // Get the account
            acct = GoogleSignIn.getLastSignedInAccount(this);
            if (acct != null) {
                credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
                current_user.reauthenticate(credential)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "Google re-auth success !");

                                } else {
                                    Log.e(TAG, "google Error re-auth failed");
                                }
                            }
                        });
            }
            if(!oldPass.isEmpty() && providerId.contains("firebase")) {
                credential = EmailAuthProvider.getCredential(current_user.getEmail(), oldPass);
                current_user.reauthenticate(credential)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {


                                } else {
                                    Log.e(TAG, "Error auth failed");
                                }
                            }
                        });
            }


        }

        return authenticated;
    }
}
