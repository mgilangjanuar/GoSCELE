package com.mgilangjanuar.dev.goscele;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;

import com.mgilangjanuar.dev.goscele.Presenters.AuthPresenter;
import com.mgilangjanuar.dev.goscele.Presenters.SchedulePresenter;

import butterknife.BindView;

public class MainActivity extends BaseActivity {

    @BindView(R.id.bottom_navigation) BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.mgilangjanuar.dev.goscele.R.layout.activity_main);

        AuthPresenter.BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        onNavigationSelected(bottomNavigationView);

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

        (new Thread(() -> (new SchedulePresenter(this, null)).notifySchedule())).start();
    }
}
