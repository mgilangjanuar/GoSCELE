package com.mgilangjanuar.dev.sceleapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import com.mgilangjanuar.dev.sceleapp.Fragments.CourseFragment;
import com.mgilangjanuar.dev.sceleapp.Fragments.HomeFragment;
import com.mgilangjanuar.dev.sceleapp.Fragments.ScheduleFragment;
import com.mgilangjanuar.dev.sceleapp.Fragments.SettingFragment;

/**
 * Created by muhammadgilangjanuar on 5/14/17.
 */

public class BaseActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    MenuItem currentItem;

    public void redirect(Intent intent) {
        startActivity(intent);
    }

    public void forceRedirect(Intent intent) {
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void showToast(String text) {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void onNavigationSelected() {
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                selectMenu(item);
                return true;
            }
        });
    }

    protected void selectMenu(MenuItem item) {
        Fragment fragment = null;

        FragmentTransaction beginTransaction = getSupportFragmentManager().beginTransaction();

        switch (item.getItemId()) {
            case com.mgilangjanuar.dev.sceleapp.R.id.action_home:
                fragment = HomeFragment.newInstance();
                break;
            case com.mgilangjanuar.dev.sceleapp.R.id.action_course:
                fragment = CourseFragment.newInstance();
                break;
            case com.mgilangjanuar.dev.sceleapp.R.id.action_schedule:
                fragment = ScheduleFragment.newInstance();
                break;
            case com.mgilangjanuar.dev.sceleapp.R.id.action_setting:
                fragment = SettingFragment.newInstance();
                break;
        }

        if (currentItem == null || !item.equals(currentItem)) {
            item.setChecked(true);
            beginTransaction.replace(com.mgilangjanuar.dev.sceleapp.R.id.container, fragment);
            beginTransaction.commit();
        }
        currentItem = item;
    }

    public void switchFragment(int container, Fragment fragment) {
        FragmentTransaction beginTransaction = getSupportFragmentManager().beginTransaction();
        beginTransaction.replace(container, fragment);
        beginTransaction.commit();
    }
}
