package com.mgilangjanuar.dev.goscele;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.mgilangjanuar.dev.goscele.Fragments.CourseFragment;
import com.mgilangjanuar.dev.goscele.Fragments.HomeFragment;
import com.mgilangjanuar.dev.goscele.Fragments.ScheduleFragment;
import com.mgilangjanuar.dev.goscele.Fragments.SettingFragment;

import butterknife.ButterKnife;

/**
 * Created by muhammadgilangjanuar on 5/14/17.
 */

public class BaseActivity extends AppCompatActivity {

    private MenuItem currentItem;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
    }

    public void redirect(Intent intent) {
        startActivity(intent);
    }

    public void forceRedirect(Intent intent) {
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void showToast(String text) {
        Snackbar snack = Snackbar.make(findViewById(android.R.id.content), text, Snackbar.LENGTH_LONG);
        View view = snack.getView();
        TextView textView = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        snack.show();
    }

    protected void onNavigationSelected(BottomNavigationView bottomNavigationView) {
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            selectMenu(item);
            return true;
        });
    }

    protected void selectMenu(MenuItem item) {
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
            switchFragment(R.id.container, fragment);
        }
        currentItem = item;
    }

    public void switchFragment(int container, Fragment fragment) {
        FragmentTransaction beginTransaction = getSupportFragmentManager().beginTransaction();
        beginTransaction.replace(container, fragment);
        beginTransaction.commit();
    }

    public void hideKeyboard() {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }
}
