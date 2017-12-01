package com.mgilangjanuar.dev.goscele.modules.course.model;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.mgilangjanuar.dev.goscele.base.BaseModel;

import java.util.List;

/**
 * Created by mgilangjanuar (mgilangjanuar@gmail.com)
 *
 * @since 2017
 */

@Table(name = "course_detail")
public class CourseDetailModel extends BaseModel {

    @Column(name = "url")
    public String url;

    @Column(name = "title")
    public String title;

    @Column(name = "summary")
    public String summary;

    public List<CourseDetailInnerModel> courseDetailInnerModels() {
        return getMany(CourseDetailInnerModel.class, "courseDetailModel");
    }
}
