package com.mgilangjanuar.dev.sceleapp.Models;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by muhammadgilangjanuar on 5/24/17.
 */

public class ForumDetailModel extends BaseRecord implements BaseRecord.BasicInterface {

    public String url;
    public String title;
    public String author;
    public String date;
    public String content;
    public String deleteUrl;
    public List<ForumCommentModel> forumCommentModelList;

    public ForumDetailModel(Context context) {
        super(context);
        forumCommentModelList = new ArrayList<>();
        tag = "ForumDetailModel";
    }


    @Override
    public void save() {
        Gson gson = new Gson();
        prefsEditor.putString(getAttributeTag("url"), this.url);
        prefsEditor.putString(getAttributeTag("title"), this.title);
        prefsEditor.putString(getAttributeTag("author"), this.author);
        prefsEditor.putString(getAttributeTag("date"), this.date);
        prefsEditor.putString(getAttributeTag("content"), this.content);
        prefsEditor.putString(getAttributeTag("deleteUrl"), this.deleteUrl);
        prefsEditor.putString(getAttributeTag("forumCommentModelList"), gson.toJson(this.forumCommentModelList));
        prefsEditor.commit();
    }

    @Override
    public void clear() {
        prefsEditor.putString(getAttributeTag("url"), null);
        prefsEditor.putString(getAttributeTag("title"), null);
        prefsEditor.putString(getAttributeTag("author"), null);
        prefsEditor.putString(getAttributeTag("date"), null);
        prefsEditor.putString(getAttributeTag("content"), null);
        prefsEditor.putString(getAttributeTag("deleteUrl"), null);
        prefsEditor.putString(getAttributeTag("forumCommentModelList"), null);
        prefsEditor.commit();
    }

    public String getSavedUrl() {
        url = sharedPreferences.getString(getAttributeTag("url"), null);
        return url;
    }

    public String getSavedTitle() {
        title = sharedPreferences.getString(getAttributeTag("title"), null);
        return title;
    }

    public String getSavedAuthor() {
        author = sharedPreferences.getString(getAttributeTag("author"), null);
        return author;
    }

    public String getSavedDate() {
        date = sharedPreferences.getString(getAttributeTag("date"), null);
        return date;
    }

    public String getSavedContent() {
        content = sharedPreferences.getString(getAttributeTag("content"), null);
        return content;
    }

    public String getSavedDeleteUrl() {
        deleteUrl = sharedPreferences.getString(getAttributeTag("deleteUrl"), null);
        return deleteUrl;
    }

    public List<ForumCommentModel> getSavedForumCommentModelList() {
        Gson gson = new Gson();
        forumCommentModelList = gson.fromJson(sharedPreferences.getString(getAttributeTag("forumCommentModelList"), null), new TypeToken<ArrayList<ForumCommentModel>>() {}.getType());
        return forumCommentModelList;
    }
}
