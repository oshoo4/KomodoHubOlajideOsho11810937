package com.olajideosho.komodohub.ui.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
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
import com.olajideosho.komodohub.data.model.User;
import com.olajideosho.komodohub.data.repository.ClassroomRepository;
import com.olajideosho.komodohub.data.repository.UserRepository;
import com.olajideosho.komodohub.utils.AccessCodeGenerator;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class RegisterActivity extends AppCompatActivity {

    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private TextView dateOfBirthTextView;
    private EditText accessCodeEditText;
    private Button registerButton;
    private Spinner roleSpinner;
    private Spinner classroomSpinner;
    private ClassroomRepository classroomRepository;
    private Calendar selectedDate;

    ImageView backButtonImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        firstNameEditText = findViewById(R.id.firstNameEditText);
        lastNameEditText = findViewById(R.id.lastNameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        accessCodeEditText = findViewById(R.id.accessCodeEditText);
        roleSpinner = findViewById(R.id.roleSpinner);
        classroomSpinner = findViewById(R.id.classroomSpinner);
        registerButton = findViewById(R.id.registerButton);
        dateOfBirthTextView = findViewById(R.id.dateOfBirthTextView);
        backButtonImageView = findViewById(R.id.backButtonImage);

        selectedDate = Calendar.getInstance();

        classroomRepository = new ClassroomRepository(this);

        backButtonImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        dateOfBirthTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();

            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
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
                        selectedDate.set(Calendar.YEAR, year);
                        selectedDate.set(Calendar.MONTH, month);
                        selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        updateDateOfBirthTextView();
                    }
                },
                year, month, day
        );
        datePickerDialog.show();
    }

    private void updateDateOfBirthTextView() {
        int year = selectedDate.get(Calendar.YEAR);
        int month = selectedDate.get(Calendar.MONTH) + 1;
        int day = selectedDate.get(Calendar.DAY_OF_MONTH);
        String dateOfBirthString = String.format(Locale.getDefault(), "%02d/%02d/%04d", day, month, year);
        dateOfBirthTextView.setText(dateOfBirthString);
    }

    private void registerUser() {
        String firstName = firstNameEditText.getText().toString();
        String lastName = lastNameEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String dateOfBirthString = dateOfBirthTextView.getText().toString();
        String accessCode = accessCodeEditText.getText().toString();
        String role = roleSpinner.getSelectedItem().toString();
        String classroomName = classroomSpinner.getSelectedItem().toString();

        if (
                firstName.isEmpty()
                || lastName.isEmpty()
                || email.isEmpty()
                || password.isEmpty()
                || accessCode.isEmpty()
        ) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!AccessCodeGenerator.verifyAccessCode(accessCode, firstName, dateOfBirthString, role, classroomName)) {
            Toast.makeText(this, "Invalid access code", Toast.LENGTH_SHORT).show();
            return;
        }

        UserRepository userRepository = new UserRepository(this);
        User existingUser = userRepository.getUserByEmail(email);

        if (existingUser == null) {
            ClassroomRepository classroomRepository = new ClassroomRepository(this);
            Classroom classroom = classroomRepository.getClassroomByName(classroomName);

            if (classroom != null) {
                User user = new User(0, firstName, lastName, email, password, role);
                userRepository.insertUser(user);

                var userForClassroom = userRepository.getUserByEmail(email);

                if (userForClassroom.getUserId() != -1) {
                    userRepository.addUserToClassroom(userForClassroom.getUserId(), classroom.getClassroomId());
                    Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, "Something went wrong, please try again with another email", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Classroom does not exist", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "User with email already exists", Toast.LENGTH_SHORT).show();
        }
    }
}