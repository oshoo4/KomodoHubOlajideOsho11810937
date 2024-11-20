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
import com.olajideosho.komodohub.data.model.Message;
import com.olajideosho.komodohub.data.model.User;
import com.olajideosho.komodohub.data.repository.ClassroomRepository;
import com.olajideosho.komodohub.data.repository.MessageRepository;
import com.olajideosho.komodohub.data.repository.UserRepository;
import com.olajideosho.komodohub.ui.adapters.MessageAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewMessagesActivity extends AppCompatActivity {

    private RecyclerView messagesRecyclerView;
    private MessageRepository messageRepository;
    private UserRepository userRepository;
    private ClassroomRepository classroomRepository;
    private ImageView backButtonImageView;
    private TextView messagesTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_messages);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        messagesTextView = findViewById(R.id.messagesTextView);
        messagesRecyclerView = findViewById(R.id.messagesRecyclerView);
        messagesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        messageRepository = new MessageRepository(this);
        userRepository = new UserRepository(this);
        classroomRepository = new ClassroomRepository(this);

        backButtonImageView = findViewById(R.id.backButtonImage);

        backButtonImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        fetchAndDisplayMessages();
    }

    private void fetchAndDisplayMessages() {
        int userId = getIntent().getIntExtra("userId", -1);
        if (userId == -1) {
            Toast.makeText(this, "Something went wrong fetching user details", Toast.LENGTH_SHORT).show();
            return;
        }

        User user = userRepository.getUserById(userId);
        if (user == null) {
            Toast.makeText(this, "Something went wrong fetching user details", Toast.LENGTH_SHORT).show();
            return;
        }
        String userRole = user.getRole();

        if (userRole != null) {
            List<Message> messages;
            if (userRole.equals("student")) {
                messages = fetchStudentMessages(userId);
            } else if (userRole.equals("teacher")) {
                messages = fetchTeacherMessages(userId);
            } else {
                messages = new ArrayList<>();
                Toast.makeText(this, "Invalid user role", Toast.LENGTH_SHORT).show();
            }

            if (messages.isEmpty()) {
                messagesTextView.setText(R.string.you_currently_don_t_have_any_messages);
            } else {
                String messageCountText = messages.size() + (messages.size() == 1 ? " Message" : " Messages");
                messagesTextView.setText(messageCountText);

                MessageAdapter adapter = new MessageAdapter(messages, userRepository);
                messagesRecyclerView.setAdapter(adapter);
            }
        } else {
            Toast.makeText(this, "Something went wrong fetching user details", Toast.LENGTH_SHORT).show();
        }
    }

    private List<Message> fetchStudentMessages(int studentId) {
        int classroomId = userRepository.getClassroomByUserId(studentId);
        if (classroomId == -1) {
            return new ArrayList<>();
        }

        int teacherId = classroomRepository.getClassroomById(classroomId).getTeacherId();
        return messageRepository.getMessagesForClassroomFromSender(classroomId, teacherId);
    }

    private List<Message> fetchTeacherMessages(int teacherId) {
        int classroomId = classroomRepository.getClassroomByTeacherId(teacherId).getClassroomId();

        List<Integer> studentIds = userRepository.getStudentIdsByClassroomId(classroomId);

        return messageRepository.getMessagesForClassroomFromSenders(classroomId, studentIds);
    }
}