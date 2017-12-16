package com.mgilangjanuar.dev.goscele.modules.course.listener;

import com.mgilangjanuar.dev.goscele.modules.course.adapter.CourseSearchRecyclerViewAdapter;

/**
 * Created by mgilangjanuar (mgilangjanuar@gmail.com)
 *
 * @since 2017
 */

public interface CourseSearchListener {

    void onRetrieve(CourseSearchRecyclerViewAdapter adapter);

    void onError(String error);
}
