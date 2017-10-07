package com.mgilangjanuar.dev.goscele.modules.auth.presenter;

import android.text.TextUtils;

import com.mgilangjanuar.dev.goscele.base.BasePresenter;
import com.mgilangjanuar.dev.goscele.modules.auth.listener.AuthListener;
import com.mgilangjanuar.dev.goscele.modules.auth.provider.AuthProvider;
import com.mgilangjanuar.dev.goscele.modules.common.model.UserModel;

/**
 * Created by mgilangjanuar (mgilangjanuar@gmail.com)
 *
 * @since 2017
 */

public class AuthPresenter extends BasePresenter {

    private AuthListener authListener;

    public AuthPresenter(AuthListener authListener) {
        super();
        this.authListener = authListener;
        UserModel userModel = new UserModel().find().executeSingle();
        if (userModel != null) {
            login(userModel.username, userModel.password);
        }
    }

    public void login(String username, String password) {
        if (isEmpty(username, password)) {
            authListener.onUsernameOrPasswordEmpty();
        } else {
            new AuthProvider(authListener, username.trim(), password).run();
        }
    }

    private boolean isEmpty(String... data) {
        for (String datum : data) {
            if (TextUtils.isEmpty(datum)) {
                return true;
            }
        }
        return false;
    }
}
