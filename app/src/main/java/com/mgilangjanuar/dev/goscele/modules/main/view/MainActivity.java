package com.mgilangjanuar.dev.goscele.modules.main.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;

import com.mgilangjanuar.dev.goscele.R;
import com.mgilangjanuar.dev.goscele.base.BaseActivity;
import com.mgilangjanuar.dev.goscele.modules.auth.view.AuthActivity;
import com.mgilangjanuar.dev.goscele.modules.common.listener.CheckLoginListener;
import com.mgilangjanuar.dev.goscele.modules.main.presenter.MainPresenter;
import com.mgilangjanuar.dev.goscele.modules.main.util.BottomNavigationViewUtil;

import butterknife.BindView;

/**
 * Created by mgilangjanuar (mgilangjanuar@gmail.com)
 *
 * @since 2017
 */
public class MainActivity extends BaseActivity implements CheckLoginListener {

    @BindView(R.id.bottom_navigation)
    BottomNavigationView bottomNavigationView;

    private MenuItem currentItem;

    private MainPresenter presenter = new MainPresenter(this, this);

    @Override
    public void initialize(Bundle savedInstanceState) {
        presenter.checkLogin();
        BottomNavigationViewUtil.disableShiftMode(bottomNavigationView);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                selectMenu(item);
                return true;
            }
        });

        MenuItem menuItem;
        if (savedInstanceState != null) {
            int selectedItem = savedInstanceState.getInt("arg_selected_item", 0);
            menuItem = bottomNavigationView.getMenu().findItem(selectedItem);
        } else {
            menuItem = bottomNavigationView.getMenu().getItem(0);
        }
        selectMenu(menuItem);
    }

    @Override
    public int findLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void onAuthenticate(String name) {
        showSnackbar(String.format(getString(R.string.greeting_message), name));
    }

    @Override
    public void onNotAuthenticate() {
        if (presenter.getUserModel() == null) {
            redirect(AuthActivity.class, true);
        } else {
            presenter.login();
        }
    }

    @Override
    public void onBackPressed() {
        if (currentItem.getItemId() == R.id.action_home) {
            super.onBackPressed();
        } else {
            selectMenu(bottomNavigationView.getMenu().getItem(0));
        }
    }

    private void selectMenu(MenuItem item) {
        Fragment fragment = null;

        switch (item.getItemId()) {
            case R.id.action_home:
                fragment = HomeFragment.newInstance();
                break;
            case R.id.action_course:
                fragment = CourseFragment.newInstance();
                break;
            case R.id.action_schedule:
                fragment = ScheduleFragment.newInstance();
                break;
            case R.id.action_setting:
                fragment = SettingFragment.newInstance();
                break;
        }

        if (currentItem == null || !item.equals(currentItem)) {
            item.setChecked(true);
            FragmentTransaction beginTransaction = getSupportFragmentManager().beginTransaction();
            beginTransaction.replace(R.id.container, fragment);
            beginTransaction.commit();
        }
        currentItem = item;
    }
}
