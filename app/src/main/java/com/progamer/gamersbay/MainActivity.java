package com.progamer.gamersbay;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.progamer.gamersbay.db.DbHelper;
import com.progamer.gamersbay.notification.NotificationsFragment;

public class MainActivity extends AppCompatActivity implements DialogClass.DialogClassListener {
    private DbHelper db;
    private BottomNavigationView bottomNavigationView;
    private FrameLayout frameLayout;
    private String title,body,time;
    private HomeFragment homeFragment;
    private AccountFragment accountFragment;
    private SettingsFragment settingsFragment;
    private NotificationsFragment notificationsFragment;
    private LeaderboardFragment leaderboardFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("firebaseGo", "token: "+FirebaseInstanceId.getInstance().getToken());

        bottomNavigationView = findViewById(R.id.bottom_nav);
        frameLayout = findViewById(R.id.main_framelayout);
        bottomNavigationView.getMenu().findItem(R.id.nav_home).setChecked(true);
        homeFragment = new HomeFragment();
        accountFragment = new AccountFragment();
        settingsFragment = new SettingsFragment();
        notificationsFragment = new NotificationsFragment();
        leaderboardFragment = new LeaderboardFragment();
        db = new DbHelper(this);
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
                        setFragment(settingsFragment);
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

}