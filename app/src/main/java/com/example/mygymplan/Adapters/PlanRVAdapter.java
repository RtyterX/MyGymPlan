package com.example.mygymplan.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mygymplan.Entitys.Plan;
import com.example.mygymplan.R;

import java.util.List;

public class PlanRVAdapter extends RecyclerView.Adapter<com.example.mygymplan.Adapters.PlanRVAdapter.MyViewHolder>  {
    public interface OnItemClickListener {
        void onItemClick(Plan item);
    }
    public interface OnItemClickDelete {
        void deleteButtonClick(int position);
    }
    public interface OnItemClickSetActive {
        void setActiveButtonClick(Plan plan);
    }

    public interface OnClickEditPlanListener {
        void editButtonClick(Plan plan);
    }

    Context context;
    List<Plan> planList;
    OnItemClickListener onListener;
    OnItemClickDelete deleteListener;
    OnItemClickSetActive setActiveListener;
    OnClickEditPlanListener editListener;



    // Constructor
    public PlanRVAdapter(Context context, List<Plan> planList, OnItemClickListener onListener, OnItemClickDelete deleteListener, OnItemClickSetActive setActiveListener, OnClickEditPlanListener editListener) {
        this.context = context;
        this.planList = planList;
        this.onListener = onListener;
        this.deleteListener = deleteListener;
        this.setActiveListener = setActiveListener;
        this.editListener = editListener;
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

        holder.textViewName.setText(planList.get(position).name);
        // holder.textViewDescription.setText(planList.get(position).description);
        holder.textViewCreatedDate.setText(planList.get(position).createdDate);
        holder.textViewAuthor.setText(planList.get(position).author);

        // Body Type
        if (planList.get(position).bodyType != 0) {
            holder.textViewBodyType.setText(String.valueOf(planList.get(position).bodyType));
        }
        else {
            holder.textViewBodyType.setVisibility(View.GONE);
            holder.textViewBodyTypeTitle.setVisibility(View.GONE);
        }

        // Fixed Days
        if (planList.get(position).fixedDays == true) {
            holder.hasFixedDays.setText("Fixed Days");
        }
        else {
            holder.hasFixedDays.setText("NO Fixed Days");
        }

        // Edit Plan
        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editListener.editButtonClick(planList.get(position));
            }
        });

        // Delete Plan
        if (planList.size() >= 2) {
            holder.deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteListener.deleteButtonClick(position);
                }
            });
        }
        else {
            holder.deleteButton.setVisibility(View.GONE);
        }

        // Set Active
        if (!planList.get(position).active) {
            holder.setActiveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setActiveListener.setActiveButtonClick(planList.get(position));
                }
            });
        }
        else {
            holder.setActiveButton.setVisibility(View.GONE);
        }

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
        TextView textViewBodyType;
        TextView textViewBodyTypeTitle;
        TextView textViewAuthor;
        TextView hasFixedDays;

        // TextView textViewWorkoutTypes;
        ImageView imageView;
        ImageView editButton;
        ImageView deleteButton;
        Button setActiveButton;



        // Constructor
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewName = itemView.findViewById(R.id.RecyclerPlanName);
            textViewDescription = itemView.findViewById(R.id.RecyclerPlanDescrip);
            textViewCreatedDate = itemView.findViewById(R.id.CreatedDatePlan);
            textViewBodyType = itemView.findViewById(R.id.BodyTypePlanRv);
            textViewBodyTypeTitle = itemView.findViewById(R.id.BodyTypeTitleRV);
            hasFixedDays = itemView.findViewById(R.id.FixedDaysText);
            // textViewType = itemView.findViewById(R.id.WorkoutTypePlanRv);
            imageView = itemView.findViewById(R.id.RecyclePlanImage);
            editButton = itemView.findViewById(R.id.EditPlanRV);
            deleteButton = itemView.findViewById(R.id.DeletePlanButton);
            setActiveButton = itemView.findViewById(R.id.SetPlanActiveRV);
            textViewAuthor = itemView.findViewById(R.id.PlanRVAuthor);
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
