package com.mgilangjanuar.dev.goscele.modules.main.listener;

import com.mgilangjanuar.dev.goscele.modules.main.adapter.ScheduleDailyDetailRecyclerViewAdapter;

/**
 * Created by mgilangjanuar (mgilangjanuar@gmail.com)
 *
 * @since 2017
 */

public interface ScheduleDailyDetailListener {

    void onRetrieveCourseDetail(ScheduleDailyDetailRecyclerViewAdapter adapter);

    void onError(String error);
}
