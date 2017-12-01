package com.mgilangjanuar.dev.goscele.modules.course.provider;


import android.text.TextUtils;

import com.mgilangjanuar.dev.goscele.base.BaseProvider;
import com.mgilangjanuar.dev.goscele.modules.common.model.CookieModel;
import com.mgilangjanuar.dev.goscele.modules.course.adapter.CourseDetailRecyclerViewAdapter;
import com.mgilangjanuar.dev.goscele.modules.course.listener.CourseDetailListener;
import com.mgilangjanuar.dev.goscele.modules.course.model.CourseDetailInnerModel;
import com.mgilangjanuar.dev.goscele.modules.course.model.CourseDetailModel;

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

public class CourseDetailProvider extends BaseProvider {

    private String url;
    private CourseDetailListener listener;

    public CourseDetailProvider(String url, CourseDetailListener listener) {
        this.url = url;
        this.listener = listener;
    }

    @Override
    public void run() {
        execute(".topics li.section");
    }

    @Override
    protected void onPostExecute(List<Elements> elementses) {
        super.onPostExecute(elementses);
        try {
            CourseDetailModel currentModel = new CourseDetailModel().find().where("url = ?", url).executeSingle();
            if (currentModel != null) {
                for (CourseDetailInnerModel innerModel: currentModel.courseDetailInnerModels()) {
                    innerModel.delete();
                }
                currentModel.delete();
            }

            Elements elements = elementses.get(0);
            List<CourseDetailModel> models = new ArrayList<>();
            for (Element e: elements) {
                if (!TextUtils.isEmpty(e.select(".content .summary").text()) || e.select(".content .section .activity").size() > 0) {
                    CourseDetailModel model = new CourseDetailModel();
                    model.url = url;
                    model.title = e.select(".content .sectionname").text();
                    if (!TextUtils.isEmpty(e.select(".content .summary").text())) {
                        model.summary = e.select(".content .summary").html();
                    }
                    model.save();
                    for (Element e1 : e.select(".content .section .activity")) {
                        CourseDetailInnerModel innerModel = new CourseDetailInnerModel();
                        innerModel.url = e1.select(".activityinstance a").attr("href");
                        innerModel.title = e1.select(".activityinstance a .instancename").text();
                        innerModel.comment = e1.select(".contentafterlink").html();
                        if (TextUtils.isEmpty(e1.select(".contentafterlink").text())) {
                            innerModel.comment = e1.select(".contentwithoutlink").html();
                        }
                        innerModel.courseDetailModel = model;
                        innerModel.save();
                    }
                    models.add(model);
                }
            }
            listener.onRetrieveDetail(new CourseDetailRecyclerViewAdapter(models));
        } catch (Exception e) {
            listener.onError(e.getMessage());
        }
    }

    @Override
    public Map<String, String> cookies() {
        return CookieModel.getCookiesMap();
    }

    @Override
    public String url() {
        return url;
    }
}
