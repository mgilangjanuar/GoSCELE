package com.mgilangjanuar.dev.goscele.Presenters;

import android.app.Activity;
import android.util.Log;

import com.mgilangjanuar.dev.goscele.Adapters.ForumAdapter;
import com.mgilangjanuar.dev.goscele.BaseActivity;
import com.mgilangjanuar.dev.goscele.Models.ForumModel;
import com.mgilangjanuar.dev.goscele.Models.ListForumModel;
import com.mgilangjanuar.dev.goscele.R;
import com.mgilangjanuar.dev.goscele.Services.ForumService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by muhammadgilangjanuar on 5/31/17.
 */

public class ForumPresenter {
    Activity activity;

    ListForumModel listForumModel;
    ListForumModel listForumSearchModel;
    ForumService forumService;

    public String url;

    public interface ForumServicePresenter {
        void setupForum();
    }

    public ForumPresenter(Activity activity, String url) {
        this.activity = activity;
        this.url = url;
        listForumModel = new ListForumModel(activity);
        listForumSearchModel = new ListForumModel(activity);
        forumService = new ForumService(url);
    }

    public ForumAdapter buildAdapter() {
        if (listForumModel.getSavedUrl() == null
                || !listForumModel.getSavedUrl().equals(url)
                || listForumModel.getSavedForumModelList() == null) {
            buildModel();
        }
        return new ForumAdapter(activity, listForumModel.getSavedForumModelList());
    }

    public void buildModel() {
        try {
            listForumModel.clear();
            listForumModel.url = url;
            listForumModel.forumModelList = new ArrayList<>();
            for (Map<String, String> e : forumService.getForums()) {
                ForumModel model = new ForumModel();
                model.url = e.get("url");
                model.title = e.get("title");
                model.author = e.get("author");
                model.lastUpdate = e.get("lastUpdate");
                model.repliesNumber = e.get("repliesNumber");
                listForumModel.forumModelList.add(model);
            }
            listForumModel.save();
        } catch (IOException e) {
            Log.e("ForumPresenter", e.getMessage());
        }
    }

    public String getTitle() {
        try {
            return forumService.getTitle();
        } catch (IOException e) {
            Log.e("ForumPresenter", e.getMessage());
        }
        return activity.getResources().getString(R.string.app_name);
    }

    public void clear() {
        listForumModel.clear();
    }

    public boolean isCanSendNews() {
        try {
            return forumService.isCanPostForum();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void sendNews(String title, String message) {
        try {
            forumService.postForum(title, message);
        } catch (IOException e) {
            Log.e("ForumPresenter", e.getMessage());
        }
    }

    public ForumAdapter buildSearchAdapter(String keyword) {
        buildSearchModel(keyword);
        return new ForumAdapter(activity, listForumSearchModel.forumModelList);
    }

    public void buildSearchModel(String keyword) {
        try {
            listForumSearchModel.forumModelList = new ArrayList<>();
            for (Map<String, String> e : forumService.searchForum(keyword)) {
                ForumModel model = new ForumModel();
                model.url = e.get("url");
                model.title = e.get("title");
                model.author = e.get("author");
                model.lastUpdate = e.get("lastUpdate");
                model.repliesNumber = e.get("repliesNumber");
                listForumSearchModel.forumModelList.add(model);
            }

            String count = forumService.searchForumInfo(keyword).get("count");
            if (!count.equals("") && Integer.parseInt(count) > 50) {
                ((BaseActivity) activity).showToast("Please try to be more specific");
            }
        } catch (IOException e) {
            Log.e("ForumPresenter", e.getMessage());
        }
    }
}
