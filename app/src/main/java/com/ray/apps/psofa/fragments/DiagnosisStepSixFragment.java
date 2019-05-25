package com.ray.apps.psofa.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ray.apps.psofa.R;
import com.ray.apps.psofa.other.SharedPrefs;

import javax.annotation.Nullable;

public class DiagnosisStepSixFragment extends Fragment implements View.OnClickListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private final String TAG = "DIAG/Step6";
    private SharedPrefs sharedPrefs;
    private TextView text_step6_age_val;
    private EditText step61Input;
    private CardView step61CardView;
    private OnStepSixListener mListener;

    public DiagnosisStepSixFragment() {
        // Required empty public constructor
    }

    public static DiagnosisStepSixFragment newInstance(String param1, String param2) {
        DiagnosisStepSixFragment fragment = new DiagnosisStepSixFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // getActivity().setTitle("Record Hepatic Data");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_diagnosis_step_six, container, false);
        text_step6_age_val = v.findViewById(R.id.text_step6_age_val);
        step61Input = v.findViewById(R.id.step61_input);
        step61CardView = v.findViewById(R.id.step61CardView);
        // restore prefs
        sharedPrefs = new SharedPrefs(getContext());
        text_step6_age_val.setText(sharedPrefs.getBasicPatientAge());
        return v;
    }

    private Button backBT;
    private Button nextBT;
    private Button skipBT;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        backBT=view.findViewById(R.id.backBT);
        nextBT=view.findViewById(R.id.nextBT);
        skipBT=view.findViewById(R.id.skipBT);

        skipBT.setVisibility(View.GONE);
        nextBT.setText("Complete");
    }

    @Override
    public void onResume() {
        super.onResume();
        backBT.setOnClickListener(this);
        nextBT.setOnClickListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        backBT.setOnClickListener(null);
        nextBT.setOnClickListener(null);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.backBT:
                if (mListener != null)
                    mListener.onBackPressed(this);
                break;

            case R.id.nextBT:
                String ageInput = "", creatinine="";
                if(!step61Input.getText().toString().isEmpty()) {
                    ageInput = sharedPrefs.getBasicPatientAge();
                    creatinine = step61Input.getText().toString();
                    Log.d(TAG, "onClick: age :" + ageInput + " creatinine count: " + creatinine);

                }

                sharedPrefs.saveRenalInput(getContext(), ageInput, creatinine);

                if (mListener != null)
                    mListener.onNextPressed(this);
                break;

        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnStepSixListener) {
            mListener = (OnStepSixListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        backBT=null;
        nextBT=null;
    }

    public interface OnStepSixListener {
        void onBackPressed(Fragment fragment);

        void onNextPressed(Fragment fragment);

    }

}