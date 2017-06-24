package com.mgilangjanuar.dev.goscele.Services;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mgilangjanuar.dev.goscele.Models.ConfigAppModel;

/**
 * Created by muhammadgilangjanuar on 5/15/17.
 */

public class CourseService {

    private Elements getCoursesHelper() throws IOException {
        Document doc;
        doc = Jsoup.connect(ConfigAppModel.BASE_URL)
                .cookies(AuthService.getCookies())
                .get();

        return doc == null ? null : doc.select(".block_course_list.block.list_block .content .column.c1");
    }

    public List<Map<String, String>> getCourses() throws IOException {
        List<Map<String, String>> results = new ArrayList<>();
        for (final Element course: getCoursesHelper()) {
            results.add(new HashMap<String, String>(){{
                put("url", course.select("a").attr("href"));
                put("name", course.text());
            }});
        }
        return results;
    }

}
