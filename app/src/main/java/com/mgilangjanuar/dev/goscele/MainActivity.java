package com.mgilangjanuar.dev.goscele;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.mgilangjanuar.dev.goscele.Presenters.AuthPresenter;
import com.mgilangjanuar.dev.goscele.Presenters.SchedulePresenter;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.mgilangjanuar.dev.goscele.R.layout.activity_main);

        bottomNavigationView = (BottomNavigationView) findViewById(com.mgilangjanuar.dev.goscele.R.id.bottom_navigation);
        AuthPresenter.BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        onNavigationSelected();

        MenuItem menuItem;
        if (savedInstanceState != null) {
            int mSelectedItem = savedInstanceState.getInt("arg_selected_item", 0);
            menuItem = bottomNavigationView.getMenu().findItem(mSelectedItem);
        } else {
            menuItem = bottomNavigationView.getMenu().getItem(0);
        }

        try {
            selectMenu(menuItem);
        } catch (Exception e) {
        }

        final Activity activity = this;
        (new Thread(new Runnable() {
            @Override
            public void run() {
                (new SchedulePresenter(activity, null)).notifySchedule();
            }
        })).start();
    }
}
