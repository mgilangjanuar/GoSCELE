package com.mgilangjanuar.dev.goscele.Models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by muhammadgilangjanuar on 5/21/17.
 */

public class CoursePostModel {
    public String url;
    public String title;
    public String summary;
    public List<InnerCoursePostModel> innerCoursePostModelList;

    public CoursePostModel() {
        innerCoursePostModelList = new ArrayList<>();
    }
}
