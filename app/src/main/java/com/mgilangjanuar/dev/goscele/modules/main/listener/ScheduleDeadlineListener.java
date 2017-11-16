package com.mgilangjanuar.dev.goscele.modules.main.listener;

import java.util.List;

/**
 * Created by mgilangjanuar (mgilangjanuar@gmail.com)
 *
 * @since 2017
 */

public interface ScheduleDeadlineListener {

    void onRetrieveDeadlineDays(List<Integer> days);

    void onError(String error);
}
