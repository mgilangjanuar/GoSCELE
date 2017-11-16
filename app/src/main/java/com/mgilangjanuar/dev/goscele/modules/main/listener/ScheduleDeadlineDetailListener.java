package com.mgilangjanuar.dev.goscele.modules.main.listener;

import com.mgilangjanuar.dev.goscele.modules.main.adapter.ScheduleDeadlineRecyclerViewAdapter;

/**
 * Created by mgilangjanuar (mgilangjanuar@gmail.com)
 *
 * @since 2017
 */

public interface ScheduleDeadlineDetailListener {

    void onRetrieveDeadlineDetail(ScheduleDeadlineRecyclerViewAdapter adapter);

    void onError(String error);
}
