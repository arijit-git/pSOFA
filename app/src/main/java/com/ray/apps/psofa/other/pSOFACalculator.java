package com.ray.apps.psofa.other;

import android.util.Log;

public class pSOFACalculator {

    private String TAG = "pSOFACalculator";
    private String coagulationScore, respiratoryScore, hepaticScore, cardioVascularScore, neurologicalScore, renalScore;

    private int pa02fio2b, spo2fio2c;
    private String plateletCount;
    private String bilirubin;
    private String glasgowComaScore;

    public pSOFACalculator()
    {

    }

    public pSOFACalculator(
            int pa02fio2b,
            int spo2fio2c,
            String plateletCount,
            String bilirubin,
            String glasgowComaScore)
    {
        this.pa02fio2b = pa02fio2b;
        this.spo2fio2c = spo2fio2c;
        this.plateletCount = plateletCount;
        this.bilirubin = bilirubin;
        this.glasgowComaScore = glasgowComaScore;

        Log.d(TAG, " Diagnosis Details : [pa02fio2b = " + pa02fio2b + " or spo2fio2c = " + spo2fio2c
                         + " plateletCount = " + plateletCount + " bilirubin = "+ bilirubin + " glasgowComaScore = " + glasgowComaScore + "]");
    }


    public double getRespiratoryScore(int count, String type)
    {
        double score = 0.0;
        int pa02fio2b = 0, spo2fio2c = 0;
        if(type.isEmpty() && type.equalsIgnoreCase("pao2"))
        {
            // scores for pa02:fio2b readings
            if(pa02fio2b >= 400) score = 0.0;
            if(pa02fio2b > 300 && pa02fio2b <= 399) score = 1.0;
            if(pa02fio2b > 200 && pa02fio2b <= 299) score = 2.0;
            if(pa02fio2b > 100 && pa02fio2b <= 199) score = 3.0;
            if(pa02fio2b < 100) score = 4.0;
        }
        if(type.isEmpty() && type.equalsIgnoreCase("spo2"))
        {
            // scores for spo2:fio2c readings
            if(spo2fio2c >= 292) score = 0.0;
            if(pa02fio2b > 264 && pa02fio2b <= 291) score = 1.0;
            if(pa02fio2b > 221 && pa02fio2b <= 264) score = 2.0;
            if(pa02fio2b > 148 && pa02fio2b <= 220) score = 3.0;
            if(pa02fio2b < 148) score = 4.0;
        }

        return score;
    }

    /**
     *
     * @param plateletCount
     * @return score
     */
    public double getCoagulationScore(int plateletCount)
    {
        double score = 0.0;

        if(plateletCount != 0)
        {
            // scores for platelet count x103 uL
            if(plateletCount >= 150) score = 0.0;
            if(plateletCount >= 100 && plateletCount <= 149) score = 1.0;
            if(plateletCount >= 50 && plateletCount <= 99) score = 2.0;
            if(plateletCount >= 20 && plateletCount <= 49) score = 3.0;
            if(plateletCount < 20) score = 4.0;
        }

        return score;
    }

    /**
     *
     * @param bilirubin
     * @return score
     */
    public double getHepaticScore(double bilirubin)
    {
        double score = 0.0;

        if(bilirubin != 0)
        {
            // scores for bilirubin count mg/dL
            if(bilirubin < 1.2) score = 0.0;
            if(bilirubin >= 1.2 && bilirubin <= 1.9) score = 1.0;
            if(bilirubin >= 2.0 && bilirubin <= 5.9) score = 2.0;
            if(bilirubin >= 6.0 && bilirubin <= 11.9) score = 3.0;
            if(bilirubin > 12.0) score = 4.0;
        }
        return score;
    }

    public double getFinalScoreForBP(int ageInMonths, String BP)
    {
        double score = 0.0;
        if(!BP.isEmpty()) {
            double bp = Double.parseDouble(BP);

            if (ageInMonths < 1) {
                if (bp >= 46) score = 0.0;
                if (bp < 46) score = 1.0;
            }
            if (ageInMonths >= 1 && ageInMonths <= 11) {
                if (bp >= 55) score = 0.0;
                if (bp < 55) score = 1.0;
            }
            if (ageInMonths >= 12 && ageInMonths <= 23) {
                if (bp >= 60) score = 0.0;
                if (bp < 60) score = 1.0;
            }
            if (ageInMonths >= 24 && ageInMonths <= 59) {
                if (bp >= 62) score = 0.0;
                if (bp < 62) score = 1.0;
            }
            if (ageInMonths >= 60 && ageInMonths <= 143) {
                if (bp >= 65) score = 0.0;
                if (bp < 65) score = 1.0;
            }
            if (ageInMonths >= 144 && ageInMonths <= 216) {
                if (bp >= 67) score = 0.0;
                if (bp < 67) score = 1.0;
            }
            if (ageInMonths > 216) {
                if (bp >= 70) score = 0.0;
                if (bp < 70) score = 1.0;
            }
        }

        return score;
    }

    public double getFinalScoreForDopamine(String DOPA)
    {
        double score = 0.0;

        if(!DOPA.isEmpty())
        {
            double dopamine = Double.parseDouble(DOPA);

            if(dopamine <= 5 ) score = 2.0;
            if(dopamine > 5 ) score = 3.0;
            if(dopamine > 15 ) score = 4.0;

        }

        return score;
    }

    public double getFinalScoreForEpinephrine(String EPI)
    {
        double score = 0.0;

        if(!EPI.isEmpty())
        {
            double epinephrine = Double.parseDouble(EPI);

            if(epinephrine <= 0.1 ) score = 3.0;
            if(epinephrine >= 0.1 ) score = 4.0;

        }

        return score;
    }

    public double getFinalScoreForNorEpimephrine(String NOREPI)
    {
        double score = 0.0;

        if(!NOREPI.isEmpty())
        {
            double norEpinephrine = Double.parseDouble(NOREPI);

            if(norEpinephrine <= 0.1 ) score = 3.0;
            if(norEpinephrine >= 0.1 ) score = 4.0;

        }

        return score;
    }


    public double getFinalScoreForDobutamine()
    {
        return 2.0;
    }


    public double getCardioVascularScore(int ageInMonths, int bp, double dopamine, double epinephrine,double nor_epinephrine, String dobutamine_any)
    {
        double score = 0.0;

        if(dopamine > 15 || epinephrine > 0.1 || nor_epinephrine > 0.1)
        {
            score = 4.0;
            return score;
        }
        if(dopamine > 5 || epinephrine <= 0.1 || nor_epinephrine <= 0.1)
        {
            score = 3.0;
            return score;
        }

        if(dopamine <= 5 || dobutamine_any.equalsIgnoreCase("ON"))
        {
            score = 2.0;
            return score;
        }

        // scores for creatinine count mg/dL
        if(ageInMonths < 1) {
            if (bp >= 46) score = 0.0;
            if (bp < 46) score = 1.0;
        }
        if(ageInMonths >= 1 && ageInMonths <= 11) {
            if (bp >= 55) score = 0.0;
            if (bp < 55) score = 1.0;
        }
        if(ageInMonths >= 12 && ageInMonths <= 23) {
            if (bp >= 60) score = 0.0;
            if (bp < 60) score = 1.0;
        }
        if(ageInMonths >= 24 && ageInMonths <= 59) {
            if (bp >= 62) score = 0.0;
            if (bp < 62) score = 1.0;
        }
        if(ageInMonths >= 60 && ageInMonths <= 143) {
            if (bp >= 65) score = 0.0;
            if (bp < 65) score = 1.0;
        }
        if(ageInMonths >= 144 && ageInMonths <= 216) {
            if (bp >= 67) score = 0.0;
            if (bp < 67) score = 1.0;
        }
        if(ageInMonths > 216) {
            if (bp >= 70) score = 0.0;
            if (bp < 70) score = 1.0;
        }

        return score;
    }

    /**
     *
     * @param glasgowComaScore
     * @return score
     */
    public double getNeurologicalScore(int glasgowComaScore)
    {
        double score = 0.0;
        if(glasgowComaScore != 0)
        {
            // scores for bilirubin count mg/dL
            if(glasgowComaScore == 15) score = 0.0;
            if(glasgowComaScore >= 13 && glasgowComaScore <= 14) score = 1.0;
            if(glasgowComaScore >= 10 && glasgowComaScore <= 12) score = 2.0;
            if(glasgowComaScore >= 6 && glasgowComaScore <= 9) score = 3.0;
            if(glasgowComaScore < 6) score = 4.0;
        }

        return score;
    }

    public double getRenalScore(int ageInMonths, double creatinineCount)
    {
        double score = 0.0;

        if(ageInMonths > 0 && creatinineCount > 0)
        {
            // scores for creatinine count mg/dL
            if(ageInMonths < 1) {
                if (creatinineCount < 0.8) score = 0.0;
                if(creatinineCount >= 0.8 && creatinineCount <= 0.9) score = 1.0;
                if(creatinineCount >= 1.0 && creatinineCount <= 1.1) score = 2.0;
                if(creatinineCount >= 1.2 && creatinineCount <= 1.5) score = 3.0;
                if(creatinineCount > 1.6) score = 4.0;
            }
            if(ageInMonths >= 1 && ageInMonths <= 11) {
                if (creatinineCount < 0.8) score = 0.0;
                if(creatinineCount >= 0.8 && creatinineCount <= 0.9) score = 1.0;
                if(creatinineCount >= 1.0 && creatinineCount <= 1.1) score = 2.0;
                if(creatinineCount >= 1.2 && creatinineCount <= 1.5) score = 3.0;
                if(creatinineCount > 1.6) score = 4.0;
            }
            if(ageInMonths >= 12 && ageInMonths <= 23) {
                if (creatinineCount < 0.8) score = 0.0;
                if(creatinineCount >= 0.8 && creatinineCount <= 0.9) score = 1.0;
                if(creatinineCount >= 1.0 && creatinineCount <= 1.1) score = 2.0;
                if(creatinineCount >= 1.2 && creatinineCount <= 1.5) score = 3.0;
                if(creatinineCount > 1.6) score = 4.0;
            }
            if(ageInMonths >= 24 && ageInMonths <= 59) {
                if (creatinineCount < 0.8) score = 0.0;
                if(creatinineCount >= 0.8 && creatinineCount <= 0.9) score = 1.0;
                if(creatinineCount >= 1.0 && creatinineCount <= 1.1) score = 2.0;
                if(creatinineCount >= 1.2 && creatinineCount <= 1.5) score = 3.0;
                if(creatinineCount > 1.6) score = 4.0;
            }
            if(ageInMonths >= 60 && ageInMonths <= 143) {
                if (creatinineCount < 0.8) score = 0.0;
                if(creatinineCount >= 0.8 && creatinineCount <= 0.9) score = 1.0;
                if(creatinineCount >= 1.0 && creatinineCount <= 1.1) score = 2.0;
                if(creatinineCount >= 1.2 && creatinineCount <= 1.5) score = 3.0;
                if(creatinineCount > 1.6) score = 4.0;
            }
            if(ageInMonths >= 144 && ageInMonths <= 216) {
                if (creatinineCount < 0.8) score = 0.0;
                if(creatinineCount >= 0.8 && creatinineCount <= 0.9) score = 1.0;
                if(creatinineCount >= 1.0 && creatinineCount <= 1.1) score = 2.0;
                if(creatinineCount >= 1.2 && creatinineCount <= 1.5) score = 3.0;
                if(creatinineCount > 1.6) score = 4.0;
            }
            if(ageInMonths > 216) {
                if (creatinineCount < 0.8) score = 0.0;
                if(creatinineCount >= 0.8 && creatinineCount <= 0.9) score = 1.0;
                if(creatinineCount >= 1.0 && creatinineCount <= 1.1) score = 2.0;
                if(creatinineCount >= 1.2 && creatinineCount <= 1.5) score = 3.0;
                if(creatinineCount > 1.6) score = 4.0;
            }

        }
        return score;
    }

    public static double calculateMaxScore()
    {
        double score  = 0.0;

        return score;
    }

    public static double calculateMinScore()
    {
        double score  = 0.0;

        return score;
    }

}
