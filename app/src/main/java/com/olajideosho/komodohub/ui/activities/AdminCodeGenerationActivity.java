package com.olajideosho.komodohub.ui.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.olajideosho.komodohub.R;
import com.olajideosho.komodohub.data.model.Classroom;
import com.olajideosho.komodohub.data.repository.ClassroomRepository;
import com.olajideosho.komodohub.utils.AccessCodeGenerator;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AdminCodeGenerationActivity extends AppCompatActivity {

    private TextView accessCodeTextView;
    private EditText firstNameEditText;
    private TextView dateOfBirthTextView;
    private Button generateButton;
    private Button logoutButton;
    private Calendar selectedDate;

    private Spinner roleSpinner;
    private Spinner classroomSpinner;
    private ClassroomRepository classroomRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_code_generation);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        accessCodeTextView = findViewById(R.id.accessCodeTextView);
        firstNameEditText = findViewById(R.id.firstNameEditText);
        dateOfBirthTextView = findViewById(R.id.dateOfBirthTextView);
        generateButton = findViewById(R.id.generateButton);
        logoutButton = findViewById(R.id.logoutButton);
        selectedDate = Calendar.getInstance();
        roleSpinner = findViewById(R.id.roleSpinner);
        classroomSpinner = findViewById(R.id.classroomSpinner);

        classroomRepository = new ClassroomRepository(this);

        dateOfBirthTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();

            }
        });

        generateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateCode();
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCodeGenerationActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });

        ArrayAdapter<CharSequence> roleAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.roles_array,
                android.R.layout.simple_spinner_item);
        roleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roleSpinner.setAdapter(roleAdapter);

        List<Classroom> classrooms = classroomRepository.getAllClassrooms();
        List<String> classroomNames = new ArrayList<>();
        for (Classroom classroom : classrooms) {
            classroomNames.add(classroom.getClassroomName());
        }
        ArrayAdapter<String> classroomAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                classroomNames
        );
        classroomAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        classroomSpinner.setAdapter(classroomAdapter);
    }

    private void showDatePickerDialog() {
        int year = selectedDate.get(Calendar.YEAR);
        int month = selectedDate.get(Calendar.MONTH);
        int day = selectedDate.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        selectedDate.set(Calendar.YEAR,
                                year);
                        selectedDate.set(Calendar.MONTH, month);
                        selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        updateDateOfBirthTextView();
                    }
                },
                year, month, day);
        datePickerDialog.show();
    }

    private void updateDateOfBirthTextView() {
        int year = selectedDate.get(Calendar.YEAR);
        int month = selectedDate.get(Calendar.MONTH) + 1;
        int day = selectedDate.get(Calendar.DAY_OF_MONTH);
        String dateOfBirthString = String.format("%02d/%02d/%04d", day, month, year);
        dateOfBirthTextView.setText(dateOfBirthString);
    }

    private void generateCode() {
        String firstName = firstNameEditText.getText().toString().toLowerCase();
        String dateOfBirthString = dateOfBirthTextView.getText().toString();
        String role = roleSpinner.getSelectedItem().toString();
        String classroomName = classroomSpinner.getSelectedItem().toString();

        String accessCode = AccessCodeGenerator.generateAccessCode(firstName, dateOfBirthString, role, classroomName);

        accessCodeTextView.setText(String.format("%s%s", getString(R.string.access_code), accessCode));
    }
}