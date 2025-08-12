package com.example.mygymplan;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RV_MyExercisesAdapter extends RecyclerView.Adapter<RV_MyExercisesAdapter.MyViewHolder> {

    UserData user;
    Context context;

    ArrayList<Exercise> myExercises;


    // Constructor
    public RV_MyExercisesAdapter(Context context, ArrayList<Exercise> myExercises) {
        this.context = context;
        this.myExercises = myExercises;
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

        holder.textViewName.setText(myExercises.get(position).geteName());
        holder.textViewType.setText(myExercises.get(position).geteType().toString());
        holder.imageView.setImageResource(myExercises.get(position).geteImage());
    }

    @Override
    public int getItemCount() {
        // The Recycle view just want to know how many items you want to display

        return user.myExercises.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // Grab views from Recycle View Row Layout file
        // Similar to onCreate method

        TextView textViewName;
        TextView textViewType;
        ImageView imageView;

        // Constructor
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewName = itemView.findViewById(R.id.RecyclerExerciseName);
            textViewType = itemView.findViewById(R.id.RecyclerExerciseType);
            imageView = itemView.findViewById(R.id.ImageRecyclerExercise);
        }
    }
}
