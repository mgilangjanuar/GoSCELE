package com.mgilangjanuar.dev.goscele.modules.common.provider;

import android.text.TextUtils;

import com.mgilangjanuar.dev.goscele.base.BaseProvider;
import com.mgilangjanuar.dev.goscele.modules.common.listener.CheckLoginListener;
import com.mgilangjanuar.dev.goscele.modules.common.model.CookieModel;
import com.mgilangjanuar.dev.goscele.modules.common.model.UserModel;
import com.mgilangjanuar.dev.goscele.utils.Constant;

import org.jsoup.select.Elements;

import java.util.List;
import java.util.Map;

/**
 * Created by mgilangjanuar (mgilangjanuar@gmail.com)
 *
 * @since 2017
 */

public class CheckLoginProvider extends BaseProvider {
    private CheckLoginListener checkLoginListener;

    public CheckLoginProvider(CheckLoginListener checkLoginListener) {
        this.checkLoginListener = checkLoginListener;
    }

    @Override
    public void run() {
        execute(".usertext");
    }

    @Override
    public String url() {
        return Constant.BASE_URL;
    }

    @Override
    public Map<String, String> cookies() {
        return CookieModel.getCookiesMap();
    }

    @Override
    protected void onPostExecute(List<Elements> elementses) {
        if (elementses.size() > 0 && !TextUtils.isEmpty(elementses.get(0).text())) {
            String fullname = elementses.get(0).text();
            checkLoginListener.onAuthenticate(fullname);
            saveUser(fullname);
        } else {
            checkLoginListener.onNotAuthenticate();
        }
    }

    private void saveUser(String name) {
        UserModel userModel = new UserModel().find().executeSingle();
        if (userModel == null) {
            userModel = new UserModel();
        }
        userModel.name = name;
        userModel.save();
    }
}
