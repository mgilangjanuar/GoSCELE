package com.mgilangjanuar.dev.goscele.modules.main.provider;

import com.mgilangjanuar.dev.goscele.base.BaseFragment;
import com.mgilangjanuar.dev.goscele.base.BaseProvider;
import com.mgilangjanuar.dev.goscele.modules.common.model.CookieModel;
import com.mgilangjanuar.dev.goscele.modules.main.adapter.CourseRecyclerViewAdapter;
import com.mgilangjanuar.dev.goscele.modules.main.listener.CourseAllListener;
import com.mgilangjanuar.dev.goscele.modules.main.model.CourseModel;
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

public class CourseProvider extends BaseProvider {

    private BaseFragment fragment;
    private CourseAllListener allListener;

    public CourseProvider(BaseFragment fragment, CourseAllListener allListener) {
        this.fragment = fragment;
        this.allListener = allListener;
    }

    @Override
    public void run() {
        execute(".block_course_list.block.list_block .content .column.c1");
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
            List<CourseModel> list = new ArrayList<>();
            for (Element e: elements) {
                CourseModel courseModel = new CourseModel();
                courseModel.url = e.select("a").attr("href");
                courseModel.name = e.text();

                CourseModel currentCourseModel = new CourseModel().find().where("url = ?", courseModel.url).executeSingle();
                courseModel.isCurrent = currentCourseModel != null && currentCourseModel.isCurrent;
                if (currentCourseModel != null) {
                    currentCourseModel.delete();
                }

                courseModel.save();
                list.add(courseModel);
            }
            allListener.onRetrieve(new CourseRecyclerViewAdapter(fragment.getContext(), fragment, list));
        } catch (Exception e) {
            allListener.onError(e.getMessage());
        }
    }
}
