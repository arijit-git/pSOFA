package com.ray.apps.psofa.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ray.apps.psofa.R;
import com.ray.apps.psofa.model.Patient;
import com.ray.apps.psofa.other.SharedPrefs;
import com.ray.apps.psofa.other.Utils;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

public class RegisterPatientActivity extends AppCompatActivity {

    private static final String TAG = "F/Patient";
    private DatabaseReference mDatabase;
    private Button savePatient;
    EditText patientName, patientAge;
    private RadioGroup radioGroup;
    private Switch aSwitch;
    private static FirebaseAuth mAuth;
    private String radio_text ="";
    private SharedPrefs sharedPrefs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_patient);

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
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.design_default_color_secondary));

        setTitle("Add Patient");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // perform whatever you want on back arrow click
                finish();
            }
        });

        mAuth = FirebaseAuth.getInstance();
        sharedPrefs = new SharedPrefs(this);
        sharedPrefs.clear();

        savePatient = findViewById(R.id.patient_register_button);
        patientName = findViewById(R.id.patient_name);
        patientAge = findViewById(R.id.patient_age);
        radioGroup = findViewById(R.id.radioGroup);
        aSwitch = findViewById(R.id.p_switch);

        String [] values =
                {"Select Day Number","Day 1","Day 2","Day 3","Day 4","Day 5","Day 6","Day 7",};
        final Spinner spinner = (Spinner) findViewById(R.id.days_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, values);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(adapter);

        // default hide

        spinner.setVisibility(View.GONE);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                switch(checkedId)
                {
                    case R.id.radio_male:
                        radio_text = "male";
                        break;
                    case R.id.radio_female:
                        radio_text = "female";
                        break;
                }
            }
        });

        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    spinner.setVisibility(View.VISIBLE);
                    savePatient.setText(R.string.patient_register_more_button_text);
                    savePatient.setTextColor(Color.RED);
                }
                else
                {
                    spinner.setVisibility(View.GONE);
                    savePatient.setText(R.string.patient_register_button_text);
                    savePatient.setTextColor(Color.WHITE);
                }
            }
        });

        // store to database onclick
        savePatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validatePatient())
                {
                    String name = patientName.getText().toString();
                    int age = Integer.parseInt(patientAge.getText().toString());
                    String gender = radio_text;

                    ProgressDialog dialog = ProgressDialog.show(RegisterPatientActivity.this, "Loading...", "Please wait...", true);
                    //String name = patientName.getText().toString();
                   // int age = Integer.parseInt(patientAge.getText().toString());

                    Log.d(TAG, "Entered patient details : [ Name : " + name + " Age : " + age + " Gender : " + gender);

                    if(savePatient.getText().toString().equalsIgnoreCase(getString(R.string.patient_register_button_text)))
                    {
                        saveToCloudDatabase(name, age, radio_text);
                        dialog.dismiss();
                        Log.d(TAG, "Patient saved successfully.");
                        Utils.showToast(getApplicationContext(), "Patient saved successfully.");
                        finish();
                    }
                    else
                        if(savePatient.getText().toString().equalsIgnoreCase(getString(R.string.patient_register_more_button_text)))
                        {
                            dialog.dismiss();
                            String dayNo = spinner.getSelectedItem().toString();

                            if(dayNo.isEmpty() || spinner.getSelectedItemPosition() == 0)
                            {
                                Utils.showToast(getApplicationContext(), "You must select a day for diagnosis.");
                            }
                            else {
                                Log.d(TAG, "Adding clinical data for day number : " + dayNo);
                                sharedPrefs.saveBasicPatientDetails(getApplicationContext(), name, patientAge.getText().toString(), gender, dayNo);
                                // get clinical data
                                Intent intent = new Intent(RegisterPatientActivity.this, DiagnosisActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }


                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private boolean validatePatient() {

        String name = patientName.getText().toString();
        String ageStr = patientAge.getText().toString();
        String gender = radio_text;

        if (TextUtils.isEmpty(name)) {
            Toast.makeText(RegisterPatientActivity.this, "Enter patient's name !", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(ageStr)) {
            Toast.makeText(RegisterPatientActivity.this, "Enter patient's age !", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(gender)) {
            Toast.makeText(RegisterPatientActivity.this, "Enter patient's gender!", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }


    private static void saveToCloudDatabase(String patientName, int patientAge, String Gender)
    {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("users");
        String patientId = mDatabase.push().getKey();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        String uid = currentUser.getUid();

        // creating patient object
        Patient patient = new Patient(patientId, patientName, patientAge, Gender, "0.0", "0.0", "0.0", "0.0", "0.0", "0.0", "0.0");
        // pushing patient to 'patients' node using the patientId
        //int i = 0;
        mDatabase.child(uid).child("patients").child(patientId).setValue(patient).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "SaveToCloudDatabase:success");

                } else {

                    Log.w(TAG, "SaveToCloudDatabase:failure", task.getException());

                }
            }
        });

    }


}
