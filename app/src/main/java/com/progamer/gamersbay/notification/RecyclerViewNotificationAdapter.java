package com.progamer.gamersbay.notification;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.progamer.gamersbay.R;


public class RecyclerViewNotificationAdapter extends FirestoreRecyclerAdapter<NotificationModel,RecyclerViewNotificationAdapter.MyHolder> {

    public RecyclerViewNotificationAdapter(@NonNull FirestoreRecyclerOptions<NotificationModel> options) {
        super(options);
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_notifications,parent,false);
        return new MyHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull MyHolder holder, int position, @NonNull NotificationModel model) {
                holder.title.setText(model.getTitle());
                holder.body.setText(model.getDescription());
                holder.time.setText(model.getNotificationTimeStamp());
    }

    public void deleteItem(int position){
        getSnapshots().getSnapshot(position).getReference().delete();
    }

    @NonNull
    @Override
    public NotificationModel getItem(int position) {
        return getSnapshots().get(position);
    }

    @Override
    public int getItemCount() {
        return getSnapshots().isListening(this) ? getSnapshots().size():0;
    }

    class MyHolder extends RecyclerView.ViewHolder{
        private TextView title,body,time;
        MyHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.id_notification_title);
            body = itemView.findViewById(R.id.id_notification_body);
            time = itemView.findViewById(R.id.id_notification_time);
        }
    }
}
