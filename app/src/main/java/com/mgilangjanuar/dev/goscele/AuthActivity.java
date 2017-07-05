package com.mgilangjanuar.dev.goscele;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.mgilangjanuar.dev.goscele.Presenters.AuthPresenter;

import java.io.IOException;

import butterknife.BindView;

public class AuthActivity extends BaseActivity implements AuthPresenter.AuthServicePresenter {

    private AuthPresenter authPresenter;

    @BindView(R.id.login_button)
    Button btnLogin;
    @BindView(R.id.username)
    EditText etUsername;
    @BindView(R.id.password)
    EditText etPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.mgilangjanuar.dev.goscele.R.layout.activity_auth);

        authPresenter = new AuthPresenter(this);
        authenticate();
    }

    @Override
    public void authenticate() {
        btnLogin.setOnClickListener(v -> {
            hideKeyboard();
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (username.trim().equals("") || password.trim().equals("")) {
                showToast("Username and password are required field");
            } else {
                authPresenter.showProgressDialog();
                validate(username, password);
            }
        });
    }

    @Override
    public boolean isAuthenticate() throws IOException {
        return authPresenter.isUsernameAndPasswordExist()
                && authPresenter.login(authPresenter.getUsername(), authPresenter.getPassword());
    }

    private void validate(final String username, final String password) {
        (new Thread(() -> {
            try {
                if (authPresenter.login(username, password)) {
                    forceRedirect(new Intent(AuthActivity.this, MainActivity.class));
                } else {
                    runOnUiThread(() -> showToast("Username or password wrong"));
                }
            } catch (IOException e) {
                runOnUiThread(() -> showToast("Whoops! Please check your internet connection"));
            }
            authPresenter.dismissProgressDialog();
        })).start();
    }
}
