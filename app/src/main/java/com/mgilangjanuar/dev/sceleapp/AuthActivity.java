package com.mgilangjanuar.dev.sceleapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.mgilangjanuar.dev.sceleapp.Presenters.AuthPresenter;

import java.io.IOException;

public class AuthActivity extends BaseActivity implements AuthPresenter.AuthServicePresenter {

    AuthPresenter authPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.mgilangjanuar.dev.sceleapp.R.layout.activity_auth);

        authPresenter = new AuthPresenter(this);
        authenticate();
    }

    @Override
    public void authenticate() {
        final Button loginButton = (Button) findViewById(com.mgilangjanuar.dev.sceleapp.R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = ((EditText) findViewById(com.mgilangjanuar.dev.sceleapp.R.id.username)).getText().toString().trim();
                String password = ((EditText) findViewById(com.mgilangjanuar.dev.sceleapp.R.id.password)).getText().toString().trim();

                if (username.trim().equals("") || password.trim().equals("")) {
                    showToast("Username and password are required field");
                } else {
                    authPresenter.showProgressDialog();
                    validate(username, password);
                }
            }
        });
    }

    @Override
    public boolean isAuthenticate() throws IOException {
        return authPresenter.isUsernameAndPasswordExist()
                && authPresenter.login(authPresenter.getUsername(), authPresenter.getPassword());
    }

    private void validate(final String username, final String password) {
        (new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (authPresenter.login(username, password)) {
                        authPresenter.save(username, password);
                        forceRedirect(new Intent(AuthActivity.this, MainActivity.class));
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showToast("Username or password wrong");
                            }
                        });
                    }
                } catch (IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showToast("Whoops! Please check your internet connection");
                        }
                    });
                }
                authPresenter.dismissProgressDialog();
            }
        })).start();
    }
}
