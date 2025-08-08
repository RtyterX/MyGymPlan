package com.example.mygymplan;

import android.provider.MediaStore;
import android.text.Editable;
import android.widget.EditText;

public class Exercise {
    public String eName;
    public int eImage;
    public String eDescription;
    public WorkoutType eType;
    public int eSets;
    public int eReps;
    public int eRest; // Time
    public int eLoad;


    public Exercise(String eName, int eImage, String eDescription, WorkoutType eType, int eSets, int eReps, int eRest, int eLoad) {
        this.eName = eName;
        this.eImage = eImage;
        this.eDescription = eDescription;
        this.eType = eType;
        this.eSets = eSets;
        this.eReps = eReps;
        this.eRest = eRest;
        this.eLoad = eLoad;
    }

    public Exercise(EditText eName, Editable text, Editable text1, Editable text2, Editable text3, Editable text4) {
    }


    public String geteName() {
        return eName;
    }

    public void seteName(String eName) {
        this.eName = eName;
    }

    public int geteImage() {
        return eImage;
    }

    public void seteImage(int eImage) {
        this.eImage = eImage;
    }

    public String geteDescription() {
        return eDescription;
    }

    public void seteDescription(String eDescription) {
        this.eDescription = eDescription;
    }

    public WorkoutType geteType() {
        return eType;
    }

    public void seteType(WorkoutType eType) {
        this.eType = eType;
    }

    public int geteSets() {
        return eSets;
    }

    public void seteSets(int eSets) {
        this.eSets = eSets;
    }

    public int geteReps() {
        return eReps;
    }

    public void seteReps(int eReps) {
        this.eReps = eReps;
    }

    public int geteRest() {
        return eRest;
    }

    public void seteRest(int eRest) {
        this.eRest = eRest;
    }

    public int geteLoad() {
        return eLoad;
    }

    public void seteLoad(int eLoad) {
        this.eLoad = eLoad;
    }
}