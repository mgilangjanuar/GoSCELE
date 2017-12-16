package com.mgilangjanuar.dev.goscele.modules.forum.list.provider;

import com.mgilangjanuar.dev.goscele.base.BaseProvider;
import com.mgilangjanuar.dev.goscele.modules.common.model.CookieModel;
import com.mgilangjanuar.dev.goscele.modules.forum.list.adapter.ForumListRecyclerViewAdapter;
import com.mgilangjanuar.dev.goscele.modules.forum.list.listener.ForumListListener;
import com.mgilangjanuar.dev.goscele.modules.forum.list.model.ForumModel;
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

public class ForumSearchProvider extends BaseProvider {

    private ForumListListener listener;
    private String query;

    public ForumSearchProvider(ForumListListener listener, String query) {
        this.listener = listener;
        this.query = query;
    }

    @Override
    public Map<String, String> cookies() {
        return CookieModel.getCookiesMap();
    }

    @Override
    public void run() {
        execute("#region-main");
    }

    @Override
    public String url() {
        try {
            return Constant.BASE_URL + "mod/forum/search.php?id=1&perpage=50&search=" + URLEncoder.encode(query, "UTF-8");
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
            List<ForumModel> results = new ArrayList<>();
            for (Element e: elements.select("div > .forumpost")) {
                ForumModel model = new ForumModel();
                model.url = Constant.BASE_URL + "/mod/forum/" + e.select(".subject a").get(1).attr("href");
                model.title = e.select(".subject a").get(1).text();
                model.author = e.select(".author a").text();
                model.lastUpdate = e.select(".author").text().replace("by " + e.select(".author a").text() + " - ", "");
                results.add(model);
            }
            listener.onRetrieve(new ForumListRecyclerViewAdapter(results));
        } catch (Exception e) {
            listener.onError(e.getMessage());
        }
    }
}
