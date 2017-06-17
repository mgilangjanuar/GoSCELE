package com.mgilangjanuar.dev.sceleapp.Services;

import android.util.Log;

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
                put("deleteUrl", e.select(".commands a:contains(Delete)").attr("href"));
            }});
        }

        results.put("url", url);
        results.put("title", getElements(".subject").get(0).text());
        results.put("author", getElements(".author a").get(0).text());
        results.put("date", getElements(".author").get(0).text().replace("by " + getElements(".author a").get(0).text() + " - ", ""));
        results.put("content", getElements(".maincontent").get(0).html());
        results.put("deleteUrl", getElements(".forumpost").get(0).select(".commands a:contains(Delete)").attr("href"));
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

    private void postHelper(String title, String message, Document doc) throws IOException {
        Jsoup.connect(ConfigAppModel.urlTo("mod/forum/post.php"))
                .data("subject", title)
                .data("message[text]", message)
                .data("message[itemid]", doc.select("input[name=message[itemid]]").attr("value"))
                .data("message[format]", doc.select("input[name=message[format]]").attr("value"))
                .data("discussionsubscribe", "1")
                .data("timeend", "0")
                .data("course", doc.select("input[name=course]").attr("value"))
                .data("forum", doc.select("input[name=forum]").attr("value"))
                .data("discussion", doc.select("input[name=discussion]").attr("value"))
                .data("parent", doc.select("input[name=parent]").attr("value"))
                .data("userid", doc.select("input[name=userid]").attr("value"))
                .data("groupid", doc.select("input[name=groupid]").attr("value"))
                .data("edit", doc.select("input[name=edit]").attr("value"))
                .data("reply", doc.select("input[name=reply]").attr("value"))
                .data("sesskey", doc.select("input[name=sesskey]").attr("value"))
                .data("_qf__mod_forum_post_form", "1")
                .data("mform_isexpanded_id_general", "1")
                .data("submitbutton", "Post to forum")
                .cookies(AuthService.getCookies())
                .post();
    }

    public void postForumComment(String message) throws IOException {
        String url = getElements(".commands a:contains(Reply)").get(0).attr("href");

        Document doc = Jsoup.connect(url)
                .cookies(AuthService.getCookies())
                .get();

        postHelper(doc.select("#id_subject").attr("value"), message, doc);
    }

    private void deleteHelper(Document doc) throws IOException {
        Jsoup.connect(ConfigAppModel.urlTo("mod/forum/post.php"))
                .data("sesskey", doc.select("input[name=sesskey]").attr("value"))
                .data("delete", doc.select("input[name=delete]").attr("value"))
                .data("confirm", doc.select("input[name=confirm]").attr("value"))
                .data("submit", "Continue")
                .cookies(AuthService.getCookies())
                .post();
    }

    public void deleteForumComment(String url) throws IOException {
        Document doc = Jsoup.connect(url)
                .cookies(AuthService.getCookies())
                .get();

        deleteHelper(doc);
    }

    public void postForum(String title, String message) throws IOException {
        String url = getElements("#newdiscussionform").attr("action");
        String id = getElements("#newdiscussionform input[name=forum]").attr("value");

        Document doc = Jsoup.connect(url + "?forum=" + id)
                .cookies(AuthService.getCookies())
                .get();

        postHelper(title, message, doc);
    }

    public void deleteForum(String url) throws IOException {
        Document doc = Jsoup.connect(url)
                .cookies(AuthService.getCookies())
                .get();

        deleteHelper(doc);
    }

}
