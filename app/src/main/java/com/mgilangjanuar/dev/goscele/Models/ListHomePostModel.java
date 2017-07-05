package com.mgilangjanuar.dev.goscele.Models;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by muhammadgilangjanuar on 5/14/17.
 */

public class ListHomePostModel extends BaseRecord implements BaseRecord.BasicInterface {

    public List<HomePostModel> homePostModelList;

    public ListHomePostModel(Context context) {
        super(context);
        homePostModelList = new ArrayList<>();
        tag = "ListHomePostModel";
    }

    @Override
    public void save() {
        Gson gson = new Gson();
        prefsEditor.putString(getAttributeTag("homePostModelList"), gson.toJson(this.homePostModelList));
        prefsEditor.commit();
    }

    @Override
    public void clear() {
        prefsEditor.putString(getAttributeTag("homePostModelList"), null);
        prefsEditor.commit();
    }

    public List<HomePostModel> getSavedHomePostModelList() {
        Gson gson = new Gson();
        homePostModelList = gson.fromJson(sharedPreferences.getString(getAttributeTag("homePostModelList"), null), new TypeToken<ArrayList<HomePostModel>>() {
        }.getType());
        return homePostModelList;
    }
}
