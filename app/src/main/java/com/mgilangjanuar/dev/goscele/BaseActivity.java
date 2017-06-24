package com.mgilangjanuar.dev.goscele;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.mgilangjanuar.dev.goscele.Fragments.CourseFragment;
import com.mgilangjanuar.dev.goscele.Fragments.HomeFragment;
import com.mgilangjanuar.dev.goscele.Fragments.ScheduleFragment;
import com.mgilangjanuar.dev.goscele.Fragments.SettingFragment;

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
        Snackbar snack = Snackbar.make(findViewById(android.R.id.content), text, Snackbar.LENGTH_SHORT);
        View view = snack.getView();
        TextView textView = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        snack.show();
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

        switch (item.getItemId()) {
            case com.mgilangjanuar.dev.goscele.R.id.action_home:
                fragment = HomeFragment.newInstance();
                break;
            case com.mgilangjanuar.dev.goscele.R.id.action_course:
                fragment = CourseFragment.newInstance();
                break;
            case com.mgilangjanuar.dev.goscele.R.id.action_schedule:
                fragment = ScheduleFragment.newInstance();
                break;
            case com.mgilangjanuar.dev.goscele.R.id.action_setting:
                fragment = SettingFragment.newInstance();
                break;
        }

        if (currentItem == null || !item.equals(currentItem)) {
            item.setChecked(true);
            switchFragment(R.id.container, fragment);
        }
        currentItem = item;
    }

    public void switchFragment(int container, Fragment fragment) {
        FragmentTransaction beginTransaction = getSupportFragmentManager().beginTransaction();
        beginTransaction.replace(container, fragment);
        beginTransaction.commit();
    }
}
