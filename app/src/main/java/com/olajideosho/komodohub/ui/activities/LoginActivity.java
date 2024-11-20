package com.olajideosho.komodohub.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.olajideosho.komodohub.R;
import com.olajideosho.komodohub.data.model.User;
import com.olajideosho.komodohub.data.repository.UserRepository;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    EditText emailEditText;
    EditText passwordEditText;
    Button loginButton;
    ImageView backButtonImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        backButtonImageView = findViewById(R.id.backButtonImage);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

        backButtonImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void loginUser() {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        UserRepository userRepository = new UserRepository(LoginActivity.this);
        User user = userRepository.getUserByEmail(email);

        if (user != null && userRepository.checkPassword(password, user.getPassword())) {
            Toast.makeText(LoginActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();
            handleNavigationByRoleOf(user);
        } else {
            Toast.makeText(LoginActivity.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
        }
    }

    private void handleNavigationByRoleOf(User user) {
        if (Objects.equals(user.getRole(), "admin")) {
            Intent intent = new Intent(this, AdminCodeGenerationActivity.class);
            startActivity(intent);
        } else if (Objects.equals(user.getRole(), "teacher")) {
            Intent intent = new Intent(this, TeacherDashboardActivity.class);
            intent.putExtra("userId", user.getUserId());
            startActivity(intent);
        } else if (Objects.equals(user.getRole(), "student")) {
            Intent intent = new Intent(this, StudentDashboardActivity.class);
            intent.putExtra("userId", user.getUserId());
            startActivity(intent);
        } else {
            Toast.makeText(LoginActivity.this, "Sorry. We can't identify what type of user you are.", Toast.LENGTH_SHORT).show();
        }
    }
}