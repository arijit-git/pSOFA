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

import com.ray.apps.psofa.R;
import com.ray.apps.psofa.other.SharedPrefs;

import javax.annotation.Nullable;

public class DiagnosisStepTwoFragment extends Fragment implements View.OnClickListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private final String TAG = "DIAG/Step2";
    private String mParam1;
    private String mParam2;
    private SharedPrefs sharedPrefs;
    private OnStepTwoListener mListener;
    private EditText step2Input;
    private CardView step2CardView;

    public DiagnosisStepTwoFragment() {
        // Required empty public constructor
    }


    public static DiagnosisStepTwoFragment newInstance(String param1, String param2) {
        DiagnosisStepTwoFragment fragment = new DiagnosisStepTwoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getActivity().setTitle("Record Coagulation Data");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_diagnosis_step_two, container, false);
        step2Input = v.findViewById(R.id.step2_input);
        step2CardView = v.findViewById(R.id.step2CardView);

        // restore prefs
        sharedPrefs = new SharedPrefs(getContext());
        //step2Input.setText(sharedPrefs.getCoagulatoryInput());

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

    }

    @Override
    public void onResume() {
        super.onResume();
        backBT.setOnClickListener(this);
        nextBT.setOnClickListener(this);
        skipBT.setOnClickListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        backBT.setOnClickListener(null);
        nextBT.setOnClickListener(null);
        skipBT.setOnClickListener(null);
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.backBT:
                if (mListener != null)
                    mListener.onBackPressed(this);
                break;

            case R.id.nextBT:
                String plateletInput = "";
                if(!step2Input.getText().toString().isEmpty()) {
                    plateletInput = step2Input.getText().toString();
                    Log.d(TAG, "onClick: Platelet Count :" + plateletInput);
                    //Toast.makeText(getActivity(), "Platelet Count : " + plateletInput, Toast.LENGTH_SHORT);
                }
                sharedPrefs.saveCoagulatoryInput(getContext(), plateletInput);
                if (mListener != null)
                    mListener.onNextPressed(this);
                break;
            case R.id.skipBT:
                if (mListener != null)
                    mListener.onNextPressed(this);
                break;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnStepTwoListener) {
            mListener = (OnStepTwoListener) context;
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
        skipBT=null;
        sharedPrefs.clear();
    }

    public interface OnStepTwoListener {
        void onBackPressed(Fragment fragment);

        void onNextPressed(Fragment fragment);

    }
}