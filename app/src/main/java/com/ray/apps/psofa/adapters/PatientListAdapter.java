package com.ray.apps.psofa.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ray.apps.psofa.R;
import com.ray.apps.psofa.activities.PatientActivity;
import com.ray.apps.psofa.model.Patient;
import com.ray.apps.psofa.other.SharedPrefs;

import java.util.Arrays;
import java.util.List;

public class PatientListAdapter extends RecyclerView.Adapter<PatientListAdapter.MyViewHolder>  {
    private Context context;
    private List<Patient> patientList;
    private SharedPrefs sharedPrefs;
    String patientID;
    //public String day1Score, day2Score,day3Score,day4Score,day5Score,day6Score,day7Score;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView id, name, age, gender, scoreMax, scoreMin, final_result;
        public String day1Score, day2Score,day3Score,day4Score,day5Score,day6Score,day7Score;

        public ImageView thumbnail;
        public RelativeLayout viewBackground, viewForeground;

        public MyViewHolder(View view) {
            super(view);
            context = view.getContext();
            id = view.findViewById(R.id.id);
            name = view.findViewById(R.id.name);
            gender = view.findViewById(R.id.gender);
            age = view.findViewById(R.id.age);
            thumbnail = view.findViewById(R.id.thumbnail);
            scoreMax = view.findViewById(R.id.scoreValMax);
            scoreMin = view.findViewById(R.id.scoreValMin);
            viewBackground = view.findViewById(R.id.view_background);
            viewForeground = view.findViewById(R.id.view_foreground);
            final_result = view.findViewById(R.id.life_res_val);

            sharedPrefs = new SharedPrefs(context);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            //Toast.makeText(view.getContext(), "position = " +  getLayoutPosition(), Toast.LENGTH_SHORT).show();
            //Toast.makeText(view.getContext(), "Name = " + name.getText() + " Age = " + age.getText(), Toast.LENGTH_SHORT).show();

            sharedPrefs.savePatientId(context, id.getText().toString());
            sharedPrefs.saveBasicPatientDetails(context, name.getText().toString(), age.getText().toString(), gender.getText().toString(), "");
            sharedPrefs.setScores(context,
                    day1Score, day2Score,day3Score,day4Score,day5Score,day6Score,day7Score);
            sharedPrefs.setFinalOutcome(context, final_result.getText().toString());
            //Log.d("D", "Score day1 : " + day1Score + "Score day 1: " + sharedPrefs.getScore1());
            Intent intent =  new Intent(context, PatientActivity.class);
            context.startActivity(intent);
        }

    }

    public PatientListAdapter(Context context, List<Patient> patientList) {
        this.context = context;
        this.patientList = patientList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.patients_view, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Patient itemPatient = patientList.get(position);
        holder.id.setText(itemPatient.getId());
        holder.name.setText(itemPatient.getName());
        holder.gender.setText(itemPatient.getGender());
        holder.age.setText(itemPatient.getAge() + "");


        if(itemPatient.getFinal_result() != null) {
            holder.final_result.setText(itemPatient.getFinal_result());
            if(!itemPatient.getFinal_result().equalsIgnoreCase("survived"))
            {
                holder.final_result.setTextColor(Color.RED);
            }
            else
            {
                holder.final_result.setTextColor(Color.GREEN);
            }
        }

        holder.day1Score = itemPatient.getScore_day1();
        holder.day2Score = itemPatient.getScore_day2();
        holder.day3Score = itemPatient.getScore_day3();
        holder.day4Score = itemPatient.getScore_day4();
        holder.day5Score = itemPatient.getScore_day5();
        holder.day6Score = itemPatient.getScore_day6();
        holder.day7Score = itemPatient.getScore_day7();

        //Log.d("Adapter", "day1 : " + itemPatient.getScore_day1() + " ");
        // calculate max score
       mapMaxMinScore(holder,
                itemPatient.getScore_day1(),
                itemPatient.getScore_day2(),
                itemPatient.getScore_day3(),
                itemPatient.getScore_day4(),
                itemPatient.getScore_day5(),
                itemPatient.getScore_day6(),
                itemPatient.getScore_day7()
        );


    }

    @Override
    public int getItemCount() {
        return patientList.size();
    }

    public void removeItem(int position) {
        patientList.remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);
    }

    public void restoreItem(Patient item, int position) {
        patientList.add(position, item);
        // notify item added by position
        notifyItemInserted(position);
    }

    private void mapMaxMinScore(MyViewHolder holder, String score_day1,String score_day2,String score_day3,String score_day4,
                                     String score_day5,String score_day6,String score_day7)
    {
        double maxScore = 0.0;
        double minScore = 0.0;
        double[] scores = {Double.parseDouble(score_day1),
                Double.parseDouble(score_day2),
                Double.parseDouble(score_day3),
                Double.parseDouble(score_day4),
                Double.parseDouble(score_day5),
                Double.parseDouble(score_day6),
                Double.parseDouble(score_day7)};

        Arrays.sort(scores);

        maxScore = scores[scores.length - 1];
        holder.scoreMax.setText(maxScore + "");
        minScore = scores[0];
        holder.scoreMin.setText(minScore + "");
       // Log.d("Adapter", "ScoreMax :" + maxScore + "ScoreMin :" + minScore);
    }

}