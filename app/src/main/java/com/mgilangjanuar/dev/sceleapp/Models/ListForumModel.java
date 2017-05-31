package com.mgilangjanuar.dev.sceleapp.Models;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by muhammadgilangjanuar on 5/31/17.
 */

public class ListForumModel extends BaseRecord implements BaseRecord.BasicInterface {

    public String url;
    public List<ForumModel> forumModelList;

    public ListForumModel(Context context) {
        super(context);
        forumModelList = new ArrayList<>();
        tag = "ListForumModel";
    }

    @Override
    public void save() {
        Gson gson = new Gson();
        prefsEditor.putString(getAttributeTag("forumModelList"), gson.toJson(this.forumModelList));
        prefsEditor.putString(getAttributeTag("url"), this.url);
        prefsEditor.commit();
    }

    @Override
    public void clear() {
        prefsEditor.putString(getAttributeTag("forumModelList"), null);
        prefsEditor.putString(getAttributeTag("url"), null);
        prefsEditor.commit();
    }

    public List<ForumModel> getSavedForumModelList() {
        Gson gson = new Gson();
        forumModelList =  gson.fromJson(sharedPreferences.getString(getAttributeTag("forumModelList"), null), new TypeToken<ArrayList<ForumModel>>() {}.getType());
        return forumModelList;
    }

    public String getSavedUrl() {
        url = sharedPreferences.getString(getAttributeTag("url"), null);
        return url;
    }
}
