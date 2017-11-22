package com.mgilangjanuar.dev.goscele.modules.main.listener;

import com.mgilangjanuar.dev.goscele.modules.main.model.ScheduleDeadlineDaysModel;

/**
 * Created by mgilangjanuar (mgilangjanuar@gmail.com)
 *
 * @since 2017
 */

public interface ScheduleDeadlineListener {

    void onRetrieveDeadlineDays(ScheduleDeadlineDaysModel model);

    void onError(String error);
}
