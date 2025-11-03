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
import com.example.mygymplan.Entitys.Plan;
import com.example.mygymplan.R;

import java.util.List;

public class PlanRVAdapter extends RecyclerView.Adapter<com.example.mygymplan.Adapters.PlanRVAdapter.MyViewHolder>  {
    public interface OnItemClickListener {
        void onItemClick(Plan item);
    }
    public interface OnItemClickDelete {
        void deleteButtonClick(Plan item);
    }

    Context context;
    List<Plan> planList;
    OnItemClickListener onListener;
    OnItemClickDelete deleteListener;


    // Constructor
    public PlanRVAdapter(Context context, List<Plan> planList, OnItemClickListener onListener, OnItemClickDelete deleteListener) {
        this.context = context;
        this.planList = planList;
        this.onListener = onListener;
        this.deleteListener = deleteListener;
    }

    @NonNull
    @Override
    public PlanRVAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        // Where you inflate the Layout
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_view_plan, parent, false);

        return new PlanRVAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlanRVAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        // Assigning values to the view we created in the recycler view row Layout file
        // Based on the position of the Recycler View

        holder.textViewName.setText(planList.get(position).planName);
        holder.textViewDescription.setText(planList.get(position).planDescription);
        // holder.textViewType.setText(Arrays.toString(workoutList.get(position).wType);
        //holder.textViewCreatedDate.setText(planList.get(position).createdDate);

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteListener.deleteButtonClick(planList.get(position));
            }
        });

        // holder.imageView.setImageResource(myWorkout.get(position).getwImage());

        // On Item Click
        holder.bind(planList.get(position), onListener);
    }

    @Override
    public int getItemCount() {
        // The Recycle view just want to know how many items you want to display

        return planList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // Grab views from Recycle View Row Layout file
        // Similar to onCreate method
        TextView textViewName;
        TextView textViewDescription;
        TextView textViewCreatedDate;
        //TextView textViewType;
        ImageView imageView;
        ImageView deleteButton;


        // Constructor
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewName = itemView.findViewById(R.id.RecyclerPlanName);
            textViewDescription = itemView.findViewById(R.id.RecyclerPlanDescrip);
            //textViewType = itemView.findViewById(R.id.RecyclerWorkoutType);
            imageView = itemView.findViewById(R.id.RecyclePlanImage);
            deleteButton = itemView.findViewById(R.id.DeletePlanButton);

        }

        public void bind(Plan item, OnItemClickListener onlistener) {

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onlistener.onItemClick(item);
                }
            });

        }
    }

}
