package com.example.mygymplan.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mygymplan.Exercise;
import com.example.mygymplan.R;

import java.util.List;

public class ExerciseRVAdapter extends RecyclerView.Adapter<ExerciseRVAdapter.MyViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(Exercise item);
        void deletebuttonClick(Exercise item);

    }

    Context context;
    List<Exercise> exerciseList;
    OnItemClickListener onListener;


    // Constructor
    public ExerciseRVAdapter(Context context, List<Exercise> exerciseList, OnItemClickListener onListener) {
        this.context = context;
        this.exerciseList = exerciseList;
        this.onListener = onListener;
    }

    @NonNull
    @Override
    public ExerciseRVAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Where you inflate the Layout

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_view_exercise, parent, false);

        return new ExerciseRVAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseRVAdapter.MyViewHolder holder, int position) {
        // Assigning values to the view we created in the recycler view row Layout file
        // Based on the position of the Recycler View

        // Text Views
        holder.textViewName.setText(exerciseList.get(position).eName);
        holder.textViewType.setText(exerciseList.get(position).eType.toString());
        holder.textViewSets.setText(exerciseList.get(position).eSets);
        holder.textViewReps.setText(exerciseList.get(position).eReps);

        // Delete Button
        holder.bind(exerciseList.get(position), onListener);
        //holder.bind2(exerciseList.get(position), onListener);

        // Image View
        // holder.imageView.setImageResource(exerciseList.get(position).geteImage());
    }


    @Override
    public int getItemCount() {
        // The Recycle view just want to know how many items you want to display

        return exerciseList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // Grab views from Recycle View Row Layout file
        // Similar to onCreate method

        TextView textViewName;
        TextView textViewType;
        TextView textViewSets;
        TextView textViewReps;

        //ImageView imageView;
        ImageButton deleteButton;

        // Constructor
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewName = itemView.findViewById(R.id.RecyclerExerciseName);
            textViewType = itemView.findViewById(R.id.RecyclerExerciseType);
            // imageView = itemView.findViewById(R.id.ImageRecyclerExercise);
            textViewSets = itemView.findViewById(R.id.RecyclerExerciseSets);
            textViewReps = itemView.findViewById(R.id.RecyclerExerciseReps);
            deleteButton = itemView.findViewById(R.id.DeleteExerciseRVButton);
        }
        public void bind(Exercise item, OnItemClickListener onlistener) {

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onlistener.onItemClick(item);
                }
            });


        }

        public void bind2(Exercise item, OnItemClickListener onlistener) {

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //AppDatabase db = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "workouts").build();
                    //ExerciseDao dao = db.exerciseDao();

                    onlistener.deletebuttonClick(item);
                }
            });
        }



    }


}
