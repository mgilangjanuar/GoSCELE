package com.mgilangjanuar.dev.sceleapp.Presenters;

import android.app.Activity;
import android.app.ProgressDialog;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.util.Log;

import java.io.IOException;
import java.lang.reflect.Field;

import com.mgilangjanuar.dev.sceleapp.Models.AccountModel;
import com.mgilangjanuar.dev.sceleapp.Models.ListCourseModel;
import com.mgilangjanuar.dev.sceleapp.Models.ListCurrentCourseModel;
import com.mgilangjanuar.dev.sceleapp.Models.ListScheduleModel;
import com.mgilangjanuar.dev.sceleapp.Services.AuthService;

/**
 * Created by muhammadgilangjanuar on 5/14/17.
 */

public class AuthPresenter {
    Activity activity;
    AuthService authService;
    AccountModel accountModel;

    ProgressDialog progress;

    public interface AuthServicePresenter {
        void authenticate();
        boolean isAuthenticate();
    }

    public AuthPresenter(Activity activity) {
        this.authService = new AuthService();
        this.activity = activity;
        this.accountModel = new AccountModel(activity);
    }

    public boolean isLogin() {
        try {
            return authService.isLogin();
        } catch (IOException e) {
            Log.e("AuthPresenter", e.getMessage());
        }
        return  false;
    }

    public boolean login(String username, String password) {
        try {
            return authService.login(username, password);
        } catch (IOException e) {
            Log.e("AuthPresenter", e.getMessage());
        }
        return  false;
    }

    public String getUserText() {
        try {
            return authService.getUserText(accountModel.username, accountModel.password);
        } catch (IOException e) {
            Log.e("AuthPresenter", e.getMessage());
        }
        return  null;
    }

    public void save(String username, String password) {
        accountModel.username = username;
        accountModel.password = password;
        accountModel.name = getUserText();
        accountModel.isUsingInAppBrowser = true;
        accountModel.save();
    }

    public String getUsername() {
        return  accountModel.getSavedUsername();
    }

    public String getPassword() {
        return  accountModel.getSavedPassword();
    }

    public boolean isUsernameAndPasswordExist() {
        return getUsername() != null && getPassword() != null;
    }

    public boolean logout() {
        try {
            authService.logout();
            accountModel.clear();
            (new ListCourseModel(activity)).clear();
            (new ListScheduleModel(activity)).clear();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return !isLogin();
    }

    public void showProgressDialog() {
        progress = new ProgressDialog(activity);
        progress.setMessage("Connecting...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(false);
        progress.setCancelable(false);
        progress.show();
    }

    public void dismissProgressDialog() {
        progress.cancel();
    }

    public static class BottomNavigationViewHelper {
        public static void disableShiftMode(BottomNavigationView view) {
            BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
            try {
                Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
                shiftingMode.setAccessible(true);
                shiftingMode.setBoolean(menuView, false);
                shiftingMode.setAccessible(false);
                for (int i = 0; i < menuView.getChildCount(); i++) {
                    BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                    //noinspection RestrictedApi
                    item.setShiftingMode(false);
                    // set once again checked value, so view will be updated
                    //noinspection RestrictedApi
                    item.setChecked(item.getItemData().isChecked());
                }
            } catch (NoSuchFieldException e) {
                Log.e("BNVHelper", "Unable to get shift mode field", e);
            } catch (IllegalAccessException e) {
                Log.e("BNVHelper", "Unable to change value of shift mode", e);
            }
        }
    }

}
