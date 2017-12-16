package com.mgilangjanuar.dev.goscele.modules.course.provider;

import com.mgilangjanuar.dev.goscele.base.BaseProvider;
import com.mgilangjanuar.dev.goscele.modules.common.model.CookieModel;
import com.mgilangjanuar.dev.goscele.modules.course.adapter.CourseSearchRecyclerViewAdapter;
import com.mgilangjanuar.dev.goscele.modules.course.listener.CourseSearchListener;
import com.mgilangjanuar.dev.goscele.modules.course.model.CourseSearchModel;
import com.mgilangjanuar.dev.goscele.utils.Constant;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by mgilangjanuar (mgilangjanuar@gmail.com)
 *
 * @since 2017
 */

public class CourseSearchProvider extends BaseProvider {

    private CourseSearchListener listener;
    private String query;

    public CourseSearchProvider(CourseSearchListener listener, String query) {
        this.listener = listener;
        this.query = query;
    }

    @Override
    public Map<String, String> cookies() {
        return CookieModel.getCookiesMap();
    }

    @Override
    public void run() {
        execute(".course-search-result.course-search-result-search");
    }

    @Override
    public String url() {
        try {
            return Constant.BASE_URL + "course/search.php?search=" + URLEncoder.encode(query, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(List<Elements> elementses) {
        super.onPostExecute(elementses);
        try {
            Elements elements = elementses.get(0);
            List<CourseSearchModel> results = new ArrayList<>();
            for (Element e: elements.select(".coursebox")) {
                String name = e.select(".coursename").text();
                String url = e.select(".coursename a").attr("href");
                results.add(new CourseSearchModel(name, url));
            }
            listener.onRetrieve(new CourseSearchRecyclerViewAdapter(results));
        } catch (Exception e) {
            listener.onError(e.getMessage());
        }
    }
}
