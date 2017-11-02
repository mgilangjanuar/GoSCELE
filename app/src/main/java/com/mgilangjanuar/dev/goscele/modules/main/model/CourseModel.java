package com.mgilangjanuar.dev.goscele.modules.main.model;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.mgilangjanuar.dev.goscele.base.BaseModel;

/**
 * Created by mgilangjanuar (mgilangjanuar@gmail.com)
 *
 * @since 2017
 */

@Table(name = "course")
public class CourseModel extends BaseModel {

    @Column(name = "url")
    public String url;

    @Column(name = "name")
    public String name;

    @Column(name = "is_current")
    public boolean isCurrent;
}
