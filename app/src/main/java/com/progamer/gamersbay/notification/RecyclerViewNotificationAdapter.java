package com.progamer.gamersbay.notification;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.progamer.gamersbay.R;

import java.util.ArrayList;

public class RecyclerViewNotificationAdapter extends RecyclerView.Adapter<RecyclerViewNotificationAdapter.MyHolder>{
    private ArrayList<NotificationModel> data;
    private Context context;
    public RecyclerViewNotificationAdapter(Context context, ArrayList<NotificationModel> data) {
        this.context = context;
        this.data = data;
    }
    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_notifications,null));
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
            holder.title.setText(data.get(position).getTitle());
            holder.time.setText(data.get(position).getTime());
            holder.body.setText(data.get(position).getBody());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{
        private TextView title,body,time;
        MyHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.id_notification_title);
            time = itemView.findViewById(R.id.id_notification_time);
            body = itemView.findViewById(R.id.id_notification_body);
        }
    }
}
