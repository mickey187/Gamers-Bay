package com.progamer.gamersbay;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.progamer.gamersbay.notification.MyNotificationManager;
import com.progamer.gamersbay.notification.SharedPrefManager;

import org.json.JSONObject;

import java.util.Iterator;
import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "Gamers";
    private LocalBroadcastManager broadcaster;

    @Override
    public void onCreate() {
        broadcaster = LocalBroadcastManager.getInstance(this);
    }
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // TODO(developer): Handle FCM messages here.
        Map<String, String> data = remoteMessage.getData();
        sendNotification(data.get("key1"),data.get("key2"));

    }
    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);
        SharedPrefManager sharedPrefManager = new SharedPrefManager(getApplicationContext());
        sharedPrefManager.storeToken(token);
    }


    private void sendNotification(String body, String title) {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("notification1", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("title", title);
        editor.putString("body", body);
        editor.putInt("count", 1);
        editor.apply();

        Intent intent = new Intent(this,MainActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("body", body);
        broadcaster.sendBroadcast(intent);

    }

}