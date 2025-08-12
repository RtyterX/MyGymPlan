package com.example.mygymplan;

import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;


public class Plan implements Serializable {

    public int id;
    public String planName;
    public Boolean active;
    public ArrayList<Workout> planWorkouts;

    // ---- Constructor ----

    public Plan(int id, String planName, ArrayList<Workout> planWorkouts, Boolean active) {
        this.id = id;
        this.planName = planName;
        this.planWorkouts = planWorkouts;
        this.active = active;
    }


    // ---- Getters and Setters ----

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public ArrayList<Workout> getPlanWorkouts() {
        return planWorkouts;
    }

    public void setPlanWorkouts(ArrayList<Workout> planWorkouts) {
        this.planWorkouts = planWorkouts;
    }
}
