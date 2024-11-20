package com.olajideosho.komodohub.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.olajideosho.komodohub.R;
import com.olajideosho.komodohub.data.model.User;
import com.olajideosho.komodohub.data.repository.UserRepository;

public class StudentDashboardActivity extends AppCompatActivity {

    private Button logoutButton;
    private TextView welcomeTextView;
    private Button viewSpeciesInfoButton;
    private Button contributeContentButton;
    private Button viewActivitiesButton;
    private Button viewMessagesButton;
    private Button sendMessageButton;
    private UserRepository userRepository;
    int userId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_student_dashboard);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        logoutButton = findViewById(R.id.logoutButton);
        welcomeTextView = findViewById(R.id.welcomeTextView);
        viewSpeciesInfoButton = findViewById(R.id.viewSpeciesInfoButton);
        contributeContentButton = findViewById(R.id.contributeContentButton);
        viewActivitiesButton = findViewById(R.id.viewActivitiesButton);
        viewMessagesButton = findViewById(R.id.viewMessagesButton);
        sendMessageButton = findViewById(R.id.sendMessageButton);
        userRepository = new UserRepository(this);

        setupWelcomeText();

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        viewSpeciesInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToSpecies();
            }
        });

        contributeContentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToContributeContent();
            }
        });

        viewActivitiesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToViewActivities();
            }
        });

        viewMessagesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToViewMessages();
            }
        });

        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToSendMessage();
            }
        });
    }

    private void goToViewMessages() {
        Intent intent = new Intent(this, ViewMessagesActivity.class);
        intent.putExtra("userId", userId);
        startActivity(intent);
    }

    private void goToSendMessage() {
        Intent intent = new Intent(this, SendMessageActivity.class);
        intent.putExtra("userId", userId);
        startActivity(intent);
    }

    private void goToContributeContent() {
        Intent intent = new Intent(this, ContributeContentActivity.class);
        intent.putExtra("userId", userId);
        startActivity(intent);
    }

    private void goToViewActivities() {
        Intent intent = new Intent(this, TeacherConservationActivitiesActivity.class);
        intent.putExtra("userId", userId);
        startActivity(intent);
    }

    private void setupWelcomeText() {
        userId = getIntent().getIntExtra("userId", -1);
        if (userId != -1) {
            User user = userRepository.getUserById(userId);
            if (user != null) {
                String firstName = user.getFirstName();
                String capitalizedFirstName = firstName.substring(0, 1).toUpperCase() + firstName.substring(1);

                String role = user.getRole();
                String capitalizedRole = role.substring(0, 1).toUpperCase() + role.substring(1);

                String welcomeMessage = String.format("Welcome, %s (%s)", capitalizedFirstName, capitalizedRole);
                welcomeTextView.setText(welcomeMessage);
            } else {
                Toast.makeText(this, "Something went wrong fetching user details", Toast.LENGTH_SHORT).show();
                logout();
            }
        } else {
            Toast.makeText(this, "Something went wrong fetching user details", Toast.LENGTH_SHORT).show();
            logout();
        }
    }

    private void goToSpecies() {
        Intent intent = new Intent(this, SpeciesListActivity.class);
        startActivity(intent);
    }

    private void logout() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }
}