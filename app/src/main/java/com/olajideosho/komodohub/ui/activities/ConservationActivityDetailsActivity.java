package com.olajideosho.komodohub.ui.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.olajideosho.komodohub.R;
import com.olajideosho.komodohub.data.model.ConservationActivity;
import com.olajideosho.komodohub.data.model.User;
import com.olajideosho.komodohub.data.repository.ConservationActivityRepository;
import com.olajideosho.komodohub.data.repository.UserRepository;

public class ConservationActivityDetailsActivity extends AppCompatActivity {

    private ImageView backButtonImageView;
    private TextView activityNameTextView;
    private TextView activityDescriptionTextView;
    private Button editButton;
    private Button deleteButton;
    private ConservationActivityRepository conservationActivityRepository;
    private UserRepository userRepository;
    private int activityId;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_conservation_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        backButtonImageView = findViewById(R.id.backButtonImage);
        activityNameTextView = findViewById(R.id.activityNameTextView);
        activityDescriptionTextView = findViewById(R.id.activityDescriptionTextView);

        editButton = findViewById(R.id.editButton);
        deleteButton = findViewById(R.id.deleteButton);
        conservationActivityRepository = new ConservationActivityRepository(this);
        userRepository = new UserRepository(this);

        backButtonImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        activityId = getIntent().getIntExtra("activityId", -1);
        userId = getIntent().getIntExtra("userId", -1);

        if (userId != -1) {
            User user = userRepository.getUserById(userId);
            if (user != null && user.getRole().equals("student")) {
                editButton.setVisibility(View.GONE);
                deleteButton.setVisibility(View.GONE);
            }
        }

        if (activityId != -1) {
            ConservationActivity activity = conservationActivityRepository.getConservationActivityById(activityId);
            if (activity != null) {
                activityNameTextView.setText(activity.getActivityName());
                activityDescriptionTextView.setText(activity.getDescription());

                editButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        editActivityAction(activity);
                    }
                });

                deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showDeleteConfirmationDialog();
                    }
                });
            } else {
                Toast.makeText(ConservationActivityDetailsActivity.this, "Something went wrong fetching the conservation activity details", Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {
            Toast.makeText(ConservationActivityDetailsActivity.this, "Something went wrong fetching the conservation activity details", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void editActivityAction(ConservationActivity activity) {
        userId = getIntent().getIntExtra("userId", -1);
        if (userId != -1) {
            Intent intent = new Intent(this, CreateAssignClassroomActivity.class);
            intent.putExtra("activityId", activity.getActivityId());
            intent.putExtra("userId", userId);
            intent.putExtra("mode", "edit");
            startActivity(intent);
        }
    }

    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Confirm Delete");
        builder.setMessage("Are you sure you want to delete this activity?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                conservationActivityRepository.deleteConservationActivity(activityId);
                Toast.makeText(ConservationActivityDetailsActivity.this, "Activity deleted", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ConservationActivityDetailsActivity.this, TeacherDashboardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("userId", userId);
                startActivity(intent);
                finish();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}