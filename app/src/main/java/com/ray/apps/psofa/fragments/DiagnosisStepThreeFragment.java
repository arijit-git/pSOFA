package com.ray.apps.psofa.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.ray.apps.psofa.R;
import com.ray.apps.psofa.other.SharedPrefs;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

public class DiagnosisStepThreeFragment extends Fragment implements View.OnClickListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private final String TAG = "DIAG/Step3";
    private SharedPrefs sharedPrefs;
    private EditText step3Input;
    private CardView step3CardView;
    private String mParam1;
    private String mParam2;

    private OnStepThreeListener mListener;

    public DiagnosisStepThreeFragment() {
        // Required empty public constructor
    }

    public static DiagnosisStepThreeFragment newInstance(String param1, String param2) {
        DiagnosisStepThreeFragment fragment = new DiagnosisStepThreeFragment();
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
        View v =  inflater.inflate(R.layout.fragment_diagnosis_step_three, container, false);
        step3Input = v.findViewById(R.id.step3_input);
        step3CardView = v.findViewById(R.id.step3CardView);

        // restore prefs
        sharedPrefs = new SharedPrefs(getContext());
        //step3Input.setText(sharedPrefs.getHepaticInput());
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

                String bilirubinInput = "";
                if(!step3Input.getText().toString().isEmpty()) {
                    bilirubinInput = step3Input.getText().toString();
                    Log.d(TAG, "onClick: bilirubinInput Count :" + bilirubinInput);
                    //Toast.makeText(getActivity(), "Platelet Count : " + plateletInput, Toast.LENGTH_SHORT);
                }
                sharedPrefs.saveHepaticInput(getContext(), bilirubinInput);

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
        if (context instanceof OnStepThreeListener) {
            mListener = (OnStepThreeListener) context;
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
        skipBT = null;
        sharedPrefs.clear();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnStepThreeListener {
        void onBackPressed(Fragment fragment);

        void onNextPressed(Fragment fragment);

    }
}