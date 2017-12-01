package com.mgilangjanuar.dev.goscele.modules.course.presenter;

import com.mgilangjanuar.dev.goscele.modules.course.adapter.CourseDetailRecyclerViewAdapter;
import com.mgilangjanuar.dev.goscele.modules.course.listener.CourseDetailListener;
import com.mgilangjanuar.dev.goscele.modules.course.model.CourseDetailInnerModel;
import com.mgilangjanuar.dev.goscele.modules.course.model.CourseDetailModel;
import com.mgilangjanuar.dev.goscele.modules.course.provider.CourseDetailProvider;
import com.mgilangjanuar.dev.goscele.modules.main.model.CourseModel;

import java.util.List;

/**
 * Created by mgilangjanuar (mgilangjanuar@gmail.com)
 *
 * @since 2017
 */

public class CourseDetailPresenter {

    private String url;
    private CourseDetailListener listener;

    public CourseDetailPresenter(String url, CourseDetailListener listener) {
        this.url = url;
        this.listener = listener;
    }

    public void runProvider() {
        List<CourseDetailModel> currentModels = new CourseDetailModel().find().where("url = ?", url).execute();
        if (currentModels.size() > 0) {
            listener.onRetrieveDetail(new CourseDetailRecyclerViewAdapter(currentModels));
        } else {
            new CourseDetailProvider(url, listener).run();
        }
    }

    public void clear() {
        List<CourseDetailModel> currentModels = new CourseDetailModel().find().where("url = ?", url).execute();
        for (CourseDetailModel model: currentModels) {
            for (CourseDetailInnerModel innerModel: model.courseDetailInnerModels()) {
                innerModel.delete();
            }
            model.delete();
        }
    }

    public String getCourseName() {
        CourseModel model = new CourseModel().find().where("url = ?", url).executeSingle();
        return model == null ? "Unknown" : model.name;
    }
}
