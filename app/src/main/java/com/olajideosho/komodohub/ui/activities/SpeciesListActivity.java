package com.olajideosho.komodohub.ui.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.olajideosho.komodohub.R;
import com.olajideosho.komodohub.data.model.Species;
import com.olajideosho.komodohub.data.repository.SpeciesRepository;
import com.olajideosho.komodohub.ui.adapters.SpeciesAdapter;

import java.util.List;

public class SpeciesListActivity extends AppCompatActivity {

    RecyclerView speciesRecyclerView;
    ImageView backButtonImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_species_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        speciesRecyclerView = findViewById(R.id.speciesRecyclerView);
        backButtonImageView = findViewById(R.id.backButtonImage);

        backButtonImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        speciesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        SpeciesRepository speciesRepository = new SpeciesRepository(this);
        List<Species> speciesList = speciesRepository.getAllSpecies();

        SpeciesAdapter adapter = new SpeciesAdapter(speciesList);
        speciesRecyclerView.setAdapter(adapter);
    }
}