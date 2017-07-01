package com.mgilangjanuar.dev.goscele.Models;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mgilangjanuar.dev.goscele.Services.AuthService;

import java.io.IOException;
import java.util.Map;

/**
 * Created by muhammadgilangjanuar on 5/14/17.
 */

public class AccountModel extends BaseRecord implements BaseRecord.BasicInterface {
    public String name;
    public String username;
    public String password;

    public boolean isUsingInAppBrowser;
    public boolean isSaveCredential;

    public Map<String, String> cookies;

    public  AccountModel(Context context) {
        super(context);
        tag = "AccountModel";
    }

    public void save() {
        Gson gson = new Gson();
        if (isSaveCredential) {
            prefsEditor.putString(getAttributeTag("password"), this.password);
        } else {
            prefsEditor.putString(getAttributeTag("password"), null);
        }
        prefsEditor.putString(getAttributeTag("name"), this.name);
        prefsEditor.putString(getAttributeTag("username"), this.username);
        prefsEditor.putBoolean(getAttributeTag("isUsingInAppBrowser"), this.isUsingInAppBrowser);
        prefsEditor.putBoolean(getAttributeTag("isSaveCredential"), this.isSaveCredential);
        prefsEditor.putString(getAttributeTag("cookies"), gson.toJson(this.cookies));
        prefsEditor.commit();
    }

    public void clear() {
        prefsEditor.putString(getAttributeTag("name"), null);
        prefsEditor.putString(getAttributeTag("username"), null);
        prefsEditor.putString(getAttributeTag("password"), null);
        prefsEditor.putBoolean(getAttributeTag("isUsingInAppBrowser"), true);
        prefsEditor.putBoolean(getAttributeTag("isSaveCredential"), true);
        prefsEditor.putString(getAttributeTag("cookies"), null);
        prefsEditor.commit();
    }

    public String getSavedName() {
        name = sharedPreferences.getString(getAttributeTag("name"), null);
        return name;
    }

    public String getSavedUsername() {
        username = sharedPreferences.getString(getAttributeTag("username"), null);
        return username;
    }

    public String getSavedPassword() {
        password = sharedPreferences.getString(getAttributeTag("password"), null);
        return password;
    }

    public boolean isUsingInAppBrowser() {
        isUsingInAppBrowser = sharedPreferences.getBoolean(getAttributeTag("isUsingInAppBrowser"), true);
        return isUsingInAppBrowser;
    }

    public boolean isSaveCredential() {
        isSaveCredential = sharedPreferences.getBoolean(getAttributeTag("isSaveCredential"), true);
        return isSaveCredential;
    }

    public Map<String, String> getSavedCookies() {
        Gson gson = new Gson();
        cookies = gson.fromJson(sharedPreferences.getString(getAttributeTag("cookies"), null), new TypeToken<Map<String, String>>() {}.getType());
        return cookies;
    }

    public void toggleInAppBrowser() {
        isUsingInAppBrowser = ! isUsingInAppBrowser();
        name = getSavedName();
        username = getSavedUsername();
        password = getSavedPassword();
        isSaveCredential = isSaveCredential();
        try {
            cookies = AuthService.getCookies();
        } catch (IOException e) {
            e.printStackTrace();
        }
        save();
    }

    public void toggleSaveCredential() {
        isSaveCredential = ! isSaveCredential();
        name = getSavedName();
        username = getSavedUsername();
        password = isSaveCredential ? getSavedPassword() : null;
        isUsingInAppBrowser = isUsingInAppBrowser();
        try {
            cookies = AuthService.getCookies();
        } catch (IOException e) {
            e.printStackTrace();
        }
        save();
    }

}
