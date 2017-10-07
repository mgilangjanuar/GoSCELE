package com.mgilangjanuar.dev.goscele.modules.auth.listener;

import android.app.ProgressDialog;

/**
 * Created by mgilangjanuar (mgilangjanuar@gmail.com)
 *
 * @since 2017
 */

public interface AuthListener {

    ProgressDialog getProgressDialog();

    void onUsernameOrPasswordEmpty();

    void onConnectionFailed();

    void onWrongCredential(String error);

    void onSuccess();
}
