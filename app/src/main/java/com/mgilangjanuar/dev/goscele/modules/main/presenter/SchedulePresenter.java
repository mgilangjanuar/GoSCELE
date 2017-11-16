package com.mgilangjanuar.dev.goscele.modules.main.presenter;

import com.mgilangjanuar.dev.goscele.modules.main.listener.ScheduleDeadlineDetailListener;
import com.mgilangjanuar.dev.goscele.modules.main.listener.ScheduleDeadlineListener;
import com.mgilangjanuar.dev.goscele.modules.main.provider.DeadlineProvider;

import java.util.Date;

/**
 * Created by mgilangjanuar (mgilangjanuar@gmail.com)
 *
 * @since 2017
 */

public class SchedulePresenter {

    private ScheduleDeadlineListener deadlineListener;
    private ScheduleDeadlineDetailListener deadlineDetailListener;

    public SchedulePresenter(ScheduleDeadlineListener deadlineListener, ScheduleDeadlineDetailListener deadlineDetailListener) {
        this.deadlineListener = deadlineListener;
        this.deadlineDetailListener = deadlineDetailListener;
    }

    public void getDeadlineDays(long time) {
        new DeadlineProvider.MonthView(time, deadlineListener).run();
    }

    public void getDeadlineDetail(long time) {
        new DeadlineProvider.DayView(time, deadlineDetailListener).run();
    }
}
