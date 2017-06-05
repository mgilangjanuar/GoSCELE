package com.mgilangjanuar.dev.sceleapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.mgilangjanuar.dev.sceleapp.Models.ListCoursePostModel;
import com.mgilangjanuar.dev.sceleapp.Presenters.AuthPresenter;
import com.mgilangjanuar.dev.sceleapp.Presenters.CourseDetailPresenter;
import com.mgilangjanuar.dev.sceleapp.Presenters.SchedulePresenter;
import com.mgilangjanuar.dev.sceleapp.Services.ForumService;

import java.io.IOException;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.mgilangjanuar.dev.sceleapp.R.layout.activity_main);

        bottomNavigationView = (BottomNavigationView) findViewById(com.mgilangjanuar.dev.sceleapp.R.id.bottom_navigation);
        AuthPresenter.BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        onNavigationSelected();

        MenuItem mMenuItem;
        if (savedInstanceState != null) {
            int mSelectedItem = savedInstanceState.getInt("arg_selected_item", 0);
            mMenuItem = bottomNavigationView.getMenu().findItem(mSelectedItem);
        } else {
            mMenuItem = bottomNavigationView.getMenu().getItem(0);
        }
        selectMenu(mMenuItem);

        final Activity activity = this;
        (new Thread(new Runnable() {
            @Override
            public void run() {
                (new SchedulePresenter(activity, null)).notifySchedule();
            }
        })).start();
    }
}
