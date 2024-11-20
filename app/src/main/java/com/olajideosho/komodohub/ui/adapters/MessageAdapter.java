package com.olajideosho.komodohub.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.olajideosho.komodohub.R;
import com.olajideosho.komodohub.data.model.Message;
import com.olajideosho.komodohub.data.model.User;
import com.olajideosho.komodohub.data.repository.UserRepository;

import org.threeten.bp.format.DateTimeFormatter;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private List<Message> messageList;
    private UserRepository userRepository;

    public MessageAdapter(List<Message> messageList, UserRepository userRepository) {
        this.messageList = messageList;
        this.userRepository = userRepository;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_message, parent, false);
        return new MessageViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        Message message = messageList.get(position);

        User sender = userRepository.getUserById(message.getSenderId());
        String senderDescription = (sender != null) ? sender.getFirstName() + " " + sender.getLastName() : "Unknown Sender";
        String senderRole = (sender != null) ? sender.getRole() : "Invalid Role";
        senderDescription = String.format("%s (%s)",senderDescription, senderRole);

        String formattedMessage = String.format("%s\n%s\n%s",
                senderDescription,
                message.getContent(),
                message.getTimestamp().format(DateTimeFormatter.ofPattern("HH:mm, dd/MM/yyyy")));

        holder.receivedMessageTextView.setText(formattedMessage);
        holder.receivedMessageTextView.setVisibility(View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        public TextView receivedMessageTextView;

        public MessageViewHolder(View itemView) {
            super(itemView);
            receivedMessageTextView = itemView.findViewById(R.id.receivedMessageTextView);
        }
    }
}
