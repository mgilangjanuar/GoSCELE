package com.mgilangjanuar.dev.goscele.modules.forum.list.listener;

import com.mgilangjanuar.dev.goscele.modules.forum.list.adapter.ForumListRecyclerViewAdapter;

/**
 * Created by mgilangjanuar (mgilangjanuar@gmail.com)
 *
 * @since 2017
 */

public interface ForumListListener {

    void onRetrieve(ForumListRecyclerViewAdapter adapter);

    void onGetTitle(String title);

    void onError(String error);
}
