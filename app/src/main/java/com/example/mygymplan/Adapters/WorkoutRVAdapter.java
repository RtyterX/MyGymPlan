package com.example.mygymplan.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mygymplan.R;
import com.example.mygymplan.Workout;

import java.util.List;

public class WorkoutRVAdapter extends RecyclerView.Adapter<WorkoutRVAdapter.MyViewHolder>  {

    public interface OnItemClickListener {
        void onItemClick(Workout item);

    }

    Context context;
    List<Workout> workoutList;
    OnItemClickListener onListener;

    // Constructor
    public WorkoutRVAdapter(Context context, List<Workout> workoutList, OnItemClickListener onListener) {
        this.context = context;
        this.workoutList = workoutList;
        this.onListener = onListener;
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
    public void onBindViewHolder(@NonNull WorkoutRVAdapter.MyViewHolder holder, int position) {
        // Assigning values to the view we created in the recycler view row Layout file
        // Based on the position of the Recycler View

        holder.textViewName.setText(workoutList.get(position).wName);
        holder.textViewDescription.setText(workoutList.get(position).wDescription);
        // holder.textViewType.setText(Arrays.toString(workoutList.get(position).wType);

        // holder.imageView.setImageResource(myWorkout.get(position).getwImage());

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
        //TextView textViewType;
        // ImageView imageView;


        // Constructor
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewName = itemView.findViewById(R.id.RecyclerWorkoutName);
            textViewDescription = itemView.findViewById(R.id.RecyclerWorkoutDescription);
            //textViewType = itemView.findViewById(R.id.RecyclerWorkoutType);
            // imageView = itemView.findViewById(R.id.RecyclerWorkoutImage);
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
