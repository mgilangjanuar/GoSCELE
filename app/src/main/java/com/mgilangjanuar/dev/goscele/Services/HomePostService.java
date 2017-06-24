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
 * Created by muhammadgilangjanuar on 5/14/17.
 */

public class HomePostService {

    private Elements getElements(String tag) throws IOException {
        Document doc;
        doc = Jsoup.connect(ConfigAppModel.BASE_URL)
                .cookies(AuthService.getCookies())
                .get();
        return doc == null ? null : doc.select(tag);
    }

    public List<Map<String, String>> getPosts() throws IOException {
        List<Map<String, String>> results = new ArrayList<>();
        for (final Element e: getElements(".forumpost")) {
            results.add(new HashMap<String, String>() {{
                put("url", e.select(".link > a").attr("href"));
                put("title", e.select(".subject").text());
                put("content", e.select(".posting").html());
                put("author", e.select(".author a").text());
                put("date", e.select(".author").text().replace("by " + e.select(".author a").text() + " - ", ""));
            }});
        }
        return results;
    }
}
