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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.olajideosho.komodohub.R;
import com.olajideosho.komodohub.data.model.ConservationActivity;
import com.olajideosho.komodohub.data.model.User;
import com.olajideosho.komodohub.data.repository.ConservationActivityRepository;
import com.olajideosho.komodohub.data.repository.UserRepository;
import com.olajideosho.komodohub.ui.adapters.ConservationActivityAdapter;

import java.util.List;

public class TeacherConservationActivitiesActivity extends AppCompatActivity {

    RecyclerView activitiesRecyclerView;
    TextView activitiesTextView;
    ImageView backButtonImageView;
    UserRepository userRepository;
    ConservationActivityRepository conservationActivityRepository;
    List<ConservationActivity> activities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_teacher_conservation_activities);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        conservationActivityRepository = new ConservationActivityRepository(this);
        userRepository = new UserRepository(this);

        backButtonImageView = findViewById(R.id.backButtonImage);

        backButtonImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        activitiesRecyclerView = findViewById(R.id.activitiesRecyclerView);
        activitiesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        int userId = getIntent().getIntExtra("userId", -1);
        if (userId != -1) {
            User user = userRepository.getUserById(userId);
            if (user != null) {
                configureActivityBasedOnRole(user);
            } else {
                Toast.makeText(this, "Something went wrong fetching user details", Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {
            Toast.makeText(this, "Something went wrong fetching user details", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void configureActivityBasedOnRole(User user) {
        activitiesTextView = findViewById(R.id.activitiesTextView);
        if (user.getRole().equals("teacher")) {
            activities = conservationActivityRepository.getConservationActivitiesByTeacherId(user.getUserId());

            if (activities.isEmpty()) {
                activitiesTextView.setText(R.string.you_have_not_created_any_activities);
            }
        } else if (user.getRole().equals("student")) {
            int classroomId = userRepository.getClassroomByUserId(user.getUserId());
            if (classroomId != -1) {
                activities = conservationActivityRepository.getConservationActivitiesByClassroomId(classroomId);
                if (activities.isEmpty()) {
                    activitiesTextView.setText(R.string.you_have_not_been_assigned_any_activities);
                }
            } else {
                Toast.makeText(this, "Something went wrong fetching user details", Toast.LENGTH_SHORT).show();
                finish();
            }
        }

        ConservationActivityAdapter adapter = new ConservationActivityAdapter(activities, user.getUserId());
        activitiesRecyclerView.setAdapter(adapter);
    }
}