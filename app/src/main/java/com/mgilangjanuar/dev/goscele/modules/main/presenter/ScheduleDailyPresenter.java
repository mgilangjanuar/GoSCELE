package com.mgilangjanuar.dev.goscele.modules.main.presenter;

import com.mgilangjanuar.dev.goscele.modules.main.adapter.ScheduleDailyDetailRecyclerViewAdapter;
import com.mgilangjanuar.dev.goscele.modules.main.adapter.ScheduleDailyRecyclerViewAdapter;
import com.mgilangjanuar.dev.goscele.modules.main.listener.ScheduleDailyDetailListener;
import com.mgilangjanuar.dev.goscele.modules.main.listener.ScheduleDailyListener;
import com.mgilangjanuar.dev.goscele.modules.main.model.ScheduleCourseModel;
import com.mgilangjanuar.dev.goscele.modules.main.model.ScheduleDailyGroupModel;
import com.mgilangjanuar.dev.goscele.modules.main.model.ScheduleDailyModel;
import com.mgilangjanuar.dev.goscele.modules.main.provider.DailyProvider;

import java.util.List;

/**
 * Created by mgilangjanuar (mgilangjanuar@gmail.com)
 *
 * @since 2017
 */

public class ScheduleDailyPresenter {

    private ScheduleDailyListener dailyListener;
    private ScheduleDailyDetailListener dailyDetailListener;

    public ScheduleDailyPresenter(ScheduleDailyListener dailyListener, ScheduleDailyDetailListener dailyDetailListener) {
        this.dailyListener = dailyListener;
        this.dailyDetailListener = dailyDetailListener;
    }

    public void runProvider() {
        if (isModelEmpty()) {
            new DailyProvider(dailyListener, dailyDetailListener).create();
        } else {
            List<ScheduleDailyGroupModel> scheduleDailyGroupModelList = new ScheduleDailyGroupModel().find().execute();
            dailyListener.onRetrieveDailySchedule(new ScheduleDailyRecyclerViewAdapter(scheduleDailyGroupModelList));

            List<ScheduleCourseModel> scheduleCourseModelList = new ScheduleCourseModel().find().execute();
            dailyDetailListener.onRetrieveCourseDetail(new ScheduleDailyDetailRecyclerViewAdapter(scheduleCourseModelList));
        }
    }

    public void clearDailySchedule() {
        List<ScheduleDailyModel> scheduleDailyModelList = new ScheduleDailyModel().find().execute();
        for (ScheduleDailyModel dailyModel: scheduleDailyModelList) {
            dailyModel.delete();
        }

        List<ScheduleDailyGroupModel> scheduleDailyGroupModelList = new ScheduleDailyGroupModel().find().execute();
        for (ScheduleDailyGroupModel groupModel: scheduleDailyGroupModelList) {
            groupModel.delete();
        }

        List<ScheduleCourseModel> scheduleCourseModelList = new ScheduleCourseModel().find().execute();
        for (ScheduleCourseModel courseModel: scheduleCourseModelList) {
            courseModel.delete();
        }
    }

    public boolean isModelEmpty() {
        List<ScheduleDailyGroupModel> scheduleDailyGroupModelList = new ScheduleDailyGroupModel().find().execute();
        return scheduleDailyGroupModelList.isEmpty();
    }
}
