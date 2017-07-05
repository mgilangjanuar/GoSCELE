package com.mgilangjanuar.dev.goscele;

import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mgilangjanuar.dev.goscele.Presenters.SplashScreenPresenter;

import butterknife.BindView;

public class SplashScreenActivity extends BaseActivity implements SplashScreenPresenter.SplashScreenServicePresenter {

    private SplashScreenPresenter presenter;

    @BindView(R.id.progress_bar_splash_screen)
    ProgressBar progressBar;
    @BindView(R.id.text_quote)
    TextView textView;

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

        progressBar.getIndeterminateDrawable().setColorFilter(0xFFFFFFFF, android.graphics.PorterDuff.Mode.MULTIPLY);

        if (!presenter.isNetworkAvailable()) {
            textView.setText("\"Oh Snap! Please check your internet connection.\"\n\u2014 A Panda");
            return;
        }

        (new Thread(() -> {
            final String quote = presenter.getQuote();
            runOnUiThread(() -> textView.setText(quote));
        })).start();

        presenter.authenticate();
    }
}
