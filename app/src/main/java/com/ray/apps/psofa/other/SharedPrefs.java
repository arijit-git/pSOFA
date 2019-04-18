package com.ray.apps.psofa.other;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefs {

    SharedPreferences sharedPreferences;
    Context mContext;
    // shared pref mode
    int PRIVATE_MODE = 0;
    // Shared preferences file name
    private static final String PREF_NAME = "sessionPref";
    SharedPreferences.Editor editor;

    public static final String resp_type = "respTypeKey";
    public static final String resp_pao2 = "pao2RespKey";
    public static final String resp_spo2 = "spo2RespKey";
    public static final String platleteCount = "platleteCountKey";
    public static final String bilirubinCount = "bilirubinCountKey";
    public static final String gcs = "gcsKey";
    public static final String renalAge = "renalAgeKey";
    public static final String renalCreatinine = "renalCreatinineKey";

    public static final String cardioAge = "cardioAgeKey";
    public static final String cardioBP = "cardioBPKey";
    public static final String cardioDop = "cardioDopKey";
    public static final String cardioDobutamine = "cardioDobutamineKey";
    public static final String cardioEpinephrine = "cardioEpinephrineKey";
    public static final String cardioNorEpinephrine = "cardioNorEpinephrineKey";

    public static final String cardioCalcKey = "cardioFinalKey";
    public static final String cardioCalcKeyVal = "cardioFinalKeyVal";

    public static final String pID = "pID";
    public static final String pName = "pName";
    public static final String pAge = "pAge";
    public static final String pGender = "pGender";
    public static final String dayNum = "dayNum";
    public static final String patientModStatus = "pModStatus";
    public static final String finalOutcome = "pFinalOutcome";

    public static final String score_day1 = "pScore-day1";
    public static final String score_day2 = "pScore-day2";
    public static final String score_day3 = "pScore-day3";
    public static final String score_day4 = "pScore-day4";
    public static final String score_day5 = "pScore-day5";
    public static final String score_day6 = "pScore-day6";
    public static final String score_day7 = "pScore-day7";


    public SharedPrefs (Context context) {
        mContext = context;
        sharedPreferences = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = sharedPreferences.edit();
    }


    public void setCardioFinalKey(Context context, String cardioKey){
        mContext = context;
        sharedPreferences = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(cardioCalcKey, cardioKey);
        editor.apply();
    }
    public String getCardioFinalKey(){
        sharedPreferences = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        String res = sharedPreferences.getString(cardioCalcKey, "NA");
        return res;
    }
    public String getCardioFinalKeyValue(){
        sharedPreferences = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        String res = sharedPreferences.getString(cardioCalcKeyVal, "NA");
        return res;
    }
    public void setCardioFinalKeyValue(Context context, String cardioKeyVal){
        mContext = context;
        sharedPreferences = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(cardioCalcKeyVal, cardioKeyVal);
        editor.apply();
    }

    public void setPatientModification(Context context, String status){
        mContext = context;
        sharedPreferences = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(patientModStatus, status);
        editor.apply();
    }
    public String getPatientModificationStatus(){
        sharedPreferences = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        String res = sharedPreferences.getString(patientModStatus, "OFF");
        return res;
    }

    public void setPatientDisplayName(Context context, String name){
        mContext = context;
        sharedPreferences = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(pName, name);
        editor.apply();
    }
    public String getPatientDisplayName(){
        sharedPreferences = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        String res = sharedPreferences.getString(pName, "N/A");
        return res;
    }

    public void setPatientAge(Context context, String age){
        mContext = context;
        sharedPreferences = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(pAge, age);
        editor.apply();
    }

    public void saveBasicPatientDetails(Context context, String name, String age, String gender, String dayNo){
        mContext = context;
        sharedPreferences = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(pName, name);
        editor.putString(pAge, age);
        editor.putString(dayNum, dayNo);
        editor.putString(pGender, gender);
        editor.apply();
    }
    public void saveBasicPatientDayNum(Context context, String dayNo){
        mContext = context;
        sharedPreferences = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(dayNum, dayNo);
        editor.apply();
    }
    public void savePatientId(Context context, String id){
        mContext = context;
        sharedPreferences = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(pID, id);
        editor.apply();
    }

    public void setFinalOutcome(Context context, String res){
        mContext = context;
        sharedPreferences = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(finalOutcome, res);
        editor.apply();
    }

    public String getFinalOutcome(){
        sharedPreferences = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        String res = sharedPreferences.getString(finalOutcome, "N/A");
        return res;
    }

    public String getPatientId(){
        sharedPreferences = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        String res = sharedPreferences.getString(pID, "N/A");
        return res;
    }

    public String getBasicPatientName(){
        sharedPreferences = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        String res = sharedPreferences.getString(pName, "N/A");
        return res;
    }
    public String getBasicPatientAge(){
        sharedPreferences = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        String res = sharedPreferences.getString(pAge, "N/A");
        return res;
    }
    public String getBasicPatientGender(){
        sharedPreferences = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        String res = sharedPreferences.getString(pGender, "N/A");
        return res;
    }
    public String getBasicPatientDayNum(){
        sharedPreferences = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        String res = sharedPreferences.getString(dayNum, "N/A");
        return res;
    }

    // save the input data

    public void saveRespiratoryInput(Context context, String respCount, String type){
        mContext = context;
        sharedPreferences = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if(!type.isEmpty() && type.equalsIgnoreCase("pao2"))
        {
            editor.putString(resp_type, "pao2");
            editor.putString(resp_pao2, respCount);

        }
        if(!type.isEmpty() && type.equalsIgnoreCase("spo2"))
        {
            editor.putString(resp_type, "spo2");
            editor.putString(resp_spo2, respCount);
        }
        editor.apply();
    }
    public String getRespiratoryInput(){
        sharedPreferences = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        String s1 = sharedPreferences.getString(resp_pao2, "0");
        String s2 = sharedPreferences.getString(resp_spo2, "0");

        return s1.equalsIgnoreCase("0")? s2 : s1;
    }
    public String getRespiratoryInputType(){
        sharedPreferences = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        return sharedPreferences.getString(resp_type, "0");
    }

    public void saveCoagulatoryInput(Context context, String pCount){
        mContext = context;
        sharedPreferences = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(platleteCount, pCount);
        editor.apply();
    }
    public String getCoagulatoryInput(){
        sharedPreferences = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        String res = sharedPreferences.getString(platleteCount, "0");
        if(res.isEmpty())
        {
            res = "0";
        }
        return res;
    }
    public void saveHepaticInput(Context context, String bilirubin){
        mContext = context;
        sharedPreferences = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(bilirubinCount, bilirubin);
        editor.apply();
    }
    public String getHepaticInput(){
        sharedPreferences = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        String res = sharedPreferences.getString(bilirubinCount, "0.0");
        if(res.isEmpty())
        {
            res = "0.0";
        }
        return res;
    }

    public void setCardioBP(Context context, String bp)
    {
        mContext = context;
        sharedPreferences = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if(!bp.isEmpty()) {
            editor.putString(cardioBP, bp);
        }
        editor.apply();
    }

    public void setCardioDopamine(Context context, String dopamine)
    {
        mContext = context;
        sharedPreferences = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if(!dopamine.isEmpty()) {
            editor.putString(cardioDop, dopamine);
        }
        editor.apply();
    }
    public void setCardioDobutamine(Context context, String dobutamine)
    {
        mContext = context;
        sharedPreferences = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if(!dobutamine.isEmpty()) {
            editor.putString(cardioDobutamine, dobutamine);
        }
        editor.apply();
    }
    public void setCardioEpinephrine(Context context, String epinephrine)
    {
        mContext = context;
        sharedPreferences = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if(!epinephrine.isEmpty()) {
            editor.putString(cardioEpinephrine, epinephrine);
        }
        editor.apply();
    }
    public void setCardioNorEpinephrine(Context context, String nor_epinephrine)
    {
        mContext = context;
        sharedPreferences = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if(!nor_epinephrine.isEmpty()) {
            editor.putString(cardioNorEpinephrine, nor_epinephrine);
        }
        editor.apply();
    }

    public void saveCardioInput(Context context, String bp, String dopamine, String epinephrine, String nor_epinephrine, String dobutamine_any){
        mContext = context;
        sharedPreferences = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if(!bp.isEmpty()) {
            editor.putString(cardioBP, bp);
        }
        if(!dopamine.isEmpty()) {
            editor.putString(cardioDop, dopamine);
        }
        if(!dobutamine_any.equalsIgnoreCase("ON")) {
            editor.putString(cardioDobutamine, dobutamine_any);
        }
        if(!epinephrine.isEmpty()) {
            editor.putString(cardioEpinephrine, epinephrine);
        }
        if(!nor_epinephrine.isEmpty()) {
            editor.putString(cardioNorEpinephrine, nor_epinephrine);
        }
        editor.apply();
    }

    public  void setCardioCalculationParamFlag(Context context, String carioParam)
    {
        mContext = context;
        sharedPreferences = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(cardioEpinephrine, carioParam);

        editor.apply();
    }

    public String getCardioBPInput(){
        sharedPreferences = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        return sharedPreferences.getString(cardioBP, "NA");
    }
    public String getCardioDopamineInput(){
        sharedPreferences = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        return sharedPreferences.getString(cardioDop, "NA");
    }

    public String getCardioEpinephrineInput(){
        sharedPreferences = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        return sharedPreferences.getString(cardioEpinephrine, "NA");
    }
    public String getCardioNorEpinephrineInput(){
        sharedPreferences = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        return sharedPreferences.getString(cardioNorEpinephrine, "NA");
    }
    public String getCardioDobutamineInput(){
        sharedPreferences = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        return sharedPreferences.getString(cardioDobutamine, "OFF");
    }

    public void saveNeurologicInput(Context context, String glasgoScore){
        mContext = context;
        sharedPreferences = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(gcs, glasgoScore);
        editor.apply();
    }
    public String getNeurologicInput(){
        sharedPreferences = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        String res = sharedPreferences.getString(gcs, "0");
        if(res.isEmpty())
        {
            res = "0";
        }
        return res;
    }
    public void saveRenalInput(Context context, String ageInMonths, String renalCreatinineCount){
        mContext = context;
        sharedPreferences = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(renalAge, ageInMonths);
        editor.putString(renalCreatinine, renalCreatinineCount);
        editor.apply();
    }
    public String getRenalAgeInput(){
        sharedPreferences = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        String res = sharedPreferences.getString(renalAge, "0");
        if(res.isEmpty())
        {
            res = "0";
        }
        return res;
    }
    public String getRenalCreatinineInput(){
        sharedPreferences = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        String res = sharedPreferences.getString(renalCreatinine, "0.0");
        if(res.isEmpty())
        {
            res = "0.0";
        }
        return res;
    }



    public void saveRespiratoryScore(Context context, double score)
    {
        mContext = context;
        sharedPreferences = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat("RESP_SCORE", (float)score);
        editor.apply();
    }

    public void setScores(Context context, String day1_score, String day2_score,
                          String day3_score, String day4_score, String day5_score, String day6_score, String day7_score){
        mContext = context;
        sharedPreferences = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(score_day1, day1_score);
        editor.putString(score_day2, day2_score);
        editor.putString(score_day3, day3_score);
        editor.putString(score_day4, day4_score);
        editor.putString(score_day5, day5_score);
        editor.putString(score_day6, day6_score);
        editor.putString(score_day7, day7_score);
        editor.apply();
    }

    public String getScore1(){
        sharedPreferences = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        String res = sharedPreferences.getString(score_day1, "N/A");
        return res;
    }
    public String getScore2(){
        sharedPreferences = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        String res = sharedPreferences.getString(score_day2, "N/A");
        return res;
    }
    public String getScore3(){
        sharedPreferences = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        String res = sharedPreferences.getString(score_day3, "N/A");
        return res;
    }
    public String getScore4(){
        sharedPreferences = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        String res = sharedPreferences.getString(score_day4, "N/A");
        return res;
    }
    public String getScore5(){
        sharedPreferences = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        String res = sharedPreferences.getString(score_day5, "N/A");
        return res;
    }
    public String getScore6(){
        sharedPreferences = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        String res = sharedPreferences.getString(score_day6, "N/A");
        return res;
    }
    public String getScore7(){
        sharedPreferences = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        String res = sharedPreferences.getString(score_day7, "N/A");
        return res;
    }

    public void clear(){
        editor.clear();
        editor.apply();
    }


    public boolean isEmptyInput(String input)
    {
        String validInput = "";
        if(input.isEmpty()){
            validInput = "0";
        }

        return false;
    }
}
