package com.ray.apps.psofa.activities;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.badoualy.stepperindicator.StepperIndicator;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ray.apps.psofa.R;
import com.ray.apps.psofa.fragments.DiagnosisStepFiveFragment;
import com.ray.apps.psofa.fragments.DiagnosisStepFourFragment;
import com.ray.apps.psofa.fragments.DiagnosisStepOneFragment;
import com.ray.apps.psofa.fragments.DiagnosisStepSixFragment;
import com.ray.apps.psofa.fragments.DiagnosisStepThreeFragment;
import com.ray.apps.psofa.fragments.DiagnosisStepTwoFragment;
import com.ray.apps.psofa.model.Patient;
import com.ray.apps.psofa.other.SharedPrefs;
import com.ray.apps.psofa.other.pSOFACalculator;

public class DiagnosisActivity extends BaseActivity
        implements
        DiagnosisStepOneFragment.OnStepOneListener,
        DiagnosisStepTwoFragment.OnStepTwoListener,
        DiagnosisStepThreeFragment.OnStepThreeListener,
        DiagnosisStepFourFragment.OnStepFourListener,
        DiagnosisStepFiveFragment.OnStepFiveListener,
        DiagnosisStepSixFragment.OnStepSixListener
        {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private ProgressBar mProgressBar;
    private static final String TAG = "DIAG/Activity";
    private SharedPrefs sharedPrefs;
    private pSOFACalculator calculator;
    private static FirebaseAuth mAuth;
    private double finalScore;
    private TextView textViewPatientVal, textViewDayVal;

    private StepperIndicator stepperIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagnosis);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Window window = this.getWindow();
        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        // finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.colorPrimary));

        setTitle("SOFA Diagnosis");
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        stepperIndicator = findViewById(R.id.stepperIndicator);
        textViewPatientVal = findViewById(R.id.textViewPatientVal);
        textViewDayVal = findViewById(R.id.textViewDayVal);

        stepperIndicator.showLabels(true);
        stepperIndicator.setViewPager(mViewPager);
        // or keep last page as "end page"
        stepperIndicator.setViewPager(mViewPager, mViewPager.getAdapter().getCount() - 1); //
        sharedPrefs = new SharedPrefs(this);

        textViewPatientVal.setText(sharedPrefs.getPatientDisplayName());
        textViewDayVal.setText(sharedPrefs.getBasicPatientDayNum());
        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (position) {
                case 0:
                    return DiagnosisStepOneFragment.newInstance("", "");
                case 1:
                    return DiagnosisStepTwoFragment.newInstance("", "");
                case 2:
                    return DiagnosisStepThreeFragment.newInstance("", "");
                case 3:
                    return DiagnosisStepFourFragment.newInstance("", "");
                case 4:
                    return DiagnosisStepFiveFragment.newInstance("", "");
                case 5:
                    return DiagnosisStepSixFragment.newInstance("", "");
            }

            return null;
        }

        @Override
        public int getCount() {
            // Show 6 total pages.
            return 6;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Step 1";
                case 1:
                    return "Step 2";
                case 2:
                    return "Step 3";
                case 3:
                    return "Step 4";
                case 4:
                    return "Step 5";
                case 5:
                    return "Step 6";
            }
            return null;
        }
    }


    @Override
    public void onNextPressed(Fragment fragment) {
        if (fragment instanceof DiagnosisStepOneFragment) {
            mViewPager.setCurrentItem(1, true);
        } else
        if (fragment instanceof DiagnosisStepTwoFragment) {
            mViewPager.setCurrentItem(2, true);
        }
        else
        if (fragment instanceof DiagnosisStepThreeFragment) {
            mViewPager.setCurrentItem(3, true);
        }
        else
        if (fragment instanceof DiagnosisStepFourFragment) {
            mViewPager.setCurrentItem(4, true);
        }
        else
        if (fragment instanceof DiagnosisStepFiveFragment) {
            mViewPager.setCurrentItem(5, true);
        }
        else if (fragment instanceof DiagnosisStepSixFragment) {

            //Toast.makeText(this, "Diagnosis Complete!", Toast.LENGTH_SHORT).show();
            hideKeyboard(this);
            mProgressBar.setVisibility(View.VISIBLE);
            // restore prefs
            //sharedPrefs = new SharedPrefs(this);
            calculator = new pSOFACalculator();
            double RespScore = calculator.getRespiratoryScore(Integer.parseInt(sharedPrefs.getRespiratoryInput()), sharedPrefs.getRespiratoryInputType());
            double CoagScore = calculator.getCoagulationScore(Integer.parseInt(sharedPrefs.getCoagulatoryInput()));
            double HepaticScore = calculator.getHepaticScore(Double.parseDouble(sharedPrefs.getHepaticInput()));

            int bp = 0;
            Double dopamine=0.0, epinephrine=0.0, nor_epinephrine=0.0;

            String key = sharedPrefs.getCardioFinalKey();
            String keyVal = sharedPrefs.getCardioFinalKeyValue();

            Log.d(TAG, "Key : " + key + " KeyVal : " + keyVal + " Age : " + sharedPrefs.getBasicPatientAge());
            double cardioScore = 0.0;

            if(key.equalsIgnoreCase("BP"))
            {
                cardioScore = calculator.getFinalScoreForBP(Integer.parseInt(sharedPrefs.getBasicPatientAge()), keyVal);
                //bp = Integer.parseInt(sharedPrefs.getCardioBPInput());
            }
            else
            if(key.equalsIgnoreCase("DOPA"))
            {
                cardioScore = calculator.getFinalScoreForDopamine(keyVal);
                //dopamine = Double.parseDouble(sharedPrefs.getCardioDopamineInput());
            }
            else
            if(key.equalsIgnoreCase("EPI"))
            {
                cardioScore = calculator.getFinalScoreForEpinephrine(keyVal);
                //epinephrine = Double.parseDouble(sharedPrefs.getCardioEpinephrineInput());
            }
            else
            if(key.equalsIgnoreCase("NOR-EPI"))
            {
                cardioScore = calculator.getFinalScoreForNorEpimephrine(keyVal);
                //nor_epinephrine = Double.parseDouble(sharedPrefs.getCardioNorEpinephrineInput());
            }
            else
            if(key.equalsIgnoreCase("DOBU"))
            {
                cardioScore = calculator.getFinalScoreForDobutamine();
            }

            /*double CardioScore = calculator.getCardioVascularScore(Integer.parseInt(sharedPrefs.getBasicPatientAge()),
                    bp,
                    dopamine,
                    epinephrine,
                    nor_epinephrine,
                    sharedPrefs.getCardioDobutamineInput());*/
            double NeuroScore = calculator.getNeurologicalScore(Integer.parseInt(sharedPrefs.getNeurologicInput()));
            double RenalScore = calculator.getRenalScore(Integer.parseInt(sharedPrefs.getRenalAgeInput()), Double.parseDouble(sharedPrefs.getRenalCreatinineInput()));

            Log.d(TAG, "RespScore = " + RespScore + " CoagScore = " + CoagScore + " HepaticScore = " + HepaticScore
            + " CardioScore = " + cardioScore + " NeuroScore = " + NeuroScore + " RenalScore = " + RenalScore);
            finalScore = RespScore + CoagScore + HepaticScore + cardioScore + NeuroScore + RenalScore;
            Log.d(TAG, "Final pSOFA Score : " + finalScore);
            mProgressBar.setVisibility(View.GONE);
            Toast.makeText(DiagnosisActivity.this, "pSOFA Score : " + finalScore, Toast.LENGTH_LONG).show();
            //View view = findViewById(android.R.id.title);
           // Snackbar snackbar = Snackbar.make(view,"SOFA Score : " + finalScore, Snackbar.LENGTH_LONG);
           // snackbar.show();

            // save to cloud
            if (sharedPrefs.getPatientModificationStatus().equalsIgnoreCase("ON"))
            {
                updatePatient(sharedPrefs.getPatientId(), sharedPrefs.getBasicPatientDayNum());
            }
            else {
                saveCompletePatientToCloudDatabase();
            }
            //saveCompletePatientToCloudDatabase();
            sharedPrefs.clear();
            finish();
            //finish();
        }
    }

    @Override
    public void onBackPressed(Fragment fragment) {
        if (fragment instanceof DiagnosisStepTwoFragment) {
            mViewPager.setCurrentItem(0, true);
        }
        else
        if (fragment instanceof DiagnosisStepThreeFragment) {
            mViewPager.setCurrentItem(1, true);
        }
        else
        if (fragment instanceof DiagnosisStepFourFragment) {
            mViewPager.setCurrentItem(2, true);
        }
        else
        if (fragment instanceof DiagnosisStepFiveFragment) {
            mViewPager.setCurrentItem(3, true);
        }
        else
        if (fragment instanceof DiagnosisStepSixFragment) {
            mViewPager.setCurrentItem(4, true);
        }
    }


        public static void hideKeyboard(Activity activity) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            //Find the currently focused view, so we can grab the correct window token from it.
            View view = activity.getCurrentFocus();
            //If no view currently has focus, create a new one, just so we can grab a window token from it
            if (view == null) {
                view = new View(activity);
            }
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        private  void saveCompletePatientToCloudDatabase()
        {
            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("users");
            String patientId = mDatabase.push().getKey();

            sharedPrefs.savePatientId(this, patientId);

            FirebaseUser currentUser = mAuth.getCurrentUser();
            String uid = currentUser.getUid();

            // creating patient object
            Patient patient = new Patient();

            patient.setId(patientId);
            patient.setName(sharedPrefs.getBasicPatientName());
            patient.setAge(Integer.parseInt(sharedPrefs.getBasicPatientAge()));
            patient.setGender(sharedPrefs.getBasicPatientGender());

            if(sharedPrefs.getBasicPatientDayNum().contains("1"))
            {
                patient.setScore_day1(finalScore + "");
            }
            if(sharedPrefs.getBasicPatientDayNum().contains("2"))
            {
                patient.setScore_day2(finalScore + "");
            }
            if(sharedPrefs.getBasicPatientDayNum().contains("3"))
            {
                patient.setScore_day3(finalScore + "");
            }
            if(sharedPrefs.getBasicPatientDayNum().contains("4"))
            {
                patient.setScore_day4(finalScore + "");
            }
            if(sharedPrefs.getBasicPatientDayNum().contains("5"))
            {
                patient.setScore_day5(finalScore + "");
            }
            if(sharedPrefs.getBasicPatientDayNum().contains("6"))
            {
                patient.setScore_day6(finalScore + "");
            }
            if(sharedPrefs.getBasicPatientDayNum().contains("7"))
            {
                patient.setScore_day7(finalScore + "");
            }

            // pushing patient to 'patients' node using the patientId
            //int i = 0;
            mDatabase.child(uid).child("patients").child(patientId).setValue(patient).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        //
                        Log.d(TAG, "SaveToCloudDatabase:success");

                    } else {

                        Log.w(TAG, "SaveToCloudDatabase:failure", task.getException());

                    }
                }
            });

        }

        private  void updatePatient(String patientId, String dayNum)
        {
            FirebaseUser currentUser = mAuth.getCurrentUser();
            String uid = currentUser.getUid();


            DatabaseReference updateData = FirebaseDatabase.getInstance()
                    .getReference("users")
                    .child(uid).child("patients").child(patientId);


            if(dayNum.contains("1"))
            {
                updateData.child("score_day1").setValue(finalScore + "");
            }
            if(dayNum.contains("2"))
            {
                updateData.child("score_day2").setValue(finalScore + "");
            }
            if(dayNum.contains("3"))
            {
                updateData.child("score_day3").setValue(finalScore + "");
            }
            if(dayNum.contains("4"))
            {
                updateData.child("score_day4").setValue(finalScore + "");
            }
            if(dayNum.contains("5"))
            {
                updateData.child("score_day5").setValue(finalScore + "");
            }
            if(dayNum.contains("6"))
            {
                updateData.child("score_day6").setValue(finalScore + "");
            }
            if(dayNum.contains("7"))
            {
                updateData.child("score_day7").setValue(finalScore + "");
            }


        }
  }