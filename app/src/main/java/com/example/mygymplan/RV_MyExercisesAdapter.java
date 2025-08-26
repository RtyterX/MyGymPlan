package com.example.mygymplan;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RV_MyExercisesAdapter extends RecyclerView.Adapter<RV_MyExercisesAdapter.MyViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(Exercise item);

    }

    Context context;
    List<Exercise> exerciseList;
    OnItemClickListener onListener;


    // Constructor
    public RV_MyExercisesAdapter(Context context, List<Exercise> exerciseList, OnItemClickListener onListener) {
        this.context = context;
        this.exerciseList = exerciseList;
        this.onListener = onListener;
    }

    @NonNull
    @Override
    public RV_MyExercisesAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Where you inflate the Layout

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_view_exercise, parent, false);

        return new RV_MyExercisesAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RV_MyExercisesAdapter.MyViewHolder holder, int position) {
        // Assigning values to the view we created in the recycler view row Layout file
        // Based on the position of the Recycler View

        holder.textViewName.setText(exerciseList.get(position).eName);
        holder.bind(exerciseList.get(position), onListener);
        holder.deletebutton.setOnClickListener(v -> {
            exerciseList.deleteExercise(exerciseList.get(position));
        };
        // holder.textViewType.setText(exerciseList.get(position).geteType().toString());
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
        //TextView textViewType;
        //ImageView imageView;
        Button deleteButton;

        // Constructor
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewName = itemView.findViewById(R.id.RecyclerExerciseName);
            //textViewType = itemView.findViewById(R.id.RecyclerExerciseType);
            // imageView = itemView.findViewById(R.id.ImageRecyclerExercise);
            deleteButton = itemView.findViewById(R.id.DeleteExerciseButton)
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
