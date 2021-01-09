package com.progamer.gamersbay.notification;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.progamer.gamersbay.R;
import com.progamer.gamersbay.db.DbHelper;
import java.util.ArrayList;

public class NotificationsFragment extends Fragment {
    public static final String TAG = "TEST";
    private RecyclerView recycle_notifications;
    private RecyclerViewNotificationAdapter adapter;
    private ArrayList<NotificationModel> data;
    private DbHelper db;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);
        data = new ArrayList<>();
        System.out.println("TAG: "+FirebaseInstanceId.getInstance().getToken());
        checkNotification(view);
        db = new DbHelper(getContext());
            recycle_notifications = view.findViewById(R.id.notification_recyclerview_id);
            recycle_notifications.setLayoutManager(new LinearLayoutManager(getActivity()));
            adapter  = new RecyclerViewNotificationAdapter(getActivity(), db.getAllData());
            recycle_notifications.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        db.close();

        return view;
    }
    public void checkNotification(View view){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("notification1", Context.MODE_PRIVATE);
        String title1,body1,time,title2,body2,time2;
        int count;
        title1 = sharedPreferences.getString("title",null);
        body1 = sharedPreferences.getString("body",null);
        count = sharedPreferences.getInt("count", 0);
        time = sharedPreferences.getString("time", null);
        Log.d(TAG, "checkNotification: "+ count);
        Log.d(TAG, "checkNotification: "+ title1);
        Log.d(TAG, "checkNotification: "+ body1);
        Log.d(TAG, "checkNotification: "+ time);
        if (count == 1){
            db = new DbHelper(getContext());
            db.insertData(title1,time,body1);
                recycle_notifications = view.findViewById(R.id.notification_recyclerview_id);
                recycle_notifications.setLayoutManager(new LinearLayoutManager(getActivity()));
                adapter  = new RecyclerViewNotificationAdapter(getActivity(), db.getAllData());
                recycle_notifications.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            db.close();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("count", 0);
            editor.apply();
        }else if (count == 2){
            title2 = sharedPreferences.getString("title2",null);
            body2 = sharedPreferences.getString("body2",null);
            time2 = sharedPreferences.getString("time2", null);

            title1 = sharedPreferences.getString("title",null);
            body1 = sharedPreferences.getString("body",null);
            time = sharedPreferences.getString("time", null);
            db = new DbHelper(getContext());
            db.insertData(title1,time,body1);
            db.insertData(title2,time2,body2);
            db.close();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("count", 0);
            editor.apply();
        }
    }
}