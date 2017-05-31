package com.mgilangjanuar.dev.sceleapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.mgilangjanuar.dev.sceleapp.Models.ListCoursePostModel;
import com.mgilangjanuar.dev.sceleapp.Presenters.AuthPresenter;
import com.mgilangjanuar.dev.sceleapp.Presenters.CourseDetailPresenter;
import com.mgilangjanuar.dev.sceleapp.Presenters.SchedulePresenter;

public class MainActivity extends BaseActivity implements AuthPresenter.AuthServicePresenter {

    AuthPresenter authPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.mgilangjanuar.dev.sceleapp.R.layout.activity_main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

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

        authPresenter = new AuthPresenter(this);
        authPresenter.showProgressDialog();
        (new Thread(new Runnable() {
            @Override
            public void run() {
                authenticate();
            }
        })).start();
    }

    @Override
    public void authenticate() {
        if (! isAuthenticate()) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    forceRedirect(new Intent(MainActivity.this, AuthActivity.class));
                }
            });
        }
        authPresenter.dismissProgressDialog();
        (new SchedulePresenter(this, null)).notifySchedule();
    }

    @Override
    public boolean isAuthenticate() {
        return authPresenter.isUsernameAndPasswordExist()
                && (authPresenter.isLogin() || authPresenter.login(authPresenter.getUsername(), authPresenter.getPassword()));
    }
}
