package com.mgilangjanuar.dev.goscele.modules.main.model;


import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mgilangjanuar.dev.goscele.base.BaseModel;

import java.util.List;

/**
 * Created by mgilangjanuar (mgilangjanuar@gmail.com)
 *
 * @since 2017
 */

@Table(name = "schedule_deadline_days")
public class ScheduleDeadlineDaysModel extends BaseModel {

    @Column(name = "month")
    public int month;

    @Column(name = "days")
    public String days;

    public List<Integer> getDays() {
        Gson gson = new Gson();
        return gson.fromJson(days, new TypeToken<List<Integer>>(){}.getType());
    }

    public void setDays(List<Integer> days) {
        Gson gson = new Gson();
        this.days = gson.toJson(days);
    }
}
