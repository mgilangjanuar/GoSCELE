package com.mgilangjanuar.dev.sceleapp.Presenters;

import android.app.Activity;
import android.util.Log;
import android.view.View;

import com.mgilangjanuar.dev.sceleapp.Adapters.ForumDetailCommentAdapter;
import com.mgilangjanuar.dev.sceleapp.Models.ForumCommentModel;
import com.mgilangjanuar.dev.sceleapp.Models.ForumDetailModel;
import com.mgilangjanuar.dev.sceleapp.Services.ForumService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by muhammadgilangjanuar on 5/25/17.
 */

public class ForumDetailPresenter {
    Activity activity;
    String url;

    ForumDetailModel forumDetailModel;
    ForumService forumService;

    public interface ForumDetailServicePresenter {
        void setupForumDetail(View view);
    }

    public ForumDetailPresenter(Activity activity, String url) {
        this.activity = activity;
        this.url = url;
        forumDetailModel = new ForumDetailModel(activity);
        forumService = new ForumService(url);
    }

    public ForumDetailCommentAdapter buildCommentAdapter() {
        if (forumDetailModel.getSavedUrl() == null
                || ! forumDetailModel.getSavedUrl().equals(url)
                || forumDetailModel.getSavedForumCommentModelList() == null) {
            buildModel();
        }
        return new ForumDetailCommentAdapter(activity, forumDetailModel.getSavedForumCommentModelList(), this);
    }

    public void buildModel() {
        try {
            clear();
            Map<String, Object> data = forumService.getForumDetails();
            forumDetailModel.url = (String) data.get("url");
            forumDetailModel.title = (String) data.get("title");
            forumDetailModel.author = (String) data.get("author");
            forumDetailModel.date = (String) data.get("date");
            forumDetailModel.content = (String) data.get("content");

            forumDetailModel.forumCommentModelList = new ArrayList<>();
            for (Map<String, String> e: (List<Map<String, String>>) data.get("forumCommentModelList")) {
                ForumCommentModel forumCommentModel = new ForumCommentModel();
                forumCommentModel.author = e.get("author");
                forumCommentModel.date = e.get("date");
                forumCommentModel.content = e.get("content");
                forumCommentModel.deleteUrl = e.get("deleteUrl");
                forumDetailModel.forumCommentModelList.add(forumCommentModel);
            }
            forumDetailModel.save();
        } catch (IOException e) {
            Log.e("ForumDetailPresenter", e.getMessage());
        }
    }

    public void clear() {
        forumDetailModel.clear();
    }

    public ForumDetailModel getForumDetailModel() {
        return forumDetailModel;
    }

    public void sendComment(String message) {
        try {
            forumService.postForumComment(message);
        } catch (IOException e) {
            Log.e("ForumDetailPresenter", e.getMessage());
        }
    }

    public void deleteComment(ForumCommentModel model) {
        try {
            forumService.deleteForumComment(model.deleteUrl);
        } catch (IOException e) {
            Log.e("ForumDetailPresenter", e.getMessage());
        }
    }
}
