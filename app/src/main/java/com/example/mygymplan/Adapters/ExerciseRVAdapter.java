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

import com.example.mygymplan.Entitys.Exercise;
import com.example.mygymplan.R;

import java.util.List;

public class ExerciseRVAdapter extends RecyclerView.Adapter<ExerciseRVAdapter.MyViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(Exercise item);
    }

    public interface OnItemClickDelete {
        void deleteButtonClick(int position);
    }

    public interface OnItemSwapPositionsClick {
        void swapButtonClick(int position);
    }

    Context context;
    List<Exercise> exerciseList;
    OnItemClickListener onListener;

    OnItemClickDelete deleteListener;

    OnItemSwapPositionsClick onSwapListener;


    // Constructor
    public ExerciseRVAdapter(Context context, List<Exercise> exerciseList, OnItemClickListener onListener, OnItemClickDelete deleteListener, OnItemSwapPositionsClick onSwapListener) {
        this.context = context;
        this.exerciseList = exerciseList;
        this.onListener = onListener;
        this.deleteListener = deleteListener;
        this.onSwapListener = onSwapListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Where you inflate the Layout

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_view_exercise, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        // Assigning values to the view we created in the recycler view row Layout file
        // Based on the position of the Recycler View

        // Text Views
        holder.textViewName.setText(exerciseList.get(position).eName);
        holder.textViewType.setText(exerciseList.get(position).eType.toString());
        holder.textViewSets.setText(String.valueOf(exerciseList.get(position).eSets));
        holder.textViewReps.setText(String.valueOf(exerciseList.get(position).eReps));
        holder.textViewRest.setText(String.valueOf(exerciseList.get(position).eRest));
        holder.textViewLoad.setText(String.valueOf(exerciseList.get(position).eLoad));
        holder.textViewLastMod.setText(String.valueOf(exerciseList.get(position).lastModified));

        // Swap Positions Button
        holder.swapPositionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSwapListener.swapButtonClick(position);
            }
        });

        // Delete Button
        holder.deleteExerciseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteListener.deleteButtonClick(position);
            }
        });

        // Image View
        // holder.imageView.setImageResource(exerciseList.get(position).geteImage());

        // On Item Click ( Everything inside "bind()" )
        holder.bind(exerciseList.get(position), onListener);
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
        TextView textViewRest;
        TextView textViewLoad;
        TextView textViewLastMod;
        ImageButton swapPositionButton;
        ImageButton deleteExerciseButton;
        ImageView imageView;

        // Constructor
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewName = itemView.findViewById(R.id.RecyclerExerciseName);
            textViewType = itemView.findViewById(R.id.RecyclerExerciseType);
            textViewSets = itemView.findViewById(R.id.RecyclerExerciseSets);
            textViewReps = itemView.findViewById(R.id.RecyclerExerciseReps);
            textViewRest = itemView.findViewById(R.id.RecyclerExerciseRest);
            textViewLoad = itemView.findViewById(R.id.RecyclerExerciseLoad);
            textViewLastMod = itemView.findViewById(R.id.LastModifiedExercise);
            swapPositionButton = itemView.findViewById(R.id.SwapPositionsExerciseRV);
            deleteExerciseButton = itemView.findViewById(R.id.DeleteWorkoutIcon);
            imageView = itemView.findViewById(R.id.ImageRecyclerExercise);
        }

        public void bind(Exercise item, OnItemClickListener onlistener) {

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onlistener.onItemClick(item);
                }
            });

        }

    }

}
