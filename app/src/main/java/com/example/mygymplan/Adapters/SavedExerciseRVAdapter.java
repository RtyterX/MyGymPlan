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

import com.example.mygymplan.Entitys.Exercise;
import com.example.mygymplan.Entitys.SavedExercise;
import com.example.mygymplan.R;
import com.example.mygymplan.Services.ImageConverter;

import java.util.List;

public class SavedExerciseRVAdapter extends RecyclerView.Adapter<SavedExerciseRVAdapter.MyViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(SavedExercise item);
    }

    public interface OnShareClickListener {
        void onItemShare(SavedExercise item);
    }

    public interface OnEditClickListener {
        void onItemEdit(SavedExercise item);
    }

    Context context;
    List<SavedExercise> savedExerciseList;
    OnItemClickListener onListener;
    OnShareClickListener shareListener ;
    OnEditClickListener editListener;

    ImageConverter imageConverter = new ImageConverter();


    // Constructor
    public SavedExerciseRVAdapter(Context context, List<SavedExercise> savedExerciseList, OnItemClickListener onListener, OnShareClickListener shareListener, OnEditClickListener editListener) {
        this.context = context;
        this.savedExerciseList = savedExerciseList;
        this.onListener = onListener;
        this.shareListener = shareListener;
        this.editListener = editListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Where you inflate the Layout
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_view_exercise_add, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        // Assigning values to the view we created in the recycler view row Layout file
        // Based on the position of the Recycler View

        // Text Views
        holder.textViewName.setText(savedExerciseList.get(position).name);
        holder.textViewType.setText(savedExerciseList.get(position).type.toString());
        holder.textViewDeleteDate.setText(String.valueOf(savedExerciseList.get(position).createdDate));
        // holder.imageView.setImageResource(exerciseList.get(position).geteImage());

        // Image View
        if (savedExerciseList.get(position).image != null) {
            holder.imageView.setImageBitmap(imageConverter.ConvertToBitmap(savedExerciseList.get(position).image));
        }

        // Edit Button
        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editListener.onItemEdit(savedExerciseList.get(position));
            }
        });

        // Share Button
        holder.shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareListener.onItemShare(savedExerciseList.get(position));
            }
        });

        // On Item Click ( Everything inside "bind()" )
        holder.bind(savedExerciseList.get(position), onListener);
    }


    @Override
    public int getItemCount() {
        // The Recycle view just want to know how many items you want to display
        return savedExerciseList.size();
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

        // Constructor
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewName = itemView.findViewById(R.id.AddExerciseRVName);
            textViewType = itemView.findViewById(R.id.AddExerciseRVType);
            textViewDeleteDate = itemView.findViewById(R.id.AddExerciseRVDelete);
            imageView = itemView.findViewById(R.id.AddExerciseImageRV);
            shareButton = itemView.findViewById(R.id.ShareExerciseIcon);
            editButton = itemView.findViewById(R.id.EditSavedExerciseIcon);
        }

        public void bind(SavedExercise item, OnItemClickListener onlistener) {

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onlistener.onItemClick(item);
                }
            });

        }

    }

}