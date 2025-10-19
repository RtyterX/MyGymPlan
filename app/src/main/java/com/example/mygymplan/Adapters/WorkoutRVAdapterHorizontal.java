package com.example.mygymplan.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mygymplan.Entitys.Workout;
import com.example.mygymplan.R;

import java.util.List;

public class WorkoutRVAdapterHorizontal extends RecyclerView.Adapter<WorkoutRVAdapterHorizontal.MyViewHolder>  {

    public interface OnItemClickListener {
        void onItemClick(Workout item);
    }

    Context context;
    List<Workout> workoutList;
    OnItemClickListener onListener;


    // Constructor
    public WorkoutRVAdapterHorizontal(Context context, List<Workout> workoutList, OnItemClickListener onListener) {
        this.context = context;
        this.workoutList = workoutList;
        this.onListener = onListener;
    }

    @NonNull
    @Override
    public WorkoutRVAdapterHorizontal.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Where you inflate the Layout

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_view_workout_horizontal, parent, false);

        return new WorkoutRVAdapterHorizontal.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkoutRVAdapterHorizontal.MyViewHolder holder, int position) {
        // Assigning values to the view we created in the recycler view row Layout file
        // Based on the position of the Recycler View
        holder.textViewName.setText(workoutList.get(position).wName);

        // On Item Click
        holder.bind(workoutList.get(position), onListener);
    }

    @Override
    public int getItemCount() {
        // The Recycle view just want to know how many items you want to display

        return workoutList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // Grab views from Recycle View Row Layout file
        // Similar to onCreate method
        TextView textViewName;


        // Constructor
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewName = itemView.findViewById(R.id.RecyclerWorkoutHorizontalName);
        }

        public void bind(Workout item, OnItemClickListener onlistener) {

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onlistener.onItemClick(item);
                }
            });

        }
    }

}
