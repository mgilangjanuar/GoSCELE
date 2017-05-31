package com.mgilangjanuar.dev.sceleapp.Services;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Map;

import com.mgilangjanuar.dev.sceleapp.Models.ConfigAppModel;

/**
 * Created by muhammadgilangjanuar on 5/14/17.
 */

public class AuthService {
    private static Map<String, String> cookies;

    public static Map<String, String> getCookies() throws IOException {
        if (cookies == null) {
            Connection.Response response = Jsoup.connect(ConfigAppModel.BASE_URL)
                    .method(Connection.Method.GET)
                    .execute();
            cookies = response.cookies();
        }
        return cookies;
    }

    private Elements authHelper(String username, String password) throws IOException {
        Document doc;
        if (username != null && password != null) {
            Connection.Response response = Jsoup.connect(ConfigAppModel.urlTo("login/index.php?authldap_skipntlmsso=1"))
                    .data("username", username, "password", password)
                    .method(Connection.Method.POST)
                    .execute();
            cookies = response.cookies();
        }

        doc = Jsoup.connect(ConfigAppModel.BASE_URL)
                .cookies(getCookies())
                .get();

        return doc == null ? null : doc.select(".usertext");
    }

    public boolean isLogin() throws IOException {
        Elements userText = authHelper(null, null);
        return userText != null && userText.hasText();
    }

    public boolean login(String username, String password) throws IOException {
        Elements userText = authHelper(username, password);
        return userText != null && userText.hasText();
    }

    public String getUserText() throws IOException {
        Elements elements = authHelper(null, null);
        return elements == null ? null : elements.text();
    }

    public String getUserText(String username, String password) throws IOException {
        Elements elements = authHelper(username, password);
        return elements == null ? null : elements.text();
    }

    public void logout() throws IOException {
        if (this.isLogin()) {
            Document doc = Jsoup.connect(ConfigAppModel.BASE_URL)
                                .cookies(getCookies())
                                .get();

            String url = doc.select(".icon.menu-action[aria-labelledby=actionmenuaction-6]").attr("href");
            Jsoup.connect(url)
                    .cookies(getCookies())
                    .get();
        }
    }
}
