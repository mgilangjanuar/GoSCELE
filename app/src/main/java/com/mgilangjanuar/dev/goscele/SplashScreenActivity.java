package com.mgilangjanuar.dev.goscele;

import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mgilangjanuar.dev.goscele.Presenters.SplashScreenPresenter;

public class SplashScreenActivity extends BaseActivity implements SplashScreenPresenter.SplashScreenServicePresenter {

    SplashScreenPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        presenter = new SplashScreenPresenter(this);
        setupSplashScreen();
    }

    @Override
    public void setupSplashScreen() {
        presenter.setupThreadPolicy();

        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress_bar_splash_screen);
        progressBar.getIndeterminateDrawable().setColorFilter(0xFFFFFFFF, android.graphics.PorterDuff.Mode.MULTIPLY);

        final TextView textView = (TextView) findViewById(R.id.text_quote);
        if (! presenter.isNetworkAvailable()) {
            textView.setText("\"Oh Snap! Please check your internet connection.\"\n\u2014 A Panda");
            return;
        }

        (new Thread(new Runnable() {
            @Override
            public void run() {
                final String quote = presenter.getQuote();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textView.setText(quote);
                    }
                });
            }
        })).start();

        presenter.authenticate();
    }
}
