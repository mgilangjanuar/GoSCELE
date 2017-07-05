package com.mgilangjanuar.dev.goscele.Models;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by muhammadgilangjanuar on 5/17/17.
 */

public class ListScheduleModel extends BaseRecord implements BaseRecord.BasicInterface {
    public List<ScheduleModel> scheduleModelList;
    public String date;

    public ListScheduleModel(Context context) {
        super(context);
        scheduleModelList = new ArrayList<>();
        tag = "ListScheduleModel";
    }

    @Override
    public void save() {
        Gson gson = new Gson();
        prefsEditor.putString(getAttributeTag("scheduleModelList"), gson.toJson(this.scheduleModelList));
        prefsEditor.putString(getAttributeTag("date"), this.date);
        prefsEditor.commit();
    }

    @Override
    public void clear() {
        prefsEditor.putString(getAttributeTag("scheduleModelList"), null);
        prefsEditor.putString(getAttributeTag("date"), null);
        prefsEditor.commit();
    }

    public List<ScheduleModel> getSavedScheduleModelList() {
        Gson gson = new Gson();
        scheduleModelList = gson.fromJson(sharedPreferences.getString(getAttributeTag("scheduleModelList"), null), new TypeToken<ArrayList<ScheduleModel>>() {
        }.getType());
        return scheduleModelList;
    }

    public String getSavedDate() {
        date = sharedPreferences.getString(getAttributeTag("date"), null);
        return date;
    }
}
