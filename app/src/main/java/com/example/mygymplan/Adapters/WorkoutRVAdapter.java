package com.example.mygymplan.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mygymplan.R;
import com.example.mygymplan.Entitys.Workout;
import com.example.mygymplan.Services.WorkoutDuration;

import java.util.List;

public class WorkoutRVAdapter extends RecyclerView.Adapter<WorkoutRVAdapter.MyViewHolder>  {

    public interface OnItemClickListener {
        void onItemClick(Workout item);

        void onItemLongClick(Workout item);

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

        WorkoutDuration workoutDuration = new WorkoutDuration();    // needs to be here to work properly

        holder.textViewName.setText(workoutList.get(position).wName);
        holder.textViewDescription.setText(workoutList.get(position).wDescription);
        holder.textViewType.setText(workoutList.get(position).wType.toString());
        holder.textViewLastMod.setText(workoutList.get(position).lastModified);
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

        // Calculate Workout Duration Time
        //new Thread(new Runnable() {
            //@Override
            //public void run() {

                //AppDatabase db = Room.databaseBuilder(context, AppDatabase.class, "workouts").build();
               // ExerciseDao dao = db.exerciseDao();

               // listExercises = dao.listExercise();

               // holder.textViewDuration.setText(workoutDuration.CalculateDurationTime(workoutList.get(position), listExercises));

           // }
      //  }).start();
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
