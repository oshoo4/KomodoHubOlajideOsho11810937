package com.olajideosho.komodohub.ui.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.olajideosho.komodohub.R;
import com.olajideosho.komodohub.data.model.Species;
import com.olajideosho.komodohub.data.model.User;
import com.olajideosho.komodohub.data.repository.SpeciesRepository;
import com.olajideosho.komodohub.data.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

public class ContributeContentActivity extends AppCompatActivity {

    private ImageView backButtonImageView;
    private Spinner speciesSpinner;
    private TextView commonNameTextView;
    private TextView scientificNameTextView;
    private EditText descriptionEditText;
    private TextView conservationStatusTextView;
    private Button contributeButton;
    private SpeciesRepository speciesRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_contribute_content);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        backButtonImageView = findViewById(R.id.backButtonImage);
        speciesSpinner = findViewById(R.id.speciesSpinner);
        commonNameTextView = findViewById(R.id.commonNameTextView);
        scientificNameTextView = findViewById(R.id.scientificNameTextView);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        conservationStatusTextView = findViewById(R.id.conservationStatusTextView);
        contributeButton = findViewById(R.id.contributeButton);
        speciesRepository = new SpeciesRepository(this);

        backButtonImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        List<Species> speciesList = speciesRepository.getAllSpecies();
        List<String> speciesNames = new ArrayList<>();
        for (Species species : speciesList) {
            speciesNames.add(species.getCommonName());
        }
        ArrayAdapter<String> speciesAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                speciesNames);
        speciesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        speciesSpinner.setAdapter(speciesAdapter);
        speciesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateSpeciesInfo();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        contributeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contributeContent();
            }
        });
    }

    private void updateSpeciesInfo() {
        String selectedSpeciesName = speciesSpinner.getSelectedItem().toString();
        Species species = speciesRepository.getSpeciesByCommonName(selectedSpeciesName);
        if (species != null) {
            commonNameTextView.setText(species.getCommonName());
            scientificNameTextView.setText(species.getScientificName());
            conservationStatusTextView.setText(species.getConservationStatus());
        }
    }

    private void contributeContent() {
        String commonName = commonNameTextView.getText().toString().trim();
        String scientificName = scientificNameTextView.getText().toString().trim();
        String description = descriptionEditText.getText().toString().trim();
        String conservationStatus = conservationStatusTextView.getText().toString().trim();

        if (description.isEmpty()) {
            Toast.makeText(this, "Please fill in some content in the description field", Toast.LENGTH_SHORT).show();
            return;
        }

        String selectedSpeciesName = speciesSpinner.getSelectedItem().toString();
        Species species = speciesRepository.getSpeciesByCommonName(selectedSpeciesName);

        if (species == null) {
            Toast.makeText(this, "Species not found", Toast.LENGTH_SHORT).show();
            return;
        }

        species.setCommonName(commonName);
        species.setScientificName(scientificName);

        int userId = getIntent().getIntExtra("userId", -1);
        if (userId == -1) {
            Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show();
            return;
        }

        UserRepository userRepository = new UserRepository(this);
        User user = userRepository.getUserById(userId);
        if (user == null) {
            Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show();
            return;
        }
        String studentName = user.getFirstName() + " " + user.getLastName();

        String currentDescription = species.getDescription();
        String newDescription = currentDescription + "\n\nContributed by: " + studentName + "\n" + description;
        species.setDescription(newDescription);

        species.setConservationStatus(conservationStatus);
        species.setImageUrl(species.getImageUrl());

        int rowsAffected = speciesRepository.updateSpecies(species);

        if (rowsAffected > 0) {
            Toast.makeText(this, "Content contributed successfully!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Failed to contribute content", Toast.LENGTH_SHORT).show();
        }
    }
}