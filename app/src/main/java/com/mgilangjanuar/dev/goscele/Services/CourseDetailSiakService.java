package com.mgilangjanuar.dev.goscele.Services;

import android.util.Log;

import com.mgilangjanuar.dev.goscele.Models.ConfigAppModel;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by gilang on 8/5/17.
 */

public class CourseDetailSiakService {

    private Elements getElements(String tag) throws IOException {
        Document doc;
        doc = Jsoup.connect(ConfigAppModel.urlTo("main/CoursePlan/CoursePlanViewClass", true))
                .cookies(AuthSiakService.cookies)
                .get();
        return doc == null ? null : doc.select(tag);
    }

    public List<Map<String, String>> getCourseDetails() throws IOException {
        List<Map<String, String>> results = new ArrayList<>();
        for (Element e: getElements("table.box > tbody > tr")) {
            if (e.className() != null && ("alt".equals(e.className()) || "x".equals(e.className()))) {
                results.add(new HashMap<String, String>() {{
                    put("code", e.select("td:eq(1)").text());
                    put("courseName", e.select("td:eq(2) > a").text());
                    put("courseClass", e.select("td:eq(2) > span").text());
                    put("credits", e.select("td:eq(3)").text());
                    put("lecturer", e.select("td:eq(7)").text());
                }});
            }
        }
        return results;
    }
}
