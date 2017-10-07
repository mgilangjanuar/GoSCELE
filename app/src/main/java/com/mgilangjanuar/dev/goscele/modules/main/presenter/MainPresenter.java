package com.mgilangjanuar.dev.goscele.modules.main.presenter;

import android.app.ProgressDialog;

import com.mgilangjanuar.dev.goscele.R;
import com.mgilangjanuar.dev.goscele.base.BasePresenter;
import com.mgilangjanuar.dev.goscele.modules.auth.listener.AuthListener;
import com.mgilangjanuar.dev.goscele.modules.auth.provider.AuthProvider;
import com.mgilangjanuar.dev.goscele.modules.auth.view.AuthActivity;
import com.mgilangjanuar.dev.goscele.modules.common.listener.CheckLoginListener;
import com.mgilangjanuar.dev.goscele.modules.common.model.UserModel;
import com.mgilangjanuar.dev.goscele.modules.common.provider.CheckLoginProvider;
import com.mgilangjanuar.dev.goscele.modules.main.view.MainActivity;

/**
 * Created by mgilangjanuar (mgilangjanuar@gmail.com)
 *
 * @since 2017
 */

public class MainPresenter extends BasePresenter {

    private MainActivity activity;
    private CheckLoginListener checkLoginListener;

    public MainPresenter(MainActivity activity, CheckLoginListener checkLoginListener) {
        this.activity = activity;
        this.checkLoginListener = checkLoginListener;
    }

    public void checkLogin() {
        new CheckLoginProvider(checkLoginListener).run();
    }

    public void login() {
        final UserModel model = getUserModel();
        new AuthProvider(new AuthListener() {
            @Override
            public ProgressDialog getProgressDialog() {
                return null;
            }

            @Override
            public void onUsernameOrPasswordEmpty() {
                activity.redirect(AuthActivity.class, true);
            }

            @Override
            public void onConnectionFailed() {
                activity.showSnackbar(R.string.connection_problem);
            }

            @Override
            public void onWrongCredential(String error) {
                activity.showSnackbar(error);
                activity.redirect(AuthActivity.class, true);
            }

            @Override
            public void onSuccess() {
                activity.onAuthenticate(model.name);
            }
        }, model.username, model.password).run();
    }

    public UserModel getUserModel() {
        return new UserModel().find().executeSingle();
    }
}
