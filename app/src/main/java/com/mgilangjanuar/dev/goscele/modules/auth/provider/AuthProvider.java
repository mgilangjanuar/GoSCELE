package com.mgilangjanuar.dev.goscele.modules.auth.provider;

import android.app.ProgressDialog;
import android.text.TextUtils;

import com.mgilangjanuar.dev.goscele.base.BaseProvider;
import com.mgilangjanuar.dev.goscele.modules.auth.listener.AuthListener;
import com.mgilangjanuar.dev.goscele.modules.common.model.CookieModel;
import com.mgilangjanuar.dev.goscele.modules.common.model.UserModel;
import com.mgilangjanuar.dev.goscele.utils.Constant;

import org.jsoup.Connection;
import org.jsoup.select.Elements;

import java.util.List;

/**
 * Created by mgilangjanuar (mgilangjanuar@gmail.com)
 *
 * @since 2017
 */

public class AuthProvider extends BaseProvider {

    private String username;
    private String password;
    private AuthListener authListener;
    private ProgressDialog progressDialog;

    public AuthProvider(AuthListener authListener, String username, String password) {
        super();
        this.authListener = authListener;
        this.progressDialog = authListener.getProgressDialog();
        this.username = username;
        this.password = password;
    }

    @Override
    public void run() {
        // try to get error
        execute(".error");
    }

    @Override
    public String url() {
        return Constant.BASE_URL + Constant.ROUTE_LOGIN;
    }

    @Override
    public String[] data() {
        return new String[]{"username", username, "password", password};
    }

    @Override
    public Connection.Method method() {
        return Connection.Method.POST;
    }

    @Override
    protected void onPreExecute() {
        if (progressDialog != null) {
            progressDialog.show();
        }
    }

    @Override
    protected void onPostExecute(List<Elements> elementses) {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        try {
            if (TextUtils.isEmpty(elementses.get(0).text())) {
                authListener.onSuccess();
                saveCookies();
                saveUser();
            } else {
                authListener.onWrongCredential(elementses.get(0).text());
            }
        } catch (Exception e) {
            authListener.onConnectionFailed();
        }
    }

    private void saveCookies() {
        for (String key : cookies.keySet()) {
            CookieModel cookieModel = new CookieModel().find().where("key = ?", key).executeSingle();
            if (cookieModel == null) {
                cookieModel = new CookieModel();
            }
            cookieModel.key = key;
            cookieModel.value = cookies.get(key);
            cookieModel.save();
        }
    }

    private void saveUser() {
        UserModel userModel = new UserModel().find().executeSingle();
        if (userModel == null) {
            userModel = new UserModel();
        }
        userModel.username = username;
        userModel.password = password;
        userModel.save();
    }
}
