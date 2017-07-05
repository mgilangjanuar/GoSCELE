package com.mgilangjanuar.dev.goscele.Services;

import com.mgilangjanuar.dev.goscele.Models.ConfigAppModel;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by muhammadgilangjanuar on 5/17/17.
 */

public class ScheduleService {

    private Elements getElements(String tag, long time) throws IOException {
        Document doc;
        doc = Jsoup.connect(ConfigAppModel.urlTo("calendar/view.php?view=day&time=" + time))
                .cookies(AuthService.getCookies())
                .get();
        return doc == null ? null : doc.select(tag);
    }

    public long getCurrentTime() {
        return (new Date()).getTime() / 1000;
    }

    public List<Map<String, String>> getSchedules() throws IOException {
        return this.getSchedules(getCurrentTime());
    }

    public List<Map<String, String>> getSchedules(long time) throws IOException {
        List<Map<String, String>> results = new ArrayList<>();
        final String date = (new SimpleDateFormat("EEEE, dd MMM yyyy")).format(time * 1000);
        for (final Element e : getElements(".event", time)) {
            results.add(new HashMap<String, String>() {{
                put("date", date);
                put("title", e.select(".referer a").text());
                put("url", e.select(".referer a").attr("href"));
                put("course", e.select(".course a").text());
                put("courseUrl", e.select(".course a").attr("href"));
                put("time", e.select(".date").text());
                put("description", e.select(".description").html());
            }});
        }
        return results;
    }
}
