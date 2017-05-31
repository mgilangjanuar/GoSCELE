package com.mgilangjanuar.dev.sceleapp.Models;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by muhammadgilangjanuar on 5/21/17.
 */

public class ListCoursePostModel extends BaseRecord implements BaseRecord.BasicInterface {

    public CourseModel courseModel;
    public List<CoursePostModel> coursePostModelList;

    public ListCoursePostModel(Context context) {
        super(context);
        courseModel = new CourseModel();
        coursePostModelList = new ArrayList<>();
        tag = "ListCoursePostModel";
    }

    @Override
    public void save() {
        Gson gson = new Gson();
        prefsEditor.putString(getAttributeTag("coursePostModelList"), gson.toJson(this.coursePostModelList));
        prefsEditor.putString(getAttributeTag("courseModel"), gson.toJson(this.courseModel));
        prefsEditor.commit();
    }

    @Override
    public void clear() {
        prefsEditor.putString(getAttributeTag("coursePostModelList"), null);
        prefsEditor.putString(getAttributeTag("courseModel"), null);
        prefsEditor.commit();
    }

    public List<CoursePostModel> getSavedCoursePostModels() {
        Gson gson = new Gson();
        coursePostModelList =  gson.fromJson(sharedPreferences.getString(getAttributeTag("coursePostModelList"), null), new TypeToken<ArrayList<CoursePostModel>>() {}.getType());
        return coursePostModelList;
    }

    public CourseModel getSavedCourseModel() {
        Gson gson = new Gson();
        courseModel =  gson.fromJson(sharedPreferences.getString(getAttributeTag("courseModel"), null), CourseModel.class);
        return courseModel;
    }
}
