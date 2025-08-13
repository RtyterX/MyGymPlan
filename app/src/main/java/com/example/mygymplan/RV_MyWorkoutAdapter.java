package com.example.mygymplan;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;

public class RV_MyWorkoutAdapter extends RecyclerView.Adapter<RV_MyWorkoutAdapter.MyViewHolder>  {

    Context context;
    ArrayList<Workout> workoutList;

    // Constructor
    public RV_MyWorkoutAdapter(Context context, ArrayList<Workout> workoutList) {
        this.context = context;
        this.workoutList = workoutList;
    }

    @NonNull
    @Override
    public RV_MyWorkoutAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Where you inflate the Layout

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_view_exercise, parent, false);

        return new RV_MyWorkoutAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RV_MyWorkoutAdapter.MyViewHolder holder, int position) {
        // Assigning values to the view we created in the recycler view row Layout file
        // Based on the position of the Recycler View

        holder.textViewName.setText(workoutList.get(position).getwName());
        holder.textViewType.setText(Arrays.toString(workoutList.get(position).getTypes()));
        holder.textViewDescription.setText(workoutList.get(position).getwDescription());
        // holder.imageView.setImageResource(myWorkout.get(position).getwImage());


    }

    @Override
    public int getItemCount() {
        // The Recycle view just want to know how many items you want to display

        return WorkoutList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // Grab views from Recycle View Row Layout file
        // Similar to onCreate method

        TextView textViewName;
        TextView textViewType;
        TextView textViewDescription;
        // ImageView imageView;


        // Constructor
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewName = itemView.findViewById(R.id.RecyclerWorkoutName);
            textViewType = itemView.findViewById(R.id.RecyclerWorkoutType);
            textViewDescription = itemView.findViewById(R.id.RecyclerWorkoutDescription);
            // imageView = itemView.findViewById(R.id.RecyclerWorkoutImage);
        }
    }

}
