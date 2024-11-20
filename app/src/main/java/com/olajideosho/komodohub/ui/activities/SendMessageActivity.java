package com.olajideosho.komodohub.ui.activities;

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
import com.olajideosho.komodohub.data.model.Message;
import com.olajideosho.komodohub.data.model.User;
import com.olajideosho.komodohub.data.repository.ClassroomRepository;
import com.olajideosho.komodohub.data.repository.MessageRepository;
import com.olajideosho.komodohub.data.repository.UserRepository;

import org.threeten.bp.LocalDateTime;

public class SendMessageActivity extends AppCompatActivity {

    private ImageView backButtonImageView;
    private EditText messageEditText;
    private Button sendButton;
    private MessageRepository messageRepository;
    private UserRepository userRepository;
    private ClassroomRepository classroomRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_send_message);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        backButtonImageView = findViewById(R.id.backButtonImage);
        messageEditText = findViewById(R.id.messageEditText);
        sendButton = findViewById(R.id.sendButton);

        messageRepository = new MessageRepository(this);
        userRepository = new UserRepository(this);
        classroomRepository = new ClassroomRepository(this);

        backButtonImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
    }

    private void sendMessage() {
        String messageContent = messageEditText.getText().toString().trim();

        if (messageContent.isEmpty()) {
            Toast.makeText(this, "Please enter a message", Toast.LENGTH_SHORT).show();
            return;
        }

        int senderId = getIntent().getIntExtra("userId", -1);
        if (senderId == -1) {
            Toast.makeText(this, "Sender ID not found", Toast.LENGTH_SHORT).show();
            return;
        }

        User sender = userRepository.getUserById(senderId);
        if (sender == null) {
            Toast.makeText(this, "Sender role not found", Toast.LENGTH_SHORT).show();
            return;
        }
        String senderRole = sender.getRole();

        Integer receiverId = null;
        Integer classroomId = null;

        if (senderRole.equals("student")) {
            classroomId = userRepository.getClassroomByUserId(senderId);
            if (classroomId == -1) {
                Toast.makeText(this, "Classroom not found", Toast.LENGTH_SHORT).show();
                return;
            }
            receiverId = classroomRepository.getClassroomById(classroomId).getTeacherId();
        } else if (senderRole.equals("teacher")) {
            classroomId = classroomRepository.getClassroomByTeacherId(senderId).getClassroomId();
        } else {
            Toast.makeText(this, "Invalid sender role", Toast.LENGTH_SHORT).show();
            return;
        }

        Message message = new Message(
                0,
                senderId,
                receiverId,
                classroomId,
                messageContent,
                LocalDateTime.now()
        );

        long messageId = messageRepository.insertMessage(message);

        if (messageId != -1) {
            Toast.makeText(this, "Message sent successfully!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Failed to send message", Toast.LENGTH_SHORT).show();
        }
    }
}