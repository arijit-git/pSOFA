package com.ray.apps.psofa.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.ray.apps.psofa.R;
import com.ray.apps.psofa.other.SharedPrefs;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

public class DiagnosisStepOneFragment extends Fragment implements View.OnClickListener {

    private final String TAG = "DIAG/Step1";
    private OnStepOneListener mListener;
    private EditText step1Input;
    private RadioGroup radioGroup;
    private CardView cardView;
    private SharedPrefs sharedPrefs;
    private String type;

    public DiagnosisStepOneFragment() {
        // Required empty public constructor
    }


    public static DiagnosisStepOneFragment newInstance(String param1, String param2) {
        DiagnosisStepOneFragment fragment = new DiagnosisStepOneFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_diagnosis_step_one, container, false);
        //spinner = v.findViewById(R.id.step1_spinner);
        //spinner.setOnItemSelectedListener(this);

        radioGroup = v.findViewById(R.id.radioGroupStep1);
        step1Input = v.findViewById(R.id.step1_input);
        cardView = v.findViewById(R.id.step1CardView);
        cardView.setVisibility(View.GONE);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                switch(checkedId)
                {
                    case R.id.respRadioBtn1:
                        step1Input.setText("");
                        cardView.setVisibility(View.VISIBLE);
                        type = "pao2";
                        break;
                    case R.id.respRadioBtn2:
                        step1Input.setText("");
                        cardView.setVisibility(View.VISIBLE);
                        type = "spo2";
                        break;
                }
            }
        });

        // restore prefs
        sharedPrefs = new SharedPrefs(getContext());
       // step1Input.setText(sharedPrefs.getRespiratoryInput());
        return v;
    }


    private Button nextBT;
    private Button backBT;
    private Button skipBT;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        nextBT = view.findViewById(R.id.nextBT);
        backBT = view.findViewById(R.id.backBT);
        skipBT = view.findViewById(R.id.skipBT);

        backBT.setVisibility(View.GONE);
        skipBT.setVisibility(View.GONE);

    }


    @Override
    public void onResume() {
        super.onResume();
        nextBT.setOnClickListener(this);
        backBT.setOnClickListener(this);
        skipBT.setOnClickListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        nextBT.setOnClickListener(null);
        backBT.setOnClickListener(null);
        skipBT.setOnClickListener(null);
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.nextBT:
                if(!step1Input.getText().toString().isEmpty()) {
                    String respInput = step1Input.getText().toString();
                    Log.d(TAG, "onClick: Respiratory reading :" + respInput + " Type :" + type);
                    sharedPrefs.saveRespiratoryInput(getContext(), respInput, type);

                }
                if (mListener != null) {
                    mListener.onNextPressed(this);
                }
                break;
        }

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnStepOneListener) {
            mListener = (OnStepOneListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        nextBT = null;
        sharedPrefs.clear();
    }

    public interface OnStepOneListener {
        //void onFragmentInteraction(Uri uri);
        void onNextPressed(Fragment fragment);
    }
}