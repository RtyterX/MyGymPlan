package com.example.mygymplan.Services;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class TimerService {

    TimerTask timerTask;
    Double timeCount;

    public void StartTimer(Double time, View v, Context context) {
        TextView text = (TextView) v;
        timeCount = time;
        timeCount--;
        text.setText(GetTimerTextInSeconds(timeCount));
        Toast.makeText(context, "Timer: " + timeCount, Toast.LENGTH_LONG).show();

        // return GetTimerTextInSeconds(timeCount);
    }

    public void StopTimer() {
        timerTask.cancel();
    }

    public String GetTimerTextInSeconds(double timeInSeconds) {

        int milliseconds = (int) (timeInSeconds / 1000);
        int seconds = (int) (timeInSeconds % 60);
        int minutes = (int) (timeInSeconds / 60);

        return FormateTime(milliseconds, seconds, minutes);
    }

    public String GetTimerTextInMilliseconds(int time) {

        int round = (int) Math.round(timeCount);

        int seconds = ((round % 86400) % 3600) % 60;
        int minutes = ((round % 86400) % 3600) / 60;

        return FormateTime(round, seconds, minutes);
    }

    public String FormateTime(int milliseconds, int seconds, int minutes) {

        return String.format("%02d",minutes) + ":" + String.format("%02d",seconds) + ":" + String.format("%02d",milliseconds);
    }


}
