package com.mgilangjanuar.dev.goscele.modules.forum.list.provider;

import com.mgilangjanuar.dev.goscele.base.BaseProvider;
import com.mgilangjanuar.dev.goscele.modules.common.model.CookieModel;
import com.mgilangjanuar.dev.goscele.modules.forum.list.adapter.ForumListRecyclerViewAdapter;
import com.mgilangjanuar.dev.goscele.modules.forum.list.listener.ForumListListener;
import com.mgilangjanuar.dev.goscele.modules.forum.list.model.ForumModel;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by mgilangjanuar (mgilangjanuar@gmail.com)
 *
 * @since 2017
 */

public class ForumListProvider extends BaseProvider {

    private String url;

    private ForumListListener listener;

    public ForumListProvider(String url, ForumListListener listener) {
        this.url = url;
        this.listener = listener;
    }

    @Override
    public void run() {
        execute("#region-main");
    }

    @Override
    public Map<String, String> cookies() {
        return CookieModel.getCookiesMap();
    }

    @Override
    public String url() {
        return url;
    }

    @Override
    protected void onPostExecute(List<Elements> elementses) {
        super.onPostExecute(elementses);
        try {
            List<ForumModel> models = new ArrayList<>();
            Elements elements = elementses.get(0);
            String title = elements.select("div[role=main] > h2").text();
            listener.onGetTitle(title);
            for (Element e: elements.select(".discussion")) {
                String url = e.select(".topic.starter a").attr("href");

                ForumModel model = new ForumModel().find().where("url = ?", url).executeSingle();
                if (model != null) model.delete();

                model = new ForumModel();
                model.listUrl = this.url;
                model.forumTitle = title;
                model.url = url;
                model.title = e.select(".topic.starter").text();
                model.author = e.select(".author").text();
                model.repliesNumber = e.select(".replies").text();
                model.lastUpdate = e.select(".lastpost a").get(1).text();
                model.save();

                models.add(model);
            }
            listener.onRetrieve(new ForumListRecyclerViewAdapter(models));
        } catch (Exception e) {
            listener.onError(e.getMessage());
        }
    }
}
