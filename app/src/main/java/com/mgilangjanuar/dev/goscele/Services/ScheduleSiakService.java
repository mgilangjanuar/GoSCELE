package com.mgilangjanuar.dev.goscele.Services;

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
 * Created by mjanuar on 8/1/17.
 */

public class ScheduleSiakService {

    private Elements getElements(String tag) throws IOException {
        Document doc;
        doc = Jsoup.connect(ConfigAppModel.urlTo("main/CoursePlan/CoursePlanViewSchedule", true))
                .cookies(AuthSiakService.cookies)
                .get();
        return doc == null ? null : doc.select(tag);
    }

    public List<List<Map<String, String>>> getSchedule() throws IOException {
        List<List<Map<String, String>>> finalResults = new ArrayList<>();
        int count = 0;
        for (Element e1 : getElements(".box.cal > tbody > tr:eq(1) > td")) {
            if (count != 0 && count != 7) {
                List<Map<String, String>> result = new ArrayList<>();
                if (e1.select("div.sch") != null) {
                    for (Element e2 : e1.select("div.sch")) {
                        result.add(new HashMap<String, String>() {{
                            put("time", e2.select(".sch-inner > h3").text());
                            put("desc", e2.select(".sch-inner > .desc > p").text());
                        }});
                    }
                }
                finalResults.add(result);
            }
            count++;
        }
        return finalResults;
    }

}
