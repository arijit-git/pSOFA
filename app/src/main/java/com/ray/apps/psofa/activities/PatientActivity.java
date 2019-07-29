package com.ray.apps.psofa.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ray.apps.psofa.R;
import com.ray.apps.psofa.other.SharedPrefs;
import com.ray.apps.psofa.other.Utils;

import java.util.HashMap;
import java.util.Map;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class PatientActivity extends BaseActivity {

    private static final String TAG = "A/Patient";
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private SharedPrefs sharedPrefs;
    private boolean mVisible;
    private TextView textViewPatientIdVal, textViewPatientNameVal, textViewPatientSexVal, textViewPatientAgeVal, warning_text, final_outcome_text, final_outcome_val;
    private TextView score_val_day1,score_val_day2,score_val_day3,score_val_day4,score_val_day5,score_val_day6,score_val_day7;
    private Button btnDiag;
    private Switch p_switch;
    FirebaseAuth mAuth ;
    FirebaseUser current_user;
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference usersRef = database.getReference("users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle("Modify Patient");


        mAuth = FirebaseAuth.getInstance();
        current_user = mAuth.getCurrentUser();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // perform whatever you want on back arrow click
                finish();
            }
        });

        //textViewPatientIdVal = findViewById(R.id.textView7);
        textViewPatientNameVal = findViewById(R.id.textView8);
        textViewPatientSexVal = findViewById(R.id.textViewSexVal);
        textViewPatientAgeVal = findViewById(R.id.textViewAgeVal);

        score_val_day1 = findViewById(R.id.score_val_day1);
        score_val_day2 = findViewById(R.id.score_val_day2);
        score_val_day3 = findViewById(R.id.score_val_day3);
        score_val_day4 = findViewById(R.id.score_val_day4);
        score_val_day5 = findViewById(R.id.score_val_day5);
        score_val_day6 = findViewById(R.id.score_val_day6);
        score_val_day7 = findViewById(R.id.score_val_day7);

        final_outcome_text = findViewById(R.id.textViewFinalOutcome);
        final_outcome_val = findViewById(R.id.textViewFinalOutcomeVal);


        btnDiag =  findViewById(R.id.btn_diag);
        p_switch = findViewById(R.id.p_switch);
        warning_text = findViewById(R.id.textView9);

        Window window = this.getWindow();
        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        // finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.colorPrimary));
        sharedPrefs = new SharedPrefs(this);

        //textViewPatientIdVal.setText(sharedPrefs.getPatientId());
        textViewPatientNameVal.setText(sharedPrefs.getBasicPatientName());
        textViewPatientSexVal.setText(sharedPrefs.getBasicPatientGender());
        textViewPatientAgeVal.setText(sharedPrefs.getBasicPatientAge());

        score_val_day1.setText(sharedPrefs.getScore1());
        score_val_day2.setText(sharedPrefs.getScore2());
        score_val_day3.setText(sharedPrefs.getScore3());
        score_val_day4.setText(sharedPrefs.getScore4());
        score_val_day5.setText(sharedPrefs.getScore5());
        score_val_day6.setText(sharedPrefs.getScore6());
        score_val_day7.setText(sharedPrefs.getScore7());
        Log.d(TAG, "Score day 1: " + sharedPrefs.getScore1());

        String [] values =
                {"Select Day Number","Day 1","Day 2","Day 3","Day 4","Day 5","Day 6","Day 7",};
        final Spinner spinner = (Spinner) findViewById(R.id.days_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, values);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(adapter);


        String [] values2 =
                {"Select Final Outcome","Survived","Not Survived"};
        final Spinner spinner2 = (Spinner) findViewById(R.id.result_spinner);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, values2);
        adapter2.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner2.setAdapter(adapter2);

        String outcome = sharedPrefs.getFinalOutcome();

        if(outcome.equalsIgnoreCase("survived") || outcome.equalsIgnoreCase("not survived"))
        {
            p_switch.setVisibility(View.GONE);
            spinner.setVisibility(View.GONE);
            spinner2.setVisibility(View.GONE);
            warning_text.setVisibility(View.GONE);
            btnDiag.setVisibility(View.GONE);


            final_outcome_text.setVisibility(View.VISIBLE);
            final_outcome_val.setVisibility(View.VISIBLE);
            final_outcome_val.setText(outcome);
        }

        p_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    spinner.setVisibility(View.GONE);
                    warning_text.setVisibility(View.GONE);
                    spinner2.setVisibility(View.VISIBLE);


                    btnDiag.setText(R.string.final_result_button_text);
                    btnDiag.setTextColor(Color.RED);
                }
                else
                {
                    spinner2.setVisibility(View.GONE);
                    spinner.setVisibility(View.VISIBLE);
                    warning_text.setVisibility(View.VISIBLE);

                    btnDiag.setText(R.string.diag_start_btn_text);
                    btnDiag.setTextColor(Color.WHITE);
                }
            }
        });


        btnDiag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(btnDiag.getText().toString().equalsIgnoreCase("Start Diagnosis")) {
                    if(spinner.getSelectedItemPosition() > 0)
                    {
                        //sharedPrefs.savePatientId(getApplicationContext(), textViewPatientIdVal.getText().toString());
                        sharedPrefs.setPatientDisplayName(getApplicationContext(), textViewPatientNameVal.getText().toString());
                        sharedPrefs.setPatientAge(getApplicationContext(), textViewPatientAgeVal.getText().toString());
                        sharedPrefs.saveBasicPatientDayNum(getApplicationContext(), spinner.getSelectedItem().toString());
                        sharedPrefs.setPatientModification(getApplicationContext(), "ON");

                        startActivity(new Intent(PatientActivity.this, DiagnosisActivity.class));
                        finish();
                    }
                    else
                    {
                        Utils.showToast(getApplicationContext(), "You must select a day for diagnosis.");
                    }

                }
                if(btnDiag.getText().toString().equalsIgnoreCase("Mark Final Outcome"))
                {
                    if(spinner2.getSelectedItemPosition() > 0) {

                        updateFinalOutcome(spinner2.getSelectedItem().toString());

                    }
                    else
                    {
                        Utils.showToast(getApplicationContext(), "You must select a final outcome to proceed.");
                    }

                }
            }
        });

    }

    public void updateFinalOutcome(String result)
    {
        final String res = result;
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        // set title
        alertDialogBuilder.setTitle("Update Final Outcome");
        // set dialog message
        alertDialogBuilder
                .setMessage("This cannot be changed at later stage.\nAre you sure you want to continue?")
                .setCancelable(false)
                .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        DatabaseReference patientRef = usersRef.child(current_user.getUid() + "/patients").child(sharedPrefs.getPatientId());
                        Map<String, Object> patientUpdates = new HashMap<>();
                        patientUpdates.put("final_result", res);

                        patientRef.updateChildren(patientUpdates);
                        Utils.showToast(getApplicationContext(), "Marked Final Outcome as : " + res);
                        finish();
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

}
