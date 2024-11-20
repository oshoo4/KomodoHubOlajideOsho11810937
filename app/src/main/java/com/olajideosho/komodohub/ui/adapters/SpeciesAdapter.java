package com.olajideosho.komodohub.ui.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.olajideosho.komodohub.R;
import com.olajideosho.komodohub.data.model.Species;
import com.olajideosho.komodohub.ui.activities.SpeciesDetailsActivity;

import java.util.List;

public class SpeciesAdapter extends RecyclerView.Adapter < SpeciesAdapter.SpeciesViewHolder > {

    private List<Species> speciesList;

    public SpeciesAdapter(List<Species> speciesList) {
        this.speciesList = speciesList;
    }

    @NonNull
    @Override
    public SpeciesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_species, parent, false);
        return new SpeciesViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SpeciesViewHolder holder, int position) {
        Species species = speciesList.get(position);
        holder.commonNameTextView.setText(species.getCommonName());
        holder.scientificNameTextView.setText(species.getScientificName());
        Glide.with(holder.itemView.getContext())
                .load(species.getImageUrl())
                .into(holder.speciesImageView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.itemView.getContext(), SpeciesDetailsActivity.class);
                intent.putExtra("speciesId", species.getSpeciesId());
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return speciesList.size();
    }

    public static class SpeciesViewHolder extends RecyclerView.ViewHolder {
        public TextView commonNameTextView;
        public TextView scientificNameTextView;
        public ImageView speciesImageView;

        public SpeciesViewHolder(View itemView) {
            super(itemView);
            commonNameTextView = itemView.findViewById(R.id.commonNameTextView);
            scientificNameTextView = itemView.findViewById(R.id.scientificNameTextView);
            speciesImageView = itemView.findViewById(R.id.speciesImageView);
        }
    }
}