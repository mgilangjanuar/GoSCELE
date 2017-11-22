package com.mgilangjanuar.dev.goscele.modules.main.model;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.mgilangjanuar.dev.goscele.base.BaseModel;

/**
 * Created by mgilangjanuar (mgilangjanuar@gmail.com)
 *
 * @since 2017
 */

@Table(name = "schedule_course")
public class ScheduleCourseModel extends BaseModel {

    @Column(name = "code")
    public String code;

    @Column(name = "course_name")
    public String courseName;

    @Column(name = "course_class")
    public String courseClass;

    @Column(name = "credits")
    public String credits;

    @Column(name = "lecturer")
    public String lecturer;
}
