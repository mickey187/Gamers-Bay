package com.progamer.gamersbay;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.progamer.gamersbay.notification.MyNotificationManager;
import com.progamer.gamersbay.notification.SharedPrefManager;

import org.json.JSONObject;

import java.util.Calendar;
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
        sendNotificationBackground(data.get("key1"),data.get("key2"));
        Intent intent = new Intent(this, MainActivity.class);
        sendBroadcast(intent);

    }
    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);
        SharedPrefManager sharedPrefManager = new SharedPrefManager(getApplicationContext());
        sharedPrefManager.storeToken(token);
    }


    private void sendNotification(String body, String title) {
        int hour,minute;
        String time = "";
        hour = Calendar.getInstance().getTime().getHours();
        minute = Calendar.getInstance().getTime().getMinutes();
        if (hour > 12){
            time = hour +":"+ minute+" PM";
        }else
            time = hour +":"+ minute+" AM";
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("notification1", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        int count = sharedPreferences.getInt("count", 0);

        if (count == 1){
            editor.putString("title2", title);
            editor.putString("body2", body);
            editor.putString("time2", time);
            editor.putInt("count", 2);
        }else{
            editor.putString("title", title);
            editor.putString("body", body);
            editor.putString("time", time);
            editor.putInt("count", 1);
        }
        editor.apply();
    }
    private void sendNotificationBackground(String messageBody, String title) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String channelId = "FCMID";
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.ic_baseline_notifications_24)
                        .setContentTitle(title)
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}