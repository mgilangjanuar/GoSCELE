package com.mgilangjanuar.dev.goscele.modules.main.provider;

import com.activeandroid.Model;
import com.mgilangjanuar.dev.goscele.base.BaseProvider;
import com.mgilangjanuar.dev.goscele.modules.common.model.CookieModel;
import com.mgilangjanuar.dev.goscele.modules.main.adapter.HomeRecyclerViewAdapter;
import com.mgilangjanuar.dev.goscele.modules.main.listener.HomeListener;
import com.mgilangjanuar.dev.goscele.modules.main.model.HomeModel;
import com.mgilangjanuar.dev.goscele.utils.Constant;

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

public class HomeProvider extends BaseProvider {

    private HomeListener listener;

    public HomeProvider(HomeListener listener) {
        this.listener = listener;
    }

    @Override
    public void run() {
        execute(".forumpost");
    }

    @Override
    public String url() {
        return Constant.BASE_URL;
    }

    @Override
    public Map<String, String> cookies() {
        return CookieModel.getCookiesMap();
    }

    @Override
    protected void onPostExecute(List<Elements> elementses) {
        super.onPostExecute(elementses);
        try {
            Elements elements = elementses.get(0);

            for (Model e: new HomeModel().find().execute()) e.delete();

            List<HomeModel> list = new ArrayList<>();
            for (Element e : elements) {
                String url = e.select(".link > a").attr("href");
                String title = e.select(".subject").text();
                String author = e.select(".author a").text();
                String date = e.select(".author").text().replace("by " + author + " - ", "");
                String content = e.select(".posting").html();

                HomeModel model = new HomeModel();
                model.url = url;
                model.title = title;
                model.author = author;
                model.date = date;
                model.content = content;
                model.save();

                list.add(model);
            }
            listener.onRetrieve(new HomeRecyclerViewAdapter(list));
        } catch (Exception e) {
            listener.onError();
        }
    }
}
