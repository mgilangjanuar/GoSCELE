package com.mgilangjanuar.dev.goscele.Models;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by muhammadgilangjanuar on 5/18/17.
 */

public class CalendarEventModel extends BaseRecord implements BaseRecord.BasicInterface {

    public String date;
    public List<Integer> listEvent;

    public CalendarEventModel(Context context) {
        super(context);
        listEvent = new ArrayList<>();
        tag = "CalendarEventModel";
    }

    @Override
    public void save() {
        try {
            Gson gson = new Gson();
            prefsEditor.putString(getAttributeTag("listEvent"), gson.toJson(this.listEvent));
            prefsEditor.putString(getAttributeTag("date"), this.date);
            prefsEditor.commit();
        } catch (Exception e) {

        }
    }

    @Override
    public void clear() {
        prefsEditor.putString(getAttributeTag("listEvent"), null);
        prefsEditor.putString(getAttributeTag("date"), null);
        prefsEditor.commit();
    }

    public String getSavedDate() {
        date = sharedPreferences.getString(getAttributeTag("date"), null);
        return date;
    }

    public List<Integer> getSavedListEvent() {
        Gson gson = new Gson();
        listEvent = gson.fromJson(sharedPreferences.getString(getAttributeTag("listEvent"), null), new TypeToken<ArrayList<Integer>>() {
        }.getType());
        return listEvent;
    }
}
