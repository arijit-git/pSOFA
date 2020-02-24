package com.ray.apps.psofa.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ray.apps.psofa.R;

import androidx.fragment.app.Fragment;

public class DashboardFragment extends Fragment {
    private static final String TAG = "pSOFADashboard";
    private ProgressBar mProgressBar;
    public DashboardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        getActivity().setTitle("pSOFA Dashboard");

        mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");

        /*mProgressBar.setVisibility(View.VISIBLE);
        Query query = reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("patients");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // dataSnapshot is the "issue" node with all children with id 0
                    for (DataSnapshot issue : dataSnapshot.getChildren()) {
                        // do something with the individual "issues"
                        Log.w(TAG, "data : " + issue.getChildrenCount() + " child : " + issue.getValue());
                    }
                    mProgressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                mProgressBar.setVisibility(View.GONE);
                Log.w(TAG, "Error : " + databaseError.getDetails());
            }
        });*/

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

}
