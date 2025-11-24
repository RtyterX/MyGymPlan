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

import com.example.mygymplan.Entitys.SavedExercise;
import com.example.mygymplan.Entitys.SavedWorkout;
import com.example.mygymplan.R;
import com.example.mygymplan.Services.ImageConverter;

import java.util.List;

public class SavedWorkoutRVAdapter extends RecyclerView.Adapter<SavedWorkoutRVAdapter.MyViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(SavedWorkout item);
    }

    public interface OnShareClickListener {
        void onItemShare(SavedWorkout item);
    }

    public interface OnEditClickListener {
        void onItemEdit(SavedWorkout item);
    }

    public interface OnDeleteClickListener {
        void onItemDelete(int position);
    }

    Context context;
    List<SavedWorkout> savedWorkoutList;
    SavedWorkoutRVAdapter.OnItemClickListener onListener;
    SavedWorkoutRVAdapter.OnShareClickListener shareListener ;
    SavedWorkoutRVAdapter.OnEditClickListener editListener;
    SavedWorkoutRVAdapter.OnDeleteClickListener deleteListener;

    ImageConverter imageConverter = new ImageConverter();


    // Constructor
    public SavedWorkoutRVAdapter(Context context, List<SavedWorkout> savedWorkoutList, SavedWorkoutRVAdapter.OnItemClickListener onListener, SavedWorkoutRVAdapter.OnShareClickListener shareListener, SavedWorkoutRVAdapter.OnEditClickListener editListener, SavedWorkoutRVAdapter.OnDeleteClickListener deleteListener) {
        this.context = context;
        this.savedWorkoutList = savedWorkoutList;
        this.onListener = onListener;
        this.shareListener = shareListener;
        this.editListener = editListener;
        this.deleteListener = deleteListener;
    }

    @NonNull
    @Override
    public SavedWorkoutRVAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Where you inflate the Layout
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_view_workout, parent, false);

        return new SavedWorkoutRVAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SavedWorkoutRVAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        // Assigning values to the view we created in the recycler view row Layout file
        // Based on the position of the Recycler View

        // Text Views
        holder.textViewName.setText(savedWorkoutList.get(position).name);
        holder.textViewType.setText(savedWorkoutList.get(position).type.toString());
        holder.textViewDeleteDate.setText(String.valueOf(savedWorkoutList.get(position).createdDate));
        // holder.imageView.setImageResource(exerciseList.get(position).geteImage());

        // Image View


        // On Item Click ( Everything inside "bind()" )
        holder.bind(savedWorkoutList.get(position), onListener);

        // Edit Button
        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editListener.onItemEdit(savedWorkoutList.get(position));
            }
        });


        // Share Button
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteListener.onItemDelete(position);
            }
        });

    }


    @Override
    public int getItemCount() {
        // The Recycle view just want to know how many items you want to display
        return savedWorkoutList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // Grab views from Recycle View Row Layout file
        // Similar to onCreate method
        TextView textViewName;
        TextView textViewType;
        TextView textViewDeleteDate;
        ImageView imageView;
        ImageView shareButton;
        ImageView editButton;
        ImageView deleteButton;

        // Constructor
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewName = itemView.findViewById(R.id.AddExerciseRVName);
            textViewType = itemView.findViewById(R.id.AddExerciseRVType);
            textViewDeleteDate = itemView.findViewById(R.id.AddExerciseRVDelete);
            imageView = itemView.findViewById(R.id.AddExerciseImageRV);
            shareButton = itemView.findViewById(R.id.ShareExerciseIcon);
            editButton = itemView.findViewById(R.id.EditSavedExerciseIcon);
            deleteButton = itemView.findViewById(R.id.DeleteSavedExerciseIcon);
        }

        public void bind(SavedWorkout item, SavedWorkoutRVAdapter.OnItemClickListener onlistener) {

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onlistener.onItemClick(item);
                }
            });

        }

    }

}
