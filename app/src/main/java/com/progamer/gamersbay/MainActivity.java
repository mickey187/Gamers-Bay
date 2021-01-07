package com.progamer.gamersbay;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements DialogClass.DialogClassListener {

    private BottomNavigationView bottomNavigationView;
    private FrameLayout frameLayout;

    private HomeFragment homeFragment;
    private AccountFragment accountFragment;
    private SettingsFragment settingsFragment;
    private NotificationsFragment notificationsFragment;
    private LeaderboardFragment leaderboardFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_nav);
        frameLayout = findViewById(R.id.main_framelayout);

        bottomNavigationView.getMenu().findItem(R.id.nav_home).setChecked(true);
        homeFragment = new HomeFragment();
        accountFragment = new AccountFragment();
        settingsFragment = new SettingsFragment();
        notificationsFragment = new NotificationsFragment();
        leaderboardFragment = new LeaderboardFragment();

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