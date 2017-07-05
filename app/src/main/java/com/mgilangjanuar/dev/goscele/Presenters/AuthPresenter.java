package com.mgilangjanuar.dev.goscele.Presenters;

import android.app.Activity;
import android.app.ProgressDialog;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.util.Log;

import com.mgilangjanuar.dev.goscele.Models.AccountModel;
import com.mgilangjanuar.dev.goscele.Models.ListCourseModel;
import com.mgilangjanuar.dev.goscele.Models.ListScheduleModel;
import com.mgilangjanuar.dev.goscele.Services.AuthService;

import java.io.IOException;
import java.lang.reflect.Field;

/**
 * Created by muhammadgilangjanuar on 5/14/17.
 */

public class AuthPresenter {
    private Activity activity;
    private AuthService authService;
    private AccountModel accountModel;

    private ProgressDialog progress;

    public interface AuthServicePresenter {
        void authenticate();

        boolean isAuthenticate() throws IOException;
    }

    public AuthPresenter(Activity activity) {
        this.authService = new AuthService();
        this.activity = activity;
        this.accountModel = new AccountModel(activity);
        if (accountModel.getSavedCookies() != null) {
            AuthService.setCookies(accountModel.cookies);
        }
    }

    public boolean isLogin() throws IOException {
        return authService.isLogin();
    }

    public boolean login(String username, String password) throws IOException {
        boolean isLogin = authService.login(username, password);
        save(username, password);
        return isLogin;
    }

    public String getUserText() {
        try {
            return authService.getUserText(accountModel.username, accountModel.password);
        } catch (IOException e) {
            Log.e("AuthPresenter", e.getMessage());
        }
        return null;
    }

    public void save(String username, String password) {
        accountModel.username = username;
        accountModel.password = password;
        accountModel.name = getUserText();
        accountModel.isUsingInAppBrowser = accountModel.isUsingInAppBrowser();
        accountModel.isSaveCredential = accountModel.isSaveCredential();
        try {
            accountModel.cookies = AuthService.getCookies();
        } catch (IOException e) {
            e.printStackTrace();
        }
        accountModel.save();
    }

    public String getUsername() {
        return accountModel.getSavedUsername();
    }

    public String getPassword() {
        return accountModel.getSavedPassword();
    }

    public boolean isUsernameAndPasswordExist() {
        return (accountModel.isSaveCredential() && getUsername() != null && getPassword() != null) || (!accountModel.isSaveCredential());
    }

    public boolean logout() {
        try {
            authService.logout();
            accountModel.clear();
            (new ListCourseModel(activity)).clear();
            (new ListScheduleModel(activity)).clear();
            return !isLogin();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
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

    public String getCookies() {
        try {
            return "MoodleSession=" + AuthService.getCookies().get("MoodleSession");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
