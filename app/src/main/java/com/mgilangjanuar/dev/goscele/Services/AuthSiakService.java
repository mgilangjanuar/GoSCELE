package com.mgilangjanuar.dev.goscele.Services;

import android.util.Log;

import com.mgilangjanuar.dev.goscele.Models.ConfigAppModel;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Map;

/**
 * Created by mjanuar on 7/20/17.
 */

public class AuthSiakService {
    public static Map<String, String> cookies;

    public Elements login(String username, String password) throws IOException {
        Connection.Response response = Jsoup.connect(ConfigAppModel.urlTo("main/Authentication/Index", true))
                .data("u", username, "p", password)
                .method(Connection.Method.POST)
                .execute();
        cookies = response.cookies();
        Jsoup.connect(ConfigAppModel.urlTo("main/Authentication/ChangeRole", true))
                .cookies(cookies)
                .get();
        Document doc = Jsoup.connect(ConfigAppModel.urlTo("main/Welcome/", true))
                .cookies(cookies)
                .get();
        Log.e("aoinsioasas",  doc.select(".linfo:eq(0)").text());
        return doc == null ? null : doc.select(".linfo:eq(0)");
    }
}
