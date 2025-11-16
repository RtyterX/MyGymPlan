package com.example.mygymplan.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.mygymplan.Database.AppDatabase;
import com.example.mygymplan.Database.ExerciseDao;
import com.example.mygymplan.Entitys.Exercise;
import com.example.mygymplan.Enums.WorkoutType;
import com.example.mygymplan.R;
import com.example.mygymplan.Entitys.Workout;
import com.example.mygymplan.Services.WorkoutDuration;

import java.util.List;

public class WorkoutRVAdapter extends RecyclerView.Adapter<WorkoutRVAdapter.MyViewHolder>  {

    public interface OnItemClickListener {
        void onItemClick(Workout item);
    }

    public interface OnClickEditListener {
        void editButtonClick(Workout workout);
    }

    Context context;
    List<Workout> workoutList;
    OnItemClickListener onListener;
    OnClickEditListener editListener;


    // Constructor
    public WorkoutRVAdapter(Context context, List<Workout> workoutList, OnItemClickListener onListener, OnClickEditListener editListener) {
        this.context = context;
        this.workoutList = workoutList;
        this.onListener = onListener;
        this.editListener = editListener;
    }

    @NonNull
    @Override
    public WorkoutRVAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Where you inflate the Layout

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_view_workout, parent, false);

        return new WorkoutRVAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkoutRVAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        // Assigning values to the view we created in the recycler view row Layout file
        // Based on the position of the Recycler View
        holder.textViewName.setText(workoutList.get(position).name);
        holder.textViewDescription.setText(workoutList.get(position).description);
        holder.textViewLastMod.setText(workoutList.get(position).lastModified);

        // Calculate Workout Duration Time
        holder.textViewDuration.setText(workoutList.get(position).duration);

        // Workout Type
        if (workoutList.get(position).type == WorkoutType.NA) {
            holder.textViewType.setVisibility(View.GONE);
        }
        else {
            holder.textViewType.setText(workoutList.get(position).type.toString());
        }

        // Image
        // holder.imageView.setImageResource(myWorkout.get(position).getwImage());

        // Edit Button
        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editListener.editButtonClick(workoutList.get(position));
            }
        });

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
        TextView textViewDescription;
        TextView textViewType;
        TextView textViewDuration;
        TextView textViewLastMod;
        ImageView imageView;
        ImageView editButton;


        // Constructor
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewName = itemView.findViewById(R.id.RecyclerWorkoutName);
            textViewDescription = itemView.findViewById(R.id.RecyclerWorkoutDescription);
            textViewType = itemView.findViewById(R.id.RecyclerWorkoutType);
            textViewLastMod = itemView.findViewById(R.id.LastModifiedWorkout);
            imageView = itemView.findViewById(R.id.RecyclerWorkoutImage);
            textViewDuration = itemView.findViewById(R.id.WorkoutDurationTime);
            editButton = itemView.findViewById(R.id.EditWorkoutRV);
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
