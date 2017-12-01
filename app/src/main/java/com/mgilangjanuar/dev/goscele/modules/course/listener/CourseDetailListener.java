package com.mgilangjanuar.dev.goscele.modules.course.listener;

import com.mgilangjanuar.dev.goscele.modules.course.adapter.CourseDetailRecyclerViewAdapter;

/**
 * Created by mgilangjanuar (mgilangjanuar@gmail.com)
 *
 * @since 2017
 */

public interface CourseDetailListener {
    void onRetrieveDetail(CourseDetailRecyclerViewAdapter adapter);

    void onError(String error);

}
