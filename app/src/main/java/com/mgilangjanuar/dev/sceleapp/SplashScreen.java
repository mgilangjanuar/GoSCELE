package com.mgilangjanuar.dev.sceleapp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mgilangjanuar.dev.sceleapp.Models.QuoteModel;
import com.mgilangjanuar.dev.sceleapp.Presenters.AuthPresenter;
import com.mgilangjanuar.dev.sceleapp.Presenters.SchedulePresenter;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;

public class SplashScreen extends BaseActivity implements AuthPresenter.AuthServicePresenter {

    AuthPresenter authPresenter;
    static String QUOTE_URL = "https://api.forismatic.com/api/1.0/?method=getQuote&lang=en&format=json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress_bar_splash_screen);
        progressBar.getIndeterminateDrawable().setColorFilter(0xFFFFFFFF, android.graphics.PorterDuff.Mode.MULTIPLY);

        final TextView textView = (TextView) findViewById(R.id.text_quote);

        if (! isNetworkAvailable()) {
            textView.setText("\"Oh Snap! Please check your internet connection.\"\n\u2014 A Panda");
            return;
        }

        (new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    InputStream input = new URL(QUOTE_URL).openStream();
                    Reader reader = new InputStreamReader(input, "UTF-8");
                    final QuoteModel data = new Gson().fromJson(reader, QuoteModel.class);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (data.quoteAuthor == null || data.quoteAuthor.equals("")) {
                                data.quoteAuthor = "Unknown";
                            }
                            textView.setText("\"" + data.quoteText.trim() + "\"" + "\n\u2014 " + data.quoteAuthor.trim());
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        })).start();

        authPresenter = new AuthPresenter(this);
        (new Thread(new Runnable() {
            @Override
            public void run() {
                authenticate();
            }
        })).start();
    }

    @Override
    public void authenticate() {
        if (! authPresenter.isUsernameAndPasswordExist()) {
            forceRedirect(new Intent(SplashScreen.this, AuthActivity.class));
        } else {
            try {
                if (isAuthenticate()) {
                    forceRedirect(new Intent(SplashScreen.this, MainActivity.class));
                } else {
                    forceRedirect(new Intent(SplashScreen.this, AuthActivity.class));
                }
            } catch (IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Whoops! Please check your internet connection", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    }

    @Override
    public boolean isAuthenticate() throws IOException {
        return authPresenter.isLogin() || authPresenter.login(authPresenter.getUsername(), authPresenter.getPassword());
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
