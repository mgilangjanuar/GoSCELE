package com.mgilangjanuar.dev.goscele.modules.auth.view;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.EditText;

import com.mgilangjanuar.dev.goscele.R;
import com.mgilangjanuar.dev.goscele.base.BaseActivity;
import com.mgilangjanuar.dev.goscele.modules.auth.listener.AuthListener;
import com.mgilangjanuar.dev.goscele.modules.auth.presenter.AuthPresenter;
import com.mgilangjanuar.dev.goscele.modules.main.view.MainActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by mgilangjanuar (mgilangjanuar@gmail.com)
 *
 * @since 2017
 */

public class AuthActivity extends BaseActivity implements AuthListener {

    @BindView(R.id.username)
    EditText username;

    @BindView(R.id.password)
    EditText password;

    private AuthPresenter authPresenter;

    @Override
    public void initialize(Bundle savedInstanceState) {
        super.initialize(savedInstanceState);
        authPresenter = new AuthPresenter(this);
    }

    @Override
    public int findLayout() {
        return R.layout.activity_auth;
    }

    @OnClick(R.id.login_button)
    public void onClickLogin() {
        hideKeyboard();
        authPresenter.login(username.getText().toString(), password.getText().toString());
    }

    @Override
    public void onUsernameOrPasswordEmpty() {
        showSnackbar(R.string.username_password_required_field);
    }

    @Override
    public ProgressDialog getProgressDialog() {
        return showProgress(R.string.loading);
    }

    @Override
    public void onConnectionFailed() {
        showSnackbar(R.string.connection_problem);
    }

    @Override
    public void onWrongCredential(String error) {
        showSnackbar(error);
    }

    @Override
    public void onSuccess() {
        redirect(MainActivity.class, true);
    }
}
