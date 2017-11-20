package com.mgilangjanuar.dev.goscele.modules.main.presenter;

import android.support.v7.widget.RecyclerView;

import com.mgilangjanuar.dev.goscele.modules.main.adapter.ScheduleDeadlineRecyclerViewAdapter;
import com.mgilangjanuar.dev.goscele.modules.main.listener.ScheduleDeadlineDetailListener;
import com.mgilangjanuar.dev.goscele.modules.main.listener.ScheduleDeadlineListener;
import com.mgilangjanuar.dev.goscele.modules.main.model.ScheduleDeadlineModel;
import com.mgilangjanuar.dev.goscele.modules.main.provider.DeadlineProvider;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

    public ScheduleDeadlineRecyclerViewAdapter buildEmptyAdapter(Date date) {
        return new ScheduleDeadlineRecyclerViewAdapter(date, new ArrayList<ScheduleDeadlineModel>());
    }

    public boolean validateAdapterCurrentDate(RecyclerView.Adapter adapter, Date date) {
        if (adapter == null) return false;
        String currentDate = new SimpleDateFormat("MMMM dd, yyyy").format(date.getTime());
        String adapterDate = new SimpleDateFormat("MMMM dd, yyyy").format(((ScheduleDeadlineRecyclerViewAdapter) adapter).date.getTime());
        return currentDate.equals(adapterDate);
    }
}
