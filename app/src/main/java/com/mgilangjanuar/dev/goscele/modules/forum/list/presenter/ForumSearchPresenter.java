package com.mgilangjanuar.dev.goscele.modules.forum.list.presenter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import com.mgilangjanuar.dev.goscele.modules.forum.list.adapter.ForumListRecyclerViewAdapter;
import com.mgilangjanuar.dev.goscele.modules.forum.list.listener.ForumListListener;
import com.mgilangjanuar.dev.goscele.modules.forum.list.model.ForumModel;
import com.mgilangjanuar.dev.goscele.modules.forum.list.provider.ForumSearchProvider;

import java.util.ArrayList;

/**
 * Created by mgilangjanuar (mgilangjanuar@gmail.com)
 *
 * @since 2017
 */

public class ForumSearchPresenter {

    private ForumListListener listener;

    public ForumSearchPresenter(ForumListListener listener) {
        this.listener = listener;
    }

    private boolean validate(String query) {
        return !TextUtils.isEmpty(query);
    }

    public void search(String query) {
        if (validate(query)) {
            new ForumSearchProvider(listener, query).run();
        } else {
            listener.onError("Please fill that required field");
        }
    }

    public ForumListRecyclerViewAdapter buildEmptyAdapter() {
        return new ForumListRecyclerViewAdapter(new ArrayList<ForumModel>());
    }
}
