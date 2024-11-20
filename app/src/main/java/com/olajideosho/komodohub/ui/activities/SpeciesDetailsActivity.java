package com.olajideosho.komodohub.ui.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.olajideosho.komodohub.R;
import com.olajideosho.komodohub.data.model.Species;
import com.olajideosho.komodohub.data.repository.SpeciesRepository;

public class SpeciesDetailsActivity extends AppCompatActivity {

    ImageView speciesImageView;
    TextView commonNameTextView;
    TextView scientificNameTextView;
    TextView descriptionTextView;
    TextView conservationStatusTextView;
    ImageView backButtonImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_species_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        speciesImageView = findViewById(R.id.speciesImageView);
        commonNameTextView = findViewById(R.id.commonNameTextView);
        scientificNameTextView = findViewById(R.id.scientificNameTextView);
        descriptionTextView = findViewById(R.id.descriptionTextView);
        conservationStatusTextView = findViewById(R.id.conservationStatusTextView);
        backButtonImageView = findViewById(R.id.backButtonImage);

        backButtonImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        int speciesId = getIntent().getIntExtra("speciesId", -1);
        if (speciesId != -1) {
            SpeciesRepository speciesRepository = new SpeciesRepository(this);
            Species species = speciesRepository.getSpeciesById(speciesId);

            if (species != null) {
                Glide.with(this)
                        .load(species.getImageUrl())
                        .into(speciesImageView);
                commonNameTextView.setText(species.getCommonName());
                scientificNameTextView.setText(species.getScientificName());
                descriptionTextView.setText(species.getDescription());
                conservationStatusTextView.setText(String.format("%s%s", getString(R.string.conservation_status), species.getConservationStatus()));
            } else {
                Toast.makeText(this, "Something went wrong fetching species details", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Something went wrong fetching species details", Toast.LENGTH_SHORT).show();
        }
    }
}