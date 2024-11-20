package com.olajideosho.komodohub.ui.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.olajideosho.komodohub.R;
import com.olajideosho.komodohub.data.model.ConservationActivity;
import com.olajideosho.komodohub.ui.activities.ConservationActivityDetailsActivity;

import java.util.List;

public class ConservationActivityAdapter extends RecyclerView.Adapter<ConservationActivityAdapter.ActivityViewHolder> {
    private List<ConservationActivity> activityList;
    private int userId;

    public ConservationActivityAdapter(List<ConservationActivity> activityList, int userId) {
        this.activityList = activityList;
        this.userId = userId;
    }

    @NonNull
    @Override
    public ActivityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_conservation_activity, parent, false);
        return new ActivityViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ActivityViewHolder holder, int position)
    {
        ConservationActivity activity = activityList.get(position);
        holder.activityNameTextView.setText(activity.getActivityName());
        holder.activityDescriptionTextView.setText(activity.getDescription());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.itemView.getContext(), ConservationActivityDetailsActivity.class);
                intent.putExtra("activityId", activity.getActivityId());
                intent.putExtra("userId", userId);
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return activityList.size();
    }

    public static class ActivityViewHolder extends RecyclerView.ViewHolder {
        public TextView activityNameTextView;
        public TextView activityDescriptionTextView;

        public ActivityViewHolder(View itemView) {
            super(itemView);
            activityNameTextView = itemView.findViewById(R.id.activityNameTextView);
            activityDescriptionTextView = itemView.findViewById(R.id.activityDescriptionTextView);
        }
    }
}
