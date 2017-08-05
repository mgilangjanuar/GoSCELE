package com.mgilangjanuar.dev.goscele.Models;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gilang on 8/5/17.
 */

public class ScheduleDailyModel extends BaseRecord {

    public List<ListSchedule> list;

    public ScheduleDailyModel(Context context) {
        super(context);
        list = new ArrayList<>();
    }

    @Override
    public String tag() {
        return getClass().getName();
    }

    @Override
    public void save() {
        Gson gson = new Gson();
        prefsEditor.putString(getAttributeTag("list"), gson.toJson(this.list));
        prefsEditor.commit();
    }

    @Override
    public void clear() {
        prefsEditor.putString(getAttributeTag("list"), null);
        prefsEditor.commit();
    }

    public List<ListSchedule> getSavedList() {
        Gson gson = new Gson();
        list = gson.fromJson(sharedPreferences.getString(getAttributeTag("list"), null), new TypeToken<ArrayList<ListSchedule>>() {
        }.getType());
        return list;
    }

    public static class Schedule {
        public String time;
        public String desc;
    }

    public static class ListSchedule {
        public List<Schedule> scheduleList;
        public String day;
    }
}
