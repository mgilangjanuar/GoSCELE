package com.mgilangjanuar.dev.goscele.modules.forum.list.presenter;

import android.util.Log;

import com.mgilangjanuar.dev.goscele.modules.forum.list.adapter.ForumListRecyclerViewAdapter;
import com.mgilangjanuar.dev.goscele.modules.forum.list.listener.ForumListListener;
import com.mgilangjanuar.dev.goscele.modules.forum.list.model.ForumModel;
import com.mgilangjanuar.dev.goscele.modules.forum.list.provider.ForumListProvider;

import java.util.List;

/**
 * Created by mgilangjanuar (mgilangjanuar@gmail.com)
 *
 * @since 2017
 */

public class ForumListPresenter {

    private ForumListListener listener;

    public ForumListPresenter(ForumListListener listener) {
        this.listener = listener;
    }

    public void retrieveForums(String url, boolean force) {
        List<ForumModel> models = new ForumModel().find().where("listUrl = ?", url).execute();
        if (models.isEmpty() || force) {
            new ForumListProvider(url, listener).run();
        } else {
            listener.onGetTitle(models.get(0).forumTitle);
            listener.onRetrieve(new ForumListRecyclerViewAdapter(models));
        }
    }
}
