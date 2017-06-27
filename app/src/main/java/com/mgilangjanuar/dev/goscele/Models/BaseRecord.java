package com.mgilangjanuar.dev.goscele.Models;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by muhammadgilangjanuar on 5/14/17.
 */

public class BaseRecord {

    public interface BasicInterface {
        void save();
        void clear();
    }

    protected Context context;
    protected SharedPreferences sharedPreferences;
    protected SharedPreferences.Editor prefsEditor;
    protected String tag;

    public BaseRecord(Context context) {
        this.context = context;
        this.sharedPreferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
//        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        this.prefsEditor = sharedPreferences.edit();
    }

    public String getAttributeTag(String attr) {
        return tag + "_" + attr;
    }
}
