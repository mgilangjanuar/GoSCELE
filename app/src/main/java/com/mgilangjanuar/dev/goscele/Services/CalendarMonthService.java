package com.mgilangjanuar.dev.goscele.Services;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mgilangjanuar.dev.goscele.Models.ConfigAppModel;

/**
 * Created by muhammadgilangjanuar on 5/18/17.
 */

public class CalendarMonthService {

    private Elements getElements(String tag) throws IOException {
        return getElements(tag, getCurrentTime());
    }

    private Elements getElements(String tag, long time) throws IOException {
        Document doc;
        doc = Jsoup.connect(ConfigAppModel.urlTo("?time=" + time))
                .cookies(AuthService.getCookies())
                .get();
        return doc == null ? null : doc.select(tag);
    }

    public long getCurrentTime() {
        return ((new Date()).getTime() / 1000);
    }

    public List<String> getListDay(long time) throws IOException {
        List<String> results = new ArrayList<>();
        for (Element e: getElements(".hasevent a", time)) {
            results.add(e.text());
        }
        return results;
    }

    public String getMonth() throws IOException {
        return getMonth(getCurrentTime());
    }

    public String getMonth(long time) throws IOException {
        return getElements(".current", time).text();
    }

}
