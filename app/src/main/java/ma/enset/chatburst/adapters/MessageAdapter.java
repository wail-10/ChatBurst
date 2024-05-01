package ma.enset.chatburst.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import ma.enset.chatburst.ChatActivity;
import ma.enset.chatburst.R;
import ma.enset.chatburst.models.MessageModel;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder>{
    private static final int VIEW_TYPE_SENT = 1;
    private static final int VIEW_TYPE_RECEIVED = 2;
    private Context context;
    private List<MessageModel> messages;

    public MessageAdapter(Context context) {
        this.context = context;
        this.messages = new ArrayList<>();
    }

    public void add(MessageModel message){
        messages.add(message);
        notifyDataSetChanged();
    }

    public void clear(){
        messages.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MessageAdapter.MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == VIEW_TYPE_SENT){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_row_sent, parent, false);
            return new MessageAdapter.MessageViewHolder(view);
        }
        else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_row_received, parent, false);
            return new MessageAdapter.MessageViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.MessageViewHolder holder, int position) {
        MessageModel message = messages.get(position);
        Log.i("info", "sender ID: " + message.getSenderId());
        Log.i("info", "sender ID from Firebase: " + FirebaseAuth.getInstance().getUid());
        Log.i("info", "message: " + message.getMessage());
        Log.i("info", "Sent holder: " + holder.textViewSentMessage);
        Log.i("info", "Received holder: " + holder.textViewReceivedMessage);

        if (message.getSenderId().equals(FirebaseAuth.getInstance().getUid())){
            holder.textViewSentMessage.setText(message.getMessage());
        } else {
            holder.textViewReceivedMessage.setText(message.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public List<MessageModel> getMessages() {
        return messages;
    }

    @Override
    public int getItemViewType(int position) {
        if (messages.get(position).getSenderId().equals(FirebaseAuth.getInstance().getUid())){
            return VIEW_TYPE_SENT;
        } else {
            return VIEW_TYPE_RECEIVED;
        }
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView textViewSentMessage, textViewReceivedMessage;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewSentMessage = itemView.findViewById(R.id.sentMessage);
            textViewReceivedMessage = itemView.findViewById(R.id.receivedMessage);
        }
    }
}
