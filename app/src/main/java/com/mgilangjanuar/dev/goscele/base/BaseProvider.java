package com.mgilangjanuar.dev.goscele.base;

import android.os.AsyncTask;
import android.text.TextUtils;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mgilangjanuar (mgilangjanuar@gmail.com)
 *
 * @since 2017
 */

public abstract class BaseProvider extends AsyncTask<String, Integer, List<Elements>> {

    protected Map<String, String> cookies = new HashMap<>();

    public BaseProvider() {
        super();
    }

    @Override
    @Deprecated
    protected List<Elements> doInBackground(String... params) {
        List<Elements> results = new ArrayList<>();
        int idx = 0;
        try {
            Connection.Response response = Jsoup.connect(url())
                    .data(data())
                    .method(method())
                    .cookies(cookies())
                    .execute();
            cookies = response.cookies();
            for (String param : params) {
                if (!TextUtils.isEmpty(param)) {
                    results.add(response.parse().select(param));
                }
                publishProgress((int) ((double) (++idx / params.length)) * 100);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return results;
    }

    @Override
    @Deprecated
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        onProgressUpdate(values[0]);
    }

    public abstract void run();

    public abstract String url();

    public String[] data() {
        return new String[0];
    }

    public Connection.Method method() {
        return Connection.Method.GET;
    }

    public Map<String, String> cookies() {
        return cookies;
    }

    public void onProgressUpdate(Integer value) {
    }
}
