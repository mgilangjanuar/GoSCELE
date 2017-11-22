package com.mgilangjanuar.dev.goscele.modules.main.listener;

import com.mgilangjanuar.dev.goscele.modules.main.adapter.ScheduleDailyRecyclerViewAdapter;

/**
 * Created by mgilangjanuar (mgilangjanuar@gmail.com)
 *
 * @since 2017
 */

public interface ScheduleDailyListener {

    void onRetrieveDailySchedule(ScheduleDailyRecyclerViewAdapter adapter);

    void onError(String error);
}
