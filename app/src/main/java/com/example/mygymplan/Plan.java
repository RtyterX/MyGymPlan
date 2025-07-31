package com.example.mygymplan;

public class Plan {

    public String planName;
    public Workout[] planWorkouts;
    public Boolean active;


    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public Workout[] getPlanWorkouts() {
        return planWorkouts;
    }

    public void setPlanWorkouts(Workout[] planWorkouts) {
        this.planWorkouts = planWorkouts;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Plan(Workout[] planWorkouts, Boolean active, String planName) {
        this.planWorkouts = planWorkouts;
        this.active = active;
        this.planName = planName;
    }



}
