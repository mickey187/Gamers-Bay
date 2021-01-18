package com.progamer.gamersbay.notification;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.progamer.gamersbay.R;

import java.util.HashMap;
import java.util.Map;


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
                if (!model.isNotify()){
                    holder.read.setText("new");
                    holder.read.setBackgroundResource(R.color.pink);
                    holder.backgroundColor.setBackgroundResource(R.color.grayLighter);
                }else {
                    holder.read.setText("read");
                    holder.read.setBackgroundResource(R.color.purple_200);
                    holder.backgroundColor.setBackgroundResource(R.color.white);

                }
                if (model.isNotify()) holder.badgeLayout.setVisibility(View.VISIBLE);
                else holder.badgeLayout.setVisibility(View.GONE);
                holder.backgroundColor.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Map<String, Object> map = new HashMap<>();
                        map.put("notify",true);
                        getSnapshots().getSnapshot(position).getReference().update(map);
                        holder.backgroundColor.setBackgroundResource(R.color.white);
                    }
                });
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
        private TextView title,body,time,read;
        private CardView badgeLayout;
        private RelativeLayout backgroundColor;
        MyHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.id_notification_title);
            body = itemView.findViewById(R.id.id_notification_body);
            time = itemView.findViewById(R.id.id_notification_time);
            read = itemView.findViewById(R.id.badge);
            badgeLayout = itemView.findViewById(R.id.card);
            backgroundColor = itemView.findViewById(R.id.item);
        }
    }
}
