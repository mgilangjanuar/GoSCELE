package com.mgilangjanuar.dev.goscele.Models;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by muhammadgilangjanuar on 5/15/17.
 */

public class ListCourseModel extends BaseRecord implements BaseRecord.BasicInterface {

    public List<CourseModel> courseModelList;

    public ListCourseModel(Context context) {
        super(context);
        courseModelList = new ArrayList<>();
        tag = "ListCourseModel";
    }

    @Override
    public void save() {
        Gson gson = new Gson();
        prefsEditor.putString(getAttributeTag("courseModelList"), gson.toJson(this.courseModelList));
        prefsEditor.commit();
    }

    @Override
    public void clear() {
        prefsEditor.putString(getAttributeTag("courseModelList"), null);
        prefsEditor.commit();
    }

    public List<CourseModel> getSavedCourseModelList() {
        Gson gson = new Gson();
        courseModelList = gson.fromJson(sharedPreferences.getString(getAttributeTag("courseModelList"), null), new TypeToken<ArrayList<CourseModel>>() {
        }.getType());
        return courseModelList;
    }
}
