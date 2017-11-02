package com.mgilangjanuar.dev.goscele.modules.main.listener;

import com.mgilangjanuar.dev.goscele.modules.main.adapter.CourseRecyclerViewAdapter;

/**
 * Created by mgilangjanuar (mgilangjanuar@gmail.com)
 *
 * @since 2017
 */

public interface CourseAllListener {
    void onRetrieve(CourseRecyclerViewAdapter adapter);
    void onError(String error);
}
