package com.mgilangjanuar.dev.goscele.Models;

import android.content.Context;

/**
 * Created by muhammadgilangjanuar on 5/14/17.
 */

public class AccountModel extends BaseRecord implements BaseRecord.BasicInterface {
    public String name;
    public String username;
    public String password;

    public boolean isUsingInAppBrowser;

    public  AccountModel(Context context) {
        super(context);
        tag = "AccountModel";
    }

    public void save() {
        prefsEditor.putString(getAttributeTag("name"), this.name);
        prefsEditor.putString(getAttributeTag("username"), this.username);
        prefsEditor.putString(getAttributeTag("password"), this.password);
        prefsEditor.putBoolean(getAttributeTag("isUsingInAppBrowser"), this.isUsingInAppBrowser);
        prefsEditor.commit();
    }

    public void clear() {
        prefsEditor.putString(getAttributeTag("name"), null);
        prefsEditor.putString(getAttributeTag("username"), null);
        prefsEditor.putString(getAttributeTag("password"), null);
        prefsEditor.putBoolean(getAttributeTag("isUsingInAppBrowser"), false);
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
        isUsingInAppBrowser = sharedPreferences.getBoolean(getAttributeTag("isUsingInAppBrowser"), false);
        return isUsingInAppBrowser;
    }

}
