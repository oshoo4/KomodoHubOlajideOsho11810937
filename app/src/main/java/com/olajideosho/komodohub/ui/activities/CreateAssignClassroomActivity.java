package com.olajideosho.komodohub.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
import com.olajideosho.komodohub.data.model.Classroom;
import com.olajideosho.komodohub.data.model.ConservationActivity;
import com.olajideosho.komodohub.data.repository.ClassroomRepository;
import com.olajideosho.komodohub.data.repository.ConservationActivityRepository;

import java.util.ArrayList;
import java.util.List;


public class CreateAssignClassroomActivity extends AppCompatActivity {

    private TextView createActivityTextView;
    private ImageView backButtonImageView;
    private EditText activityNameEditText;
    private EditText activityDescriptionEditText;
    private Spinner classroomSpinner;
    private Button createAssignButton;
    ArrayAdapter<String> classroomAdapter;
    private ClassroomRepository classroomRepository;
    private ConservationActivityRepository conservationActivityRepository;
    int activityId;
    int teacherId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_assign_classroom);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        createActivityTextView = findViewById(R.id.createActivityTextView);
        backButtonImageView = findViewById(R.id.backButtonImage);
        activityNameEditText = findViewById(R.id.activityNameEditText);
        activityDescriptionEditText = findViewById(R.id.activityDescriptionEditText);

        classroomSpinner = findViewById(R.id.classroomSpinner);
        createAssignButton = findViewById(R.id.createAssignButton);
        classroomRepository = new ClassroomRepository(this);

        backButtonImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        teacherId = getIntent().getIntExtra("userId", -1);

        List<Classroom> classrooms = classroomRepository.getAllClassrooms();
        List<String> classroomNames = new ArrayList<>();
        for (Classroom classroom : classrooms) {
            classroomNames.add(classroom.getClassroomName());
        }
        classroomAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                classroomNames);
        classroomAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        classroomSpinner.setAdapter(classroomAdapter);

        activityId = getIntent().getIntExtra("activityId", -1);
        String mode = getIntent().getStringExtra("mode");
        if (activityId != -1 && "edit".equals(mode)) {
            configureEdit();
        } else {
            configureCreate();
        }
    }

    private void configureEdit() {
        createActivityTextView.setText("Edit Activity");

        conservationActivityRepository = new ConservationActivityRepository(this);
        ConservationActivity activity = conservationActivityRepository.getConservationActivityById(activityId);
        if (activity != null) {
            activityNameEditText.setText(activity.getActivityName());
            activityDescriptionEditText.setText(activity.getDescription());

            int classroomId = conservationActivityRepository.getClassroomIdForActivity(activityId);
            if (classroomId != -1) {
                String classroomName = classroomRepository.getClassroomById(classroomId).getClassroomName();
                int spinnerPosition = classroomAdapter.getPosition(classroomName);
                classroomSpinner.setSelection(spinnerPosition);
            }
        }

        createAssignButton.setText("Save Changes");

        createAssignButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveChanges(activityId);
            }
        });
    }

    private void configureCreate() {
        createAssignButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAndAssignActivity();
            }
        });
    }

    private void saveChanges(int activityId) {
        String activityName = activityNameEditText.getText().toString();
        String activityDescription = activityDescriptionEditText.getText().toString();
        String classroomName = classroomSpinner.getSelectedItem().toString();

        if (activityName.isEmpty() || activityDescription.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        int teacherId = getIntent().getIntExtra("userId", -1);
        if (teacherId == -1) {
            Toast.makeText(this, "Teacher ID not found", Toast.LENGTH_SHORT).show();
            return;
        }

        ConservationActivity updatedActivity = new ConservationActivity(
                activityId,
                activityName,
                activityDescription,
                teacherId
        );
        conservationActivityRepository.updateConservationActivity(updatedActivity);

        Classroom newClassroom = classroomRepository.getClassroomByName(classroomName);
        if (newClassroom != null) {
            conservationActivityRepository.deleteActivityFromClassroom(activityId);
            conservationActivityRepository.addActivityToClassroom(activityId, newClassroom.getClassroomId());
        }

        Toast.makeText(this, "Activity updated!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, TeacherDashboardActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("userId", teacherId);
        startActivity(intent);
        finish();
    }

    private void createAndAssignActivity() {
        String activityName = activityNameEditText.getText().toString();
        String activityDescription = activityDescriptionEditText.getText().toString();
        String classroomName = classroomSpinner.getSelectedItem().toString();

        if (activityName.isEmpty() || activityDescription.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        int teacherId = getIntent().getIntExtra("userId", -1);
        if (teacherId == -1) {
            Toast.makeText(this, "Teacher ID not found", Toast.LENGTH_SHORT).show();
            return;
        }

        ConservationActivity conservationActivity = new ConservationActivity(
                0,
                activityName,
                activityDescription,
                teacherId
        );

        ConservationActivityRepository conservationActivityRepository = new ConservationActivityRepository(this);
        long activityId = conservationActivityRepository.insertConservationActivity(conservationActivity);

        if (activityId != -1) {
            ClassroomRepository classroomRepository = new ClassroomRepository(this);
            Classroom classroom = classroomRepository.getClassroomByName(classroomName);
            if (classroom != null) {
                conservationActivityRepository.addActivityToClassroom(activityId, classroom.getClassroomId());
            }

            Toast.makeText(this, "Activity created and assigned!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Failed to create activity", Toast.LENGTH_SHORT).show();
        }
    }
}