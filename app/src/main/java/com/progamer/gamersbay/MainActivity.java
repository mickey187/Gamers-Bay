package com.progamer.gamersbay;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.iid.FirebaseInstanceId;
import com.progamer.gamersbay.notification.NotificationModel;
import com.progamer.gamersbay.notification.NotificationsFragment;

import java.util.HashMap;
import java.util.Map;

import io.grpc.Context;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class MainActivity extends AppCompatActivity implements DialogClass.DialogClassListener, SharedPreferences.OnSharedPreferenceChangeListener {

    private BottomNavigationView bottomNavigationView;
    private FrameLayout frameLayout;
    private String title,body,time;
    private HomeFragment homeFragment;
    private AccountFragment accountFragment;
    private SettingsFragment settingsFragment;
    private NotificationsFragment notificationsFragment;
    private LeaderboardFragment leaderboardFragment;
    private SettingFragment settingFragment;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference notificationRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setUpSharedPreferences();
        setContentView(R.layout.activity_main);

        Log.d("firebaseGo", "token: "+FirebaseInstanceId.getInstance().getToken());

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String userID = user.getUid();

        notificationRef =  db.collection("NotificationTest").document(userID).collection("notification");
        bottomNavigationView = findViewById(R.id.bottom_nav);
        frameLayout = findViewById(R.id.main_framelayout);
        bottomNavigationView.getMenu().findItem(R.id.nav_home).setChecked(true);
        homeFragment = new HomeFragment();
        accountFragment = new AccountFragment();
        settingsFragment = new SettingsFragment();
        notificationsFragment = new NotificationsFragment();
        leaderboardFragment = new LeaderboardFragment();
        settingFragment = new SettingFragment();
        setFragment(homeFragment);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){

                    case R.id.nav_home:
                        setFragment(homeFragment);
                        return true;
                    case R.id.nav_account:
                        setFragment(accountFragment);
                        return true;
                    case R.id.nav_settings:
                        setFragment(settingFragment);
                        return true;
                    case R.id.nav_notifications:
                        setFragment(notificationsFragment);
                        return true;
                    case R.id.nav_leaderboard:
                        setFragment(leaderboardFragment);
                        return true;
                }
                return  false;
            }
        });
    }

    private void setFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_framelayout,fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void applyTexts(String phonenum, String amount, String option) {
        AccountFragment accountFragment = new AccountFragment();
        accountFragment.applyTexts(phonenum, amount, option);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        if (key.equals(getString(R.string.dark_mode))){
            loadDarkModeFromPreference(sharedPreferences);
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }

    }

    private void loadDarkModeFromPreference(SharedPreferences sharedPreferences) {

        if (sharedPreferences.getBoolean(getString(R.string.dark_mode),false)){
            setTheme(R.style.darkTheme);

        }
    }

    public void setUpSharedPreferences(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        loadDarkModeFromPreference(sharedPreferences);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        PreferenceManager.getDefaultSharedPreferences(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        notificationRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value.size()==0)
                    bottomNavigationView.getOrCreateBadge(R.id.nav_notifications).setVisible(false);
                else {
                    bottomNavigationView.getOrCreateBadge(R.id.nav_notifications).setVisible(true);
                    bottomNavigationView.getOrCreateBadge(R.id.nav_notifications).setNumber(value.size());
                }
            }
        });
    }
}