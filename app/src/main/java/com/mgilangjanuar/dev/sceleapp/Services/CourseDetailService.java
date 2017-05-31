package com.mgilangjanuar.dev.sceleapp.Services;

import android.util.Log;

import com.google.gson.Gson;
import com.mgilangjanuar.dev.sceleapp.Models.ConfigAppModel;

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
 * Created by muhammadgilangjanuar on 5/21/17.
 */

public class CourseDetailService {

    private Elements getDetailsHelper(String tag, String url) throws IOException {
        Document doc;
        doc = Jsoup.connect(url)
                .cookies(AuthService.getCookies())
                .get();
        return doc == null ? null : doc.select(tag);
    }

    public List<Map<String, String>> getCourseDetails(final String url) throws IOException {
        List<Map<String, String>> results = new ArrayList<>();
        final Gson gson = new Gson();
        for (final Element e: getDetailsHelper(".section.main", url)) {

            final List<Map<String, String>> inner = new ArrayList<>();
            for (final Element f: e.select("ul.section > li")) {
                Map<String, String> map = new HashMap<>();
                map.put("url", f.select(".activityinstance a").attr("href"));
                map.put("title", f.select(".activityinstance a .instancename").text());
                map.put("comment", f.select(".contentafterlink").html());
                if (f.select(".contentafterlink").text().equals("")) {
                    map.put("comment", f.select(".contentwithoutlink").html());
                }
                inner.add(map);
            }

            results.add(new HashMap<String, String>() {{
                put("url", url);
                put("title", e.select(".sectionname").text());
                put("summary", e.select(".summary").html());
                put("innerCoursePostModelList", gson.toJson(inner));
            }});
        }
        return results;
    }

    public String getCourseName(String url) throws IOException {
        return getDetailsHelper("title", url).text().substring(8);
    }

    public List<Map<String, String>> getEvents(final String url) throws IOException {
        final List<Map<String, String>> results = new ArrayList<>();
        for (final Element e: getDetailsHelper(".block_calendar_upcoming .content .event", url)) {
            results.add(new HashMap<String, String>() {{
                put("url", e.select("a").get(0).attr("href"));
                put("title", e.select("a").get(0).text());
                put("info", e.select(".date").text());
            }});
        }
        return results;
    }

    public List<Map<String, String>> getNews(final String url) throws IOException {
        final List<Map<String, String>> results = new ArrayList<>();
        for (final Element e: getDetailsHelper(".block_news_items .content .post", url)) {
            results.add(new HashMap<String, String>() {{
                put("url", e.select(".info a").attr("href"));
                put("title", e.select(".info").text());
                put("info", e.select(".head").text());
            }});
        }
        return results;
    }

}
