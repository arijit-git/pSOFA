package com.ray.apps.psofa.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.ray.apps.psofa.R;
import com.ray.apps.psofa.activities.RegisterPatientActivity;
import com.ray.apps.psofa.adapters.PatientListAdapter;
import com.ray.apps.psofa.adapters.RecyclerItemTouchHelper;
import com.ray.apps.psofa.model.Patient;
import com.ray.apps.psofa.other.Utils;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


public class PatientsViewFragment extends Fragment
        implements SwipeRefreshLayout.OnRefreshListener,
        RecyclerItemTouchHelper.RecyclerItemTouchHelperListener
{

    private static final String TAG = "F/Patients";

    private ProgressBar mProgressBar;
    private RecyclerView recyclerView;
    private ProgressDialog dialog;
    private List<Patient> patientList;
    private PatientListAdapter mAdapter;
    private FirebaseUser currentUser;
    private FloatingActionButton fab;
    TextView text_totalPatients, val_totalPatients, scoreValMax, scoreValMin, final_result;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private DatabaseReference mPatientsReference;
    private ValueEventListener mPatientsListener;
    SwipeRefreshLayout pullToRefresh;
    private long itemCount = 0;

    Utils utils;
    int refreshCounter = 1;

    public PatientsViewFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        utils = new Utils(getActivity());
        View view = inflater.inflate(R.layout.fragment_patients_view, container, false);

        text_totalPatients = view.findViewById(R.id.text_totalPatients);
        val_totalPatients = view.findViewById(R.id.val_totalPatients);
        scoreValMax = view.findViewById(R.id.scoreValMax);
        scoreValMin = view.findViewById(R.id.scoreValMin);
        final_result = view.findViewById(R.id.life_res_val);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getActivity(), RegisterPatientActivity.class));
            }
        });

            recyclerView = (RecyclerView) view.findViewById(R.id.rview);

            patientList = new ArrayList<>();

            mAdapter = new PatientListAdapter(getContext(), patientList);

            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
            recyclerView.setAdapter(mAdapter);

            // adding item touch helper
            // only ItemTouchHelper.LEFT added to detect Right to Left swipe
            // if you want both Right -> Left and Left -> Right
            // add pass ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT as param
            ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
            new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);

            //setting an setOnRefreshListener on the SwipeDownLayout
            pullToRefresh = view.findViewById(R.id.pullToRefresh);
            pullToRefresh.setOnRefreshListener(this);

           /* dialog = new ProgressDialog(getActivity());
            dialog.setTitle("loading");
            dialog.setCancelable(true);
            dialog.setMessage("Please wait ...");*/

            mAuth = FirebaseAuth.getInstance();
            currentUser = mAuth.getCurrentUser();

           //preparePatientList();
        //new AsyncPatientTask();
        mProgressBar.setVisibility(View.VISIBLE);


            if(!isConnected()) {
                Utils.showNoInternetToast(getContext());

            }
        // Inflate the layout for this fragment
        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        getActivity().setTitle("My Patients");

    }

    @Override
    public void onResume() {
        super.onResume();
        preparePatientList();
    }

    @Override
    public void onStop() {
        super.onStop();

        if (mPatientsListener != null) {
            mPatientsReference.removeEventListener(mPatientsListener);
        }
    }

    @Override
    public void onRefresh(){
        if(!isConnected()) {
            Utils.showNoInternetToast(getContext());

        }
        pullToRefresh.setRefreshing(true);
        refreshList();
    }


    private  void refreshList(){
        preparePatientList();
        pullToRefresh.setRefreshing(false);

    }

    /**
     * callback when recycler view is swiped
     * item will be removed on swiped
     * undo option will be provided in snackbar to restore the item
     */
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof PatientListAdapter.MyViewHolder) {
            // get the removed item name to display it in snack bar
            final String name = patientList.get(viewHolder.getAdapterPosition()).getName();
            String patientId = patientList.get(viewHolder.getAdapterPosition()).getId();

            // backup of removed item for undo purpose
            final Patient deletedItem = patientList.get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();

            // remove the item from recycler view
            mAdapter.removeItem(viewHolder.getAdapterPosition());

           // mPatientsReference.
            // showing snack bar with Undo option
            Snackbar snackbar = Snackbar
                    .make(getView(), name + " removed from list!", Snackbar.LENGTH_LONG);
            mDatabase = FirebaseDatabase.getInstance().getReference("users");
            mPatientsReference = mDatabase.child(currentUser.getUid()).child("patients");
            //Query applesQuery = ref.child("patients").orderByChild("name").equalTo(name);
            Query applesQuery = mPatientsReference.orderByChild("id").equalTo(patientId);

            applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    //Log.e(TAG, " Id : " + dataSnapshot.getKey());

                    for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                        //Log.d(TAG, "Remove Id : " + appleSnapshot.getKey());
                        appleSnapshot.getRef().removeValue();
                        Log.d(TAG, "Remove : Success for " + name);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.e(TAG, "onCancelled", databaseError.toException());
                }
            });
            /*snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // undo is selected, restore the deleted item
                    mAdapter.restoreItem(deletedItem, deletedIndex);
                }
            });*/
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
        }
    }


    /**
     *
     */
    public void preparePatientList()
    {
        if(currentUser != null)
        {
            mProgressBar.setVisibility(View.VISIBLE);
            // fetching patients
            mDatabase = FirebaseDatabase.getInstance().getReference("users");
            mPatientsReference = mDatabase.child(currentUser.getUid()).child("patients");

            // fetching patients
            ValueEventListener patientsListener =  mPatientsReference.addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    patientList.clear();
                    if(dataSnapshot.exists())
                    {
                        itemCount = dataSnapshot.getChildrenCount();
                        Log.d(TAG, " Total Patients : " + itemCount);
                        val_totalPatients.setText(itemCount + "");

                        for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Patient patient = snapshot.getValue(Patient.class);
                            patientList.add(patient);
                        }
                        //Collections.reverse(patientList);
                        // refreshing recycler view
                        mAdapter.notifyDataSetChanged();
                    }
                    mProgressBar.setVisibility(View.GONE);
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.d(TAG, "Operation Error :  " + databaseError.getDetails());
                    mProgressBar.setVisibility(View.GONE);
                }
            });

            // copy for removing at onStop()
            mPatientsListener = patientsListener;
//            dialog.dismiss();
        }

    }


    public class AsyncPatientTask extends AsyncTask<String, Void, Integer> {

        @Override
        protected Integer doInBackground(String... params) {
            Integer result = 0;
            try {
                preparePatientList();
                result = 1; // Successful

            } catch (Exception e) {
                Log.d(TAG, e.getLocalizedMessage());
            }
            return result;
        }

        @Override
        protected void onPostExecute(Integer result) {
            // Download complete. Let us update UI

            mProgressBar.setVisibility(View.GONE);
        }
    }

    public boolean isConnected() {

        ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;

    }

}
