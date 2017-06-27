package com.mgilangjanuar.dev.goscele.Presenters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.StrictMode;

import com.google.gson.Gson;
import com.mgilangjanuar.dev.goscele.AuthActivity;
import com.mgilangjanuar.dev.goscele.BaseActivity;
import com.mgilangjanuar.dev.goscele.MainActivity;
import com.mgilangjanuar.dev.goscele.Models.QuoteModel;
import com.mgilangjanuar.dev.goscele.R;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;

/**
 * Created by mjanuar on 25/06/17.
 */

public class SplashScreenPresenter implements AuthPresenter.AuthServicePresenter {

    public AuthPresenter authPresenter;
    Activity activity;
    static String QUOTE_URL = "https://api.forismatic.com/api/1.0/?method=getQuote&lang=en&format=json";

    public interface SplashScreenServicePresenter {
        void setupSplashScreen();
    }

    public SplashScreenPresenter(Activity activity) {
        this.activity = activity;
        authPresenter = new AuthPresenter(activity);
    }

    @Override
    public void authenticate() {
        (new Thread(new Runnable() {
            @Override
            public void run() {
                if (! authPresenter.isUsernameAndPasswordExist()) {
                    ((BaseActivity) activity).forceRedirect(new Intent(activity, AuthActivity.class));
                } else {
                    try {
                        if (isAuthenticate()) {
                            ((BaseActivity) activity).forceRedirect(new Intent(activity, MainActivity.class));
                        } else {
                            ((BaseActivity) activity).forceRedirect(new Intent(activity, AuthActivity.class));
                        }
                    } catch (IOException e) {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ((BaseActivity) activity).showToast("Whoops! Please check your internet connection");
                            }
                        });
                        authenticate();
                    }
                }
            }
        })).start();
    }

    @Override
    public boolean isAuthenticate() throws IOException {
        return authPresenter.isLogin() || authPresenter.login(authPresenter.getUsername(), authPresenter.getPassword());
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void setupThreadPolicy() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    public String getQuote() {
        try {
            InputStream input = new URL(QUOTE_URL).openStream();
            Reader reader = new InputStreamReader(input, "UTF-8");
            QuoteModel data = new Gson().fromJson(reader, QuoteModel.class);
            if (data.quoteAuthor == null || data.quoteAuthor.equals("")) {
                data.quoteAuthor = "Unknown";
            }
            return "\"" + data.quoteText.trim() + "\"" + "\n\u2014 " + data.quoteAuthor.trim();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
