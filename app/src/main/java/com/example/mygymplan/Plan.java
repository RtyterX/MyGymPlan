package com.example.mygymplan;

import java.util.ArrayList;

public class Plan {

    public String planName;
    // public Workout[] planWorkouts;
    public ArrayList<Workout> planWorkouts;
    public Boolean active;


    public Plan(String planName, ArrayList<Workout> planWorkouts, Boolean active) {
        this.planName = planName;
        this.planWorkouts = planWorkouts;
        this.active = active;
    }

    public ArrayList<Workout> getPlanWorkouts() {
        return planWorkouts;
    }

    public void setPlanWorkouts(ArrayList<Workout> planWorkouts) {
        this.planWorkouts = planWorkouts;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }
}
