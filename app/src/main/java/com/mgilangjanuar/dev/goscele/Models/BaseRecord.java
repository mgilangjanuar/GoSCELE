package com.mgilangjanuar.dev.goscele.Models;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by muhammadgilangjanuar on 5/14/17.
 */

public abstract class BaseRecord {
    protected Context context;
    protected SharedPreferences sharedPreferences;
    protected SharedPreferences.Editor prefsEditor;

    public BaseRecord(Context context) {
        this.context = context;
        this.sharedPreferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        this.prefsEditor = sharedPreferences.edit();
    }

    public String getAttributeTag(String attr) {
        return tag() + "_" + attr;
    }

    public abstract String tag();

    public abstract void save();

    public abstract void clear();
}
