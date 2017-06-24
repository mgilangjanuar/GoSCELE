package com.mgilangjanuar.dev.goscele.Presenters;

import android.app.Activity;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mgilangjanuar.dev.goscele.Adapters.CourseDetailAdapter;
import com.mgilangjanuar.dev.goscele.Adapters.CourseDetailEventAdapter;
import com.mgilangjanuar.dev.goscele.Adapters.CourseDetailNewsAdapter;
import com.mgilangjanuar.dev.goscele.Models.CourseEventModel;
import com.mgilangjanuar.dev.goscele.Models.CourseModel;
import com.mgilangjanuar.dev.goscele.Models.CourseNewsModel;
import com.mgilangjanuar.dev.goscele.Models.CoursePostModel;
import com.mgilangjanuar.dev.goscele.Models.InnerCoursePostModel;
import com.mgilangjanuar.dev.goscele.Models.ListCourseEventModel;
import com.mgilangjanuar.dev.goscele.Models.ListCourseNewsModel;
import com.mgilangjanuar.dev.goscele.Models.ListCoursePostModel;
import com.mgilangjanuar.dev.goscele.Services.CourseDetailService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by muhammadgilangjanuar on 5/21/17.
 */

public class CourseDetailPresenter {

    Activity activity;

    ListCoursePostModel listCoursePostModel;
    ListCourseEventModel listCourseEventModel;
    ListCourseNewsModel listCourseNewsModel;

    CourseDetailAdapter courseDetailAdapter;
    CourseDetailEventAdapter courseDetailEventAdapter;
    CourseDetailNewsAdapter courseDetailNewsAdapter;

    CourseDetailService courseDetailService;

    CourseModel courseModel;

    public String url;

    public interface CourseDetailServicePresenter {
        void setupCourseDetail(View view);
    }

    public CourseDetailPresenter(Activity activity, String url) {
        this.activity = activity;
        this.url = url;
        listCoursePostModel = new ListCoursePostModel(activity);
        listCourseEventModel = new ListCourseEventModel(activity);
        listCourseNewsModel = new ListCourseNewsModel(activity);
        courseDetailService = new CourseDetailService();
    }

    public CourseDetailAdapter buildDashboardAdapter() {
        if (listCoursePostModel.getSavedCourseModel() == null
                || ! listCoursePostModel.getSavedCourseModel().url.equals(url)
                || listCoursePostModel.getSavedCoursePostModels() == null) {
            buildDashboardModel();
        }
        if (courseDetailAdapter == null) {
            courseDetailAdapter = new CourseDetailAdapter(activity, listCoursePostModel.getSavedCoursePostModels());
        }
        return courseDetailAdapter;
    }

    private void buildDashboardModel() {
        try {
            listCoursePostModel.clear();
            listCoursePostModel.courseModel = getCourseModel();
            Gson gson = new Gson();
            listCoursePostModel.coursePostModelList = new ArrayList<>();
            for (Map<String, String> e: courseDetailService.getCourseDetails(url)) {
                CoursePostModel model = new CoursePostModel();
                model.url = e.get("url");
                model.title = e.get("title");
                model.summary = e.get("summary");
                model.innerCoursePostModelList = gson.fromJson(e.get("innerCoursePostModelList"), new TypeToken<ArrayList<InnerCoursePostModel>>() {}.getType());
                if (! model.summary.equals("") || ! model.innerCoursePostModelList.isEmpty()) {
                    listCoursePostModel.coursePostModelList.add(model);
                }
            }
            listCoursePostModel.save();
        } catch (IOException e) {
            Log.e("CourseDetailPresenter", e.getMessage());
        }
    }

    public void clearDashboard() {
        listCoursePostModel.clear();
    }

    public CourseDetailEventAdapter buildEventAdapter() {
        if (listCourseEventModel.getSavedCourseEventModelList() == null
                || ! listCourseEventModel.getSavedCourseModel().url.equals(url)) {
            buildEventModel();
        }
        if (courseDetailEventAdapter == null) {
            courseDetailEventAdapter = new CourseDetailEventAdapter(activity, listCourseEventModel.getSavedCourseEventModelList());
        }
        return courseDetailEventAdapter;
    }

    public void buildEventModel() {
        try {
            listCourseEventModel.clear();
            listCourseEventModel.courseModel = getCourseModel();
            listCourseEventModel.courseEventModelList = new ArrayList<>();
            for (Map<String, String> e: courseDetailService.getEvents(url)) {
                CourseEventModel model = new CourseEventModel();
                model.url = e.get("url");
                model.title = e.get("title");
                model.info = e.get("info");
                listCourseEventModel.courseEventModelList.add(model);
            }
            listCourseEventModel.save();
        } catch (IOException e) {
            Log.e("CourseDetailPresenter", e.getMessage());
        }
    }

    public void clearEvent() {
        listCourseEventModel.clear();
    }

    public CourseModel getCourseModel() {
        if (courseModel != null) return courseModel;

        courseModel = new CourseModel();
        try {
            courseModel.url = url;
            courseModel.name = courseDetailService.getCourseName(url);
        } catch (IOException e) {
            Log.e("CourseDetailPresenter", e.getMessage() + " ");
        }
        return courseModel;
    }

    public CourseDetailNewsAdapter buildNewsAdapter() {
        if (listCourseNewsModel.getSavedCourseNewsModelList() == null
                || ! listCourseNewsModel.getSavedCourseModel().url.equals(url)) {
            buildNewsModel();
        }
        if (courseDetailNewsAdapter == null) {
            courseDetailNewsAdapter = new CourseDetailNewsAdapter(activity, listCourseNewsModel.getSavedCourseNewsModelList());
        }
        return courseDetailNewsAdapter;
    }

    public void buildNewsModel() {
        try {
            listCourseNewsModel.clear();
            listCourseNewsModel.courseModel = getCourseModel();
            listCourseNewsModel.courseNewsModelList = new ArrayList<>();
            for (Map<String, String> e: courseDetailService.getNews(url)) {
                CourseNewsModel model = new CourseNewsModel();
                model.url = e.get("url");
                model.title = e.get("title");
                model.info = e.get("info");
                listCourseNewsModel.courseNewsModelList.add(model);
            }
            listCourseNewsModel.save();
        } catch (IOException e) {
            Log.e("CourseDetailPresenter", e.getMessage());
        }
    }

    public void clearNews() {
        listCourseNewsModel.clear();
    }
}
