package com.ray.apps.psofa.model;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Patient {

    public String id;
    public String name;
    public int age;
    public String gender;
    public String
            score_day1 = "0.0",
            score_day2 = "0.0",
            score_day3 = "0.0",
            score_day4 = "0.0",
            score_day5 = "0.0",
            score_day6 = "0.0",
            score_day7 = "0.0";
    public String final_result;

    public String getFinal_result() {
        return final_result;
    }

    public void setFinal_result(String final_result) {
        this.final_result = final_result;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getScore_day1() {
        return score_day1;
    }

    public void setScore_day1(String score_day1) {
        this.score_day1 = score_day1;
    }

    public String getScore_day2() {
        return score_day2;
    }

    public void setScore_day2(String score_day2) {
        this.score_day2 = score_day2;
    }

    public String getScore_day3() {
        return score_day3;
    }

    public void setScore_day3(String score_day3) {
        this.score_day3 = score_day3;
    }

    public String getScore_day4() {
        return score_day4;
    }

    public void setScore_day4(String score_day4) {
        this.score_day4 = score_day4;
    }

    public String getScore_day5() {
        return score_day5;
    }

    public void setScore_day5(String score_day5) {
        this.score_day5 = score_day5;
    }

    public String getScore_day6() {
        return score_day6;
    }

    public void setScore_day6(String score_day6) {
        this.score_day6 = score_day6;
    }

    public String getScore_day7() {
        return score_day7;
    }

    public void setScore_day7(String score_day7) {
        this.score_day7 = score_day7;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }


    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getGender() {
        return gender;
    }


    // Default constructor required for FireBase
    public Patient() {
    }

    public Patient(String id,
                   String name,
                   int age,
                   String gender,
                   String score_day1,
                   String score_day2,
                   String score_day3,
                   String score_day4,
                   String score_day5,
                   String score_day6,
                   String score_day7) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.score_day1 = score_day1;
        this.score_day2 = score_day2;
        this.score_day3 = score_day3;
        this.score_day4 = score_day4;
        this.score_day5 = score_day5;
        this.score_day6 = score_day6;
        this.score_day7 = score_day7;
    }

}
