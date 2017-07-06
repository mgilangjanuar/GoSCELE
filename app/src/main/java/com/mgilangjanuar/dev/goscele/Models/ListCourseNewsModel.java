package com.mgilangjanuar.dev.goscele.Models;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by muhammadgilangjanuar on 5/23/17.
 */

public class ListCourseNewsModel extends BaseRecord {

    public CourseModel courseModel;
    public List<CourseNewsModel> courseNewsModelList;

    public ListCourseNewsModel(Context context) {
        super(context);
        courseModel = new CourseModel();
        courseNewsModelList = new ArrayList<>();
    }

    @Override
    public String tag() {
        return "ListCourseNewsModel";
    }

    @Override
    public void save() {
        Gson gson = new Gson();
        prefsEditor.putString(getAttributeTag("courseNewsModelList"), gson.toJson(this.courseNewsModelList));
        prefsEditor.putString(getAttributeTag("courseModel"), gson.toJson(this.courseModel));
        prefsEditor.commit();
    }

    @Override
    public void clear() {
        prefsEditor.putString(getAttributeTag("courseNewsModelList"), null);
        prefsEditor.putString(getAttributeTag("courseModel"), null);
        prefsEditor.commit();
    }

    public List<CourseNewsModel> getSavedCourseNewsModelList() {
        Gson gson = new Gson();
        courseNewsModelList = gson.fromJson(sharedPreferences.getString(getAttributeTag("courseNewsModelList"), null), new TypeToken<ArrayList<CourseNewsModel>>() {
        }.getType());
        return courseNewsModelList;
    }

    public CourseModel getSavedCourseModel() {
        Gson gson = new Gson();
        courseModel = gson.fromJson(sharedPreferences.getString(getAttributeTag("courseModel"), null), CourseModel.class);
        return courseModel;
    }
}
