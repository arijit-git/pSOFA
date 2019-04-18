package com.ray.apps.psofa.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.ray.apps.psofa.R;

import java.util.List;


public class HomeFragment extends Fragment {

    private static final String TAG = "F/Home";
    private FirebaseAuth mAuth;
    private TextView welcomeText;
    private TextView verifyEmail;


    RecyclerView recyclerview;

    private DatabaseReference mDatabase;
    private DatabaseReference mPatientsReference;
    private ValueEventListener mPatientsListener;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActivity().setTitle("Home");
       // welcomeText = getActivity().findViewById(R.id.textWelcome);
        //user.sendEmailVerification();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        //TextView tv = (TextView) view.findViewById(R.id.textWelcome);


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
