package com.ray.apps.psofa.fragments;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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
import com.ray.apps.psofa.activities.SignInActivity;
import com.ray.apps.psofa.other.SharedPrefs;
import com.ray.apps.psofa.other.Utils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

public class SettingsFragment extends Fragment {

    private static final String TAG = "F/pSOFAAccountSettings";
    private Button btnDelAcct, btnUpdatePassword, btnResetPassword;
    private FirebaseAuth mAuth;
    private FirebaseUser current_user;
    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        getActivity().setTitle("Account Settings");

        btnDelAcct = view.findViewById(R.id.btnDelAccount);
        //btnUpdatePassword = view.findViewById(R.id.btnUpdatePswd);
        btnResetPassword = view.findViewById(R.id.btnResetPswd);

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
                                        Utils.showToast(getContext(), "A password reset link sent to your registered email.");
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
        // Inflate the layout for this fragment
        return view;
    }

    private void deleteUserAccount(){

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
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
                                .delete(getContext())
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            // Deletion succeeded
                                            Log.d(TAG, "Delete Account : Success");
                                            Utils.showToast(getContext(), "Account deleted !");
                                            signOut();
                                        } else {
                                            // Deletion failed
                                            Log.d(TAG, "Delete Account : Failed");
                                            Utils.showToast(getContext(), "Could not delete account. Error: " + task.getException());
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
        new SharedPrefs(getContext()).clear();
        AuthUI.getInstance()
                .signOut(getContext())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        // user is now signed out
                        startActivity(new Intent(getContext(), SignInActivity.class));
                        getActivity().finish();
                    }
                });
    }

    public boolean isReAuthenticatedUser(String oldPass)
    {
        boolean authenticated = false;
        GoogleSignInAccount acct = null;
        AuthCredential credential = null;
        String providerId = current_user.getProviderId();

        if(providerId.contains("google")) {
            //AuthCredential credential = EmailAuthProvider.getCredential(current_user.getEmail(), oldPass);
            // Get the account
            acct = GoogleSignIn.getLastSignedInAccount(getContext());
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

}
