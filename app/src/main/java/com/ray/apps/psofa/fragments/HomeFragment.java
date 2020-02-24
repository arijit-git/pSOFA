package com.ray.apps.psofa.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.ray.apps.psofa.R;
import com.ray.apps.psofa.activities.NavActivity;
import com.ray.apps.psofa.activities.PatientActivity;
import com.ray.apps.psofa.activities.RegisterPatientActivity;
import com.ray.apps.psofa.other.Utils;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


public class HomeFragment extends Fragment {

    private static final String TAG = "F/Home";
    private FirebaseAuth mAuth;
    private TextView welcomeText, patientsText, addPatientText,  dashText, predictText;
    private TextView verifyEmail;
    private ImageView patientsView, addPatientView, dasboardView, predictView;

    private DatabaseReference mDatabase;
    private DatabaseReference mPatientsReference;
    private ValueEventListener mPatientsListener;
    private AdView mAdView;
    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActivity().setTitle("Home");

        //user.sendEmailVerification();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        patientsView = view.findViewById(R.id.patientsImgView);
        addPatientView = view.findViewById(R.id.addImgView);
        dasboardView = view.findViewById(R.id.dashImgView);
        predictView = view.findViewById(R.id.predictImgView);

        patientsText = view.findViewById(R.id.patientsText);
        addPatientText = view.findViewById(R.id.addPatientText);
        dashText = view.findViewById(R.id.dashText);
        predictText = view.findViewById(R.id.predictText);

        welcomeText = view.findViewById(R.id.welcomeTextView);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if(user != null) {
            String name = user.getDisplayName();
            welcomeText.setText("Welcome Dr. " + name + " !");
        }

        patientsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.flContent, new PatientsViewFragment()).addToBackStack("home_fragment");
                fragmentTransaction.commit();
            }
        });
        patientsText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.flContent, new PatientsViewFragment()).addToBackStack("home_fragment");
                fragmentTransaction.commit();
            }
        });

        addPatientView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentRegister = new Intent(getActivity(), RegisterPatientActivity.class);
                startActivity(intentRegister);
            }
        });
        addPatientText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentRegister = new Intent(getActivity(), RegisterPatientActivity.class);
                startActivity(intentRegister);
            }
        });
        dasboardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.showToast(getContext(), "Coming Soon !");
            }
        });
        dashText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.showToast(getContext(), "Coming Soon !");
            }
        });
        predictView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.showToast(getContext(), "Coming Soon !");
            }
        });
        predictText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.showToast(getContext(), "Coming Soon !");
            }
        });


        MobileAds.initialize(getContext(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        // get ref to auth
       // mAuth = FirebaseAuth.getInstance();
        //FirebaseUser user = mAuth.getCurrentUser();
        //String name = user.getDisplayName().
        /*if(user != null) {
            tv.setText("Welcome to pSOFA !");
        }*/
        return view;
    }


    /*public void setText(String text){
        TextView textView = (TextView) getView().findViewById(R.id.textWelcome);
        textView.setText(text);
    }*/

    @Override
    public void onStop() {
        super.onStop();

        if (mPatientsListener != null) {
            mPatientsReference.removeEventListener(mPatientsListener);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if(getView() == null){
            return;
        }

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){
                    // handle back button's click listener
                    Intent intentRegister = new Intent(getActivity(), NavActivity.class);
                    startActivity(intentRegister);
                    return true;
                }
                return false;
            }
        });
    }

    private void updateUI(FirebaseUser user) {
        /*if (user != null) {
            txtStatus.setText("User Email: " + user.getEmail());
            txtDetail.setText("Firebase User ID: " + user.getUid());

            findViewById(R.id.email_password_buttons).setVisibility(View.GONE);
            findViewById(R.id.email_password_fields).setVisibility(View.GONE);
            findViewById(R.id.layout_signed_in_control).setVisibility(View.VISIBLE);

        } else {
            txtStatus.setText("Signed Out");
            txtDetail.setText(null);

            findViewById(R.id.email_password_buttons).setVisibility(View.VISIBLE);
            findViewById(R.id.email_password_fields).setVisibility(View.VISIBLE);
            findViewById(R.id.layout_signed_in_control).setVisibility(View.GONE);
        }*/
    }
}
