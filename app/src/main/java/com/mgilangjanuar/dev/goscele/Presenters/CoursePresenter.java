package com.mgilangjanuar.dev.goscele.Presenters;

import android.app.Activity;
import android.util.Log;
import android.view.View;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import com.mgilangjanuar.dev.goscele.Adapters.AllCoursesViewAdapter;
import com.mgilangjanuar.dev.goscele.Adapters.CurrentCoursesViewAdapter;
import com.mgilangjanuar.dev.goscele.Adapters.SearchCourseAdapter;
import com.mgilangjanuar.dev.goscele.Models.CourseModel;
import com.mgilangjanuar.dev.goscele.Models.ListCourseModel;
import com.mgilangjanuar.dev.goscele.Models.ListCurrentCourseModel;
import com.mgilangjanuar.dev.goscele.Services.CourseService;

/**
 * Created by muhammadgilangjanuar on 5/15/17.
 */

public class CoursePresenter {

    Activity activity;
    View view;

    CourseService courseService;

    CurrentCoursesViewAdapter currentCoursesViewAdapter;
    AllCoursesViewAdapter allCoursesViewAdapter;

    ListCourseModel listCourseModel;
    ListCurrentCourseModel listCurrentCourseModel;
    ListCourseModel listSearchCourseModel;

    public static boolean isDataCurrentCoursesViewAdapterChanged;
    public static boolean isDataAllCoursesViewAdapterChanged;

    public interface CourseServicePresenter {
        void setupCourses(View view);
    }

    public CoursePresenter(Activity activity, View view) {
        this.activity = activity;
        this.view = view;
        listCourseModel = new ListCourseModel(activity);
        listCurrentCourseModel = new ListCurrentCourseModel(activity);
        listSearchCourseModel = new ListCourseModel(activity);
        courseService = new CourseService();
    }

    public CurrentCoursesViewAdapter getCurrentCoursesViewAdapter() {
        if (listCurrentCourseModel.getSavedCourseModelList() == null) {
            buildCurrentCoursesIfNull();
        }
        if (this.currentCoursesViewAdapter == null) {
            this.currentCoursesViewAdapter = new CurrentCoursesViewAdapter(activity, this, listCurrentCourseModel.courseModelList);
        }
        return this.currentCoursesViewAdapter;
    }

    public CurrentCoursesViewAdapter getCurrentCoursesViewAdapterForce() {
        buildCurrentCoursesIfNull();
        this.currentCoursesViewAdapter = new CurrentCoursesViewAdapter(activity, this, listCurrentCourseModel.courseModelList);
        return this.currentCoursesViewAdapter;
    }

    public AllCoursesViewAdapter getAllCoursesViewAdapter() {
        if (listCourseModel.getSavedCourseModelList() == null) {
            buildAllCourses();
        }
        if (this.allCoursesViewAdapter == null) {
            this.allCoursesViewAdapter = new AllCoursesViewAdapter(activity, this, listCourseModel.courseModelList);
        }
        return this.allCoursesViewAdapter;
    }

    public AllCoursesViewAdapter getAllCoursesViewAdapterForce() {
        buildAllCourses();
        this.allCoursesViewAdapter = new AllCoursesViewAdapter(activity, this, listCourseModel.courseModelList);
        return this.allCoursesViewAdapter;
    }

    public void buildAllCourses() {
        try {
            listCourseModel.courseModelList = new ArrayList<>();
            for (Map<String, String> course: courseService.getCourses()) {
                CourseModel courseModel = new CourseModel();
                courseModel.url = course.get("url");
                courseModel.name = course.get("name");
                listCourseModel.courseModelList.add(courseModel);
            }
            listCourseModel.save();
        } catch (IOException e) {
            Log.e("CoursePresenter", e.getMessage());
        }
    }

    public void buildCurrentCoursesIfNull() {
        if (listCurrentCourseModel.getSavedCourseModelList() == null) {
            listCurrentCourseModel.courseModelList = new ArrayList<>();
        }
    }

    public void addToCurrent(CourseModel courseModel) {
        buildCurrentCoursesIfNull();
        if (! listCurrentCourseModel.courseModelList.contains(courseModel)) {
            listCurrentCourseModel.courseModelList.add(courseModel);
            listCurrentCourseModel.save();
        }
    }

    public void removeFromCurrentByIndex(int position) {
        buildCurrentCoursesIfNull();
        listCurrentCourseModel.courseModelList.remove(position);
        listCurrentCourseModel.save();
    }

    public boolean alreadyExistInCurrent(CourseModel courseModel) {
        buildCurrentCoursesIfNull();
        return listCurrentCourseModel.courseModelList.contains(courseModel);
    }

    public void clear() {
        listCourseModel.clear();
    }

    public View getView() {
        return  this.view;
    }

    public SearchCourseAdapter buildSearchAdapter(String query) {
        buildSearchCourses(query);
        return new SearchCourseAdapter(activity, listSearchCourseModel.courseModelList);
    }

    public void buildSearchCourses(String query) {
        try {
            listSearchCourseModel.courseModelList = new ArrayList<>();
            for (Map<String, String> course: courseService.searchCourse(query)) {
                CourseModel courseModel = new CourseModel();
                courseModel.url = course.get("url");
                courseModel.name = course.get("name");
                listSearchCourseModel.courseModelList.add(courseModel);
            }
        } catch (IOException e) {
            Log.e("CoursePresenter", e.getMessage());
        }
    }
}
