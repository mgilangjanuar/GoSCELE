package com.mgilangjanuar.dev.sceleapp.Services;

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
import java.util.Objects;

/**
 * Created by muhammadgilangjanuar on 5/24/17.
 */

public class ForumService {

    public String url;

    public ForumService(String url) {
        this.url = url;
    }

    private Elements getElements(String tag) throws IOException {
        Document doc;
        doc = Jsoup.connect(url)
                .cookies(AuthService.getCookies())
                .get();
        return doc == null ? null : doc.select(tag);
    }

    public Map<String, Object> getForumDetails() throws IOException {
        Map<String, Object> results = new HashMap<>();

        final List<Map<String, String>> subResults = new ArrayList<>();
        for(final Element e: getElements(".indent")) {
            subResults.add(new HashMap<String, String>() {{
                put("author", e.select(".author a").get(0).text());
                put("date", e.select(".author").get(0).text().replace("by " + e.select(".author a").get(0).text() + " - ", ""));
                put("content", e.select(".maincontent").get(0).html());
            }});
        }

        results.put("url", url);
        results.put("title", getElements(".subject").get(0).text());
        results.put("author", getElements(".author a").get(0).text());
        results.put("date", getElements(".author").get(0).text().replace("by " + getElements(".author a").get(0).text() + " - ", ""));
        results.put("content", getElements(".maincontent").get(0).html());
        results.put("forumCommentModelList", subResults);

        return results;
    }

    public List<Map<String, String>> getForums() throws IOException {
        List<Map<String, String>> results = new ArrayList<>();
        for (final Element e: getElements(".discussion")) {
            results.add(new HashMap<String, String>() {{
                put("url", e.select(".topic.starter a").attr("href"));
                put("title", e.select(".topic.starter").text());
                put("author", e.select(".author").text());
                put("repliesNumber", e.select(".replies").text());
                put("lastUpdate", e.select(".lastpost a").get(1).text());

            }});
        }
        return results;
    }

    public String getTitle() throws IOException {
        return getElements("title").text();
    }

}
