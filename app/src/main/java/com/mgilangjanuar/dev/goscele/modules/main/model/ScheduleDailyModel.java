package com.mgilangjanuar.dev.goscele.modules.main.model;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.mgilangjanuar.dev.goscele.base.BaseModel;

/**
 * Created by mgilangjanuar (mgilangjanuar@gmail.com)
 *
 * @since 2017
 */

@Table(name = "schedule_daily")
public class ScheduleDailyModel extends BaseModel {

    @Column(name = "desc")
    public String desc;

    @Column(name = "time")
    public String time;

    @Column(name = "scheduleDailyGroupModel")
    public ScheduleDailyGroupModel scheduleDailyGroupModel;
}
