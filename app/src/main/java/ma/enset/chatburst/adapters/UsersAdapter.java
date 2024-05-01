package ma.enset.chatburst.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ma.enset.chatburst.ChatActivity;
import ma.enset.chatburst.MainActivity;
import ma.enset.chatburst.R;
import ma.enset.chatburst.models.UserModel;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder>{
    private Context context;
    private List<UserModel> users;

    public UsersAdapter(Context context) {
        this.context = context;
        this.users = new ArrayList<>();
    }

    public void add(UserModel user){
        users.add(user);
    }

    public void clear(){
        users.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UsersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UsersAdapter.ViewHolder holder, int position) {
        UserModel user = users.get(position);
        holder.name.setText(user.getName());
        holder.email.setText(user.getEmail());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ChatActivity.class);
            intent.putExtra("id", user.getId());
            intent.putExtra("name", user.getName());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public List<UserModel> getUsers() {
        return users;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView name, email;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.user_name);
            email = itemView.findViewById(R.id.user_email);
        }
    }
}
