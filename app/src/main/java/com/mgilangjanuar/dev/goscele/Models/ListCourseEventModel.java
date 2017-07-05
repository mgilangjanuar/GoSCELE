package com.mgilangjanuar.dev.goscele.Models;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by muhammadgilangjanuar on 5/22/17.
 */

public class ListCourseEventModel extends BaseRecord implements BaseRecord.BasicInterface {

    public CourseModel courseModel;
    public List<CourseEventModel> courseEventModelList;

    public ListCourseEventModel(Context context) {
        super(context);
        courseModel = new CourseModel();
        courseEventModelList = new ArrayList<>();
        tag = "ListCourseEventModel";
    }

    @Override
    public void save() {
        Gson gson = new Gson();
        prefsEditor.putString(getAttributeTag("courseEventModelList"), gson.toJson(this.courseEventModelList));
        prefsEditor.putString(getAttributeTag("courseModel"), gson.toJson(this.courseModel));
        prefsEditor.commit();
    }

    @Override
    public void clear() {
        prefsEditor.putString(getAttributeTag("courseEventModelList"), null);
        prefsEditor.putString(getAttributeTag("courseModel"), null);
        prefsEditor.commit();
    }

    public List<CourseEventModel> getSavedCourseEventModelList() {
        Gson gson = new Gson();
        courseEventModelList = gson.fromJson(sharedPreferences.getString(getAttributeTag("courseEventModelList"), null), new TypeToken<ArrayList<CourseEventModel>>() {
        }.getType());
        return courseEventModelList;
    }

    public CourseModel getSavedCourseModel() {
        Gson gson = new Gson();
        courseModel = gson.fromJson(sharedPreferences.getString(getAttributeTag("courseModel"), null), CourseModel.class);
        return courseModel;
    }
}
