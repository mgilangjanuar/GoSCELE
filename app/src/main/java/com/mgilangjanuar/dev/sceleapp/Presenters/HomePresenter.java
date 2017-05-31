package com.mgilangjanuar.dev.sceleapp.Presenters;

import android.app.Activity;
import android.util.Log;
import android.view.View;

import com.mgilangjanuar.dev.sceleapp.Adapters.HomePostAdapter;
import com.mgilangjanuar.dev.sceleapp.Models.HomePostModel;
import com.mgilangjanuar.dev.sceleapp.Models.ListHomePostModel;
import com.mgilangjanuar.dev.sceleapp.Services.HomePostService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by muhammadgilangjanuar on 5/20/17.
 */

public class HomePresenter {

    Activity activity;
    View view;

    HomePostService homePostService;
    ListHomePostModel listHomePostModel;

    public interface HomeServicePresenter {
        void setupHome(View view);
    }

    public HomePresenter(Activity activity, View view) {
        this.activity = activity;
        this.view = view;
        this.homePostService = new HomePostService();
        this.listHomePostModel = new ListHomePostModel(activity);
    }

    public HomePostAdapter buildAdapter() {
        if (listHomePostModel.getSavedHomePostModelList() == null) {
            buildModel();
        }
        return new HomePostAdapter(activity, listHomePostModel.homePostModelList);
    }

    public void buildModel() {
        try {
            listHomePostModel.clear();
            listHomePostModel.homePostModelList = new ArrayList<>();
            for (Map<String, String> e: homePostService.getPosts()) {
                HomePostModel model = new HomePostModel();
                model.url = e.get("url");
                model.title = e.get("title");
                model.author = e.get("author");
                model.date = e.get("date");
                model.content = e.get("content");
                listHomePostModel.homePostModelList.add(model);
            }
            listHomePostModel.save();
        } catch (IOException e) {
            Log.e("HomePresenter", e.getMessage());
        }
    }

    public void clear() {
        this.listHomePostModel.clear();
    }
}
