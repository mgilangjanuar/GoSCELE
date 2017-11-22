package com.mgilangjanuar.dev.goscele.modules.main.model;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.mgilangjanuar.dev.goscele.base.BaseModel;

import java.util.List;

/**
 * Created by mgilangjanuar (mgilangjanuar@gmail.com)
 *
 * @since 2017
 */

@Table(name = "schedule_daily_group")
public class ScheduleDailyGroupModel extends BaseModel {

    @Column(name = "day")
    public String day;

    public List<ScheduleDailyModel> scheduleDailyModels() {
        return getMany(ScheduleDailyModel.class, "scheduleDailyGroupModel");
    }
}
