package com.ray.apps.psofa.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.ray.apps.psofa.R;
import com.ray.apps.psofa.other.SharedPrefs;
import com.ray.apps.psofa.other.Utils;

import javax.annotation.Nullable;

public class DiagnosisStepFourFragment extends Fragment implements View.OnClickListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private final String TAG = "DIAG/Step4";
    private SharedPrefs sharedPrefs;
    private TextView text_step4_age_val, text_step40, text_step41, text_step42, text_step43;
    private EditText step40_bp_input, step41_dopamine_input, step42_epinephrine_input, step43_nor_epinephrine_input;
    private CardView step40CardView, step41CardView, step42CardView, step43CardView;
    private Switch pSwitch, p2Switch, p3Switch, p4Switch, switchChoose;
    private ConstraintLayout cardioContent;
    private String dobutamine_any = "OFF", BP = "", dopamine = "", epinephrine = "", nor_epinephrine = "";
    private OnStepFourListener mListener;

    private boolean cardioFlag = false;

    public DiagnosisStepFourFragment() {
        // Required empty public constructor
    }

    public static DiagnosisStepFourFragment newInstance(String param1, String param2) {
        DiagnosisStepFourFragment fragment = new DiagnosisStepFourFragment();
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
        View v =  inflater.inflate(R.layout.fragment_diagnosis_step_four, container, false);

        cardioContent = v.findViewById(R.id.cardio_content);

        text_step4_age_val = v.findViewById(R.id.text_step4_age_val);

        text_step40 = v.findViewById(R.id.text_step40);
        step40_bp_input = v.findViewById(R.id.step40_input);
        step40CardView = v.findViewById(R.id.step40CardView);

        text_step41 = v.findViewById(R.id.text_step41);
        step41_dopamine_input = v.findViewById(R.id.step41_input);
        step41CardView = v.findViewById(R.id.step41CardView);

        text_step42 = v.findViewById(R.id.text_step42);
        step42_epinephrine_input = v.findViewById(R.id.step42_input);
        step42CardView = v.findViewById(R.id.step42CardView);

        text_step43 = v.findViewById(R.id.text_step43);
        step43_nor_epinephrine_input = v.findViewById(R.id.step43_input);
        step43CardView = v.findViewById(R.id.step43CardView);

        // restore prefs
        sharedPrefs = new SharedPrefs(getContext());
        text_step4_age_val.setText(sharedPrefs.getBasicPatientAge());

        switchChoose = v.findViewById(R.id.switchChoose);
        switchChoose.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    cardioContent.setVisibility(View.VISIBLE);
                    pSwitch.setVisibility(View.VISIBLE);
                    p2Switch.setVisibility(View.VISIBLE);
                    p3Switch.setVisibility(View.VISIBLE);
                    p4Switch.setVisibility(View.VISIBLE);

                    text_step40.setVisibility(View.GONE);
                    step40CardView.setVisibility(View.GONE);

                    text_step41.setVisibility(View.GONE);
                    step41CardView.setVisibility(View.GONE);

                    text_step42.setVisibility(View.GONE);
                    step42CardView.setVisibility(View.GONE);

                    text_step43.setVisibility(View.GONE);
                    step43CardView.setVisibility(View.GONE);
                }
                else
                {
                    cardioContent.setVisibility(View.GONE);
                    pSwitch.setVisibility(View.GONE);
                    p2Switch.setVisibility(View.GONE);
                    p3Switch.setVisibility(View.GONE);
                    p4Switch.setVisibility(View.GONE);

                    pSwitch.setChecked(false);
                    p2Switch.setChecked(false);
                    p3Switch.setChecked(false);
                    p4Switch.setChecked(false);


                    text_step41.setVisibility(View.GONE);
                    step41CardView.setVisibility(View.GONE);

                    text_step42.setVisibility(View.GONE);
                    step42CardView.setVisibility(View.GONE);

                    text_step43.setVisibility(View.GONE);
                    step43CardView.setVisibility(View.GONE);

                    text_step40.setVisibility(View.VISIBLE);
                    step40CardView.setVisibility(View.VISIBLE);


                }
            }
        });

        pSwitch = v.findViewById(R.id.p_switch);
        pSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    text_step41.setVisibility(View.VISIBLE);
                    step41CardView.setVisibility(View.VISIBLE);

                    p2Switch.setVisibility(View.GONE);
                    p3Switch.setVisibility(View.GONE);
                    p4Switch.setVisibility(View.GONE);



                }
                else
                {
                    text_step41.setVisibility(View.GONE);
                    step41CardView.setVisibility(View.GONE);

                    p2Switch.setVisibility(View.VISIBLE);
                    p3Switch.setVisibility(View.VISIBLE);
                    p4Switch.setVisibility(View.VISIBLE);
                }
            }
        });

        p2Switch = v.findViewById(R.id.p2_switch);
        p2Switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    // set a flag for dobutamine 'ON'
                    dobutamine_any = "ON";
                    pSwitch.setVisibility(View.GONE);
                    p3Switch.setVisibility(View.GONE);
                    p4Switch.setVisibility(View.GONE);

                    if(!dobutamine_any.isEmpty())
                    {
                        //save the input data
                        sharedPrefs.setCardioFinalKey(getContext(), "DOBU");
                        sharedPrefs.setCardioFinalKeyValue(getContext(), dobutamine_any);
                        //sharedPrefs.setCardioDobutamine(getContext(), dobutamine_any);
                    }
                }
                else
                {
                    dobutamine_any = "OFF";
                    pSwitch.setVisibility(View.VISIBLE);
                    p3Switch.setVisibility(View.VISIBLE);
                    p4Switch.setVisibility(View.VISIBLE);
                }
            }
        });

        p3Switch = v.findViewById(R.id.p3_switch);
        p3Switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    text_step42.setVisibility(View.VISIBLE);
                    step42CardView.setVisibility(View.VISIBLE);

                    pSwitch.setVisibility(View.GONE);
                    p2Switch.setVisibility(View.GONE);
                    p4Switch.setVisibility(View.GONE);





                }
                else
                {
                    text_step42.setVisibility(View.GONE);
                    step42CardView.setVisibility(View.GONE);

                    pSwitch.setVisibility(View.VISIBLE);
                    p2Switch.setVisibility(View.VISIBLE);
                    p4Switch.setVisibility(View.VISIBLE);

                }
            }
        });

        p4Switch = v.findViewById(R.id.p4_switch);
        p4Switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    text_step43.setVisibility(View.VISIBLE);
                    step43CardView.setVisibility(View.VISIBLE);

                    pSwitch.setVisibility(View.GONE);
                    p2Switch.setVisibility(View.GONE);
                    p3Switch.setVisibility(View.GONE);


                }
                else
                {
                    text_step43.setVisibility(View.GONE);
                    step43CardView.setVisibility(View.GONE);

                    pSwitch.setVisibility(View.VISIBLE);
                    p2Switch.setVisibility(View.VISIBLE);
                    p3Switch.setVisibility(View.VISIBLE);

                }
            }
        });





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

                if(isValidatedCardio())
                {
                    saveCardioFinalInput();
                    if (mListener != null)
                        mListener.onNextPressed(this);
                }

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
        if (context instanceof OnStepFourListener) {
            mListener = (OnStepFourListener) context;
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

    public interface OnStepFourListener {
        void onBackPressed(Fragment fragment);

        void onNextPressed(Fragment fragment);

    }

    public boolean isValidatedCardio() {



        if (switchChoose.isChecked()) {
            if (step41_dopamine_input.getText().toString().isEmpty() && pSwitch.isChecked()) {
                Utils.showToast(getContext(), "Please enter dopamine dosage.");
                return false;
            }
            if (step42_epinephrine_input.getText().toString().isEmpty() && p3Switch.isChecked()) {
                Utils.showToast(getContext(), "Please enter Epinephrine dosage.");
                return false;
            }
            if (step43_nor_epinephrine_input.getText().toString().isEmpty() && p4Switch.isChecked()) {
                Utils.showToast(getContext(), "Please enter nor-epinephrine dosage.");
                return false;

            }
            if (!pSwitch.isChecked() && !p2Switch.isChecked() && !p3Switch.isChecked() && !p4Switch.isChecked()) {
                Utils.showToast(getContext(), "You must choose any dosage or dobutamine.");
                return false;
            }
        }
        else
        if (step40_bp_input.getText().toString().isEmpty()) {
            Utils.showToast(getContext(), "Please enter BP reading.");
            return false;
        }



        return true;
    }

    public void saveCardioFinalInput()
    {
        Log.d(TAG, "Inital Key : " + sharedPrefs.getCardioFinalKey() );
        if(!step40_bp_input.getText().toString().isEmpty())
        {
            BP = "ON";
            //save the input data
            Log.d(TAG, "BP : " + step40_bp_input.getText().toString() + " Switch Checked: " + switchChoose.isChecked());
            sharedPrefs.setCardioFinalKey(getContext(), "BP");
            sharedPrefs.setCardioFinalKeyValue(getContext(), step40_bp_input.getText().toString());
            Log.d(TAG, "Key : " + sharedPrefs.getCardioFinalKey() );
            //sharedPrefs.setCardioBP(getContext(), step40_bp_input.getText().toString());
        }

        if(!step41_dopamine_input.getText().toString().isEmpty())
        {
            dopamine = "ON";
            //save the input data
            Log.d(TAG, "DOPA : " + step41_dopamine_input.getText().toString() + " Switch Checked: " + switchChoose.isChecked());
            sharedPrefs.setCardioFinalKey(getContext(), "DOPA");
            sharedPrefs.setCardioFinalKeyValue(getContext(), step41_dopamine_input.getText().toString());
            //sharedPrefs.setCardioDopamine(getContext(), step41_dopamine_input.getText().toString());
        }

        if(!step42_epinephrine_input.getText().toString().isEmpty())
        {
            epinephrine = "ON";
            //save the input data
            Log.d(TAG, "EPI : " + step42_epinephrine_input.getText().toString() + " Switch Checked: " + switchChoose.isChecked());
            sharedPrefs.setCardioFinalKey(getContext(), "EPI");
            sharedPrefs.setCardioFinalKeyValue(getContext(), step42_epinephrine_input.getText().toString());
            //sharedPrefs.setCardioEpinephrine(getContext(), step42_epinephrine_input.getText().toString());
        }

        if(!step43_nor_epinephrine_input.getText().toString().isEmpty())
        {
            nor_epinephrine = "ON";
            //save the input data
            Log.d(TAG, "NOR-EPI : " + step43_nor_epinephrine_input.getText().toString() + " Switch Checked: " + switchChoose.isChecked());
            sharedPrefs.setCardioFinalKey(getContext(), "NOR-EPI");
            sharedPrefs.setCardioFinalKeyValue(getContext(), step43_nor_epinephrine_input.getText().toString());
            //sharedPrefs.setCardioNorEpinephrine(getContext(), step43_nor_epinephrine_input.getText().toString());
        }

    }

}