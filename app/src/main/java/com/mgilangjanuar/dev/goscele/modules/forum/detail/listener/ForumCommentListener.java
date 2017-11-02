package com.mgilangjanuar.dev.goscele.modules.forum.detail.listener;

import com.mgilangjanuar.dev.goscele.modules.forum.detail.adapter.CommentRecyclerViewAdapter;

/**
 * Created by mgilangjanuar (mgilangjanuar@gmail.com)
 *
 * @since 2017
 */

public interface ForumCommentListener {

    void onRetrieveComments(CommentRecyclerViewAdapter adapter);

    void onError(String error);
}
