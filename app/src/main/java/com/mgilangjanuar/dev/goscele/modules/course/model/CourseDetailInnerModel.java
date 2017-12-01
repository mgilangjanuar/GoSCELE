package com.mgilangjanuar.dev.goscele.modules.course.model;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.mgilangjanuar.dev.goscele.base.BaseModel;

/**
 * Created by mgilangjanuar (mgilangjanuar@gmail.com)
 *
 * @since 2017
 */

@Table(name = "course_detail_inner")
public class CourseDetailInnerModel extends BaseModel {

    @Column(name = "url")
    public String url;

    @Column(name = "title")
    public String title;

    @Column(name = "comment")
    public String comment;

    @Column(name = "courseDetailModel")
    public CourseDetailModel courseDetailModel;
}
