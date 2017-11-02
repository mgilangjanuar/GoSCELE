package com.mgilangjanuar.dev.goscele.modules.forum.detail.listener;

import com.mgilangjanuar.dev.goscele.modules.forum.detail.model.ForumDetailModel;

/**
 * Created by mgilangjanuar (mgilangjanuar@gmail.com)
 *
 * @since 2017
 */

public interface ForumPostListener {

    void onRetrievePost(ForumDetailModel model);

    void onError(String error);
}
