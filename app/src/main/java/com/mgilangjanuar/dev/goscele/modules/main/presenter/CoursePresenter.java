package com.mgilangjanuar.dev.goscele.modules.main.presenter;

import com.mgilangjanuar.dev.goscele.base.BaseFragment;
import com.mgilangjanuar.dev.goscele.base.BasePresenter;
import com.mgilangjanuar.dev.goscele.modules.main.adapter.CourseRecyclerViewAdapter;
import com.mgilangjanuar.dev.goscele.modules.main.listener.CourseAllListener;
import com.mgilangjanuar.dev.goscele.modules.main.listener.CourseCurrentListener;
import com.mgilangjanuar.dev.goscele.modules.main.model.CourseModel;
import com.mgilangjanuar.dev.goscele.modules.main.provider.CourseProvider;
import com.mgilangjanuar.dev.goscele.modules.main.view.CourseCurrentFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mgilangjanuar (mgilangjanuar@gmail.com)
 *
 * @since 2017
 */

public class CoursePresenter extends BasePresenter {

    private CourseCurrentListener currentListener;
    private CourseAllListener allListener;
    private List<CourseModel> models = new ArrayList<>();

    public CoursePresenter() {
        super();
    }

    public void runProvider(BaseFragment fragment, boolean force) {
        models = new CourseModel().find().execute();
        if (!force && models.size() > 0) {
            if (fragment instanceof CourseCurrentFragment) {
                models = new CourseModel().find().where("is_current = ?", true).execute();
                currentListener.onRetrieve(new CourseRecyclerViewAdapter(fragment.getContext(), fragment, models));
            } else {
                models = new CourseModel().find().where("is_current = ?", false).execute();
                allListener.onRetrieve(new CourseRecyclerViewAdapter(fragment.getContext(), fragment, models));
            }
        } else {
            new CourseProvider(fragment, allListener).run();
        }
    }

    public void runProvider(BaseFragment fragment) {
        runProvider(fragment, false);
    }

    public void setCurrentListener(CourseCurrentListener currentListener) {
        this.currentListener = currentListener;
    }

    public void setAllListener(CourseAllListener allListener) {
        this.allListener = allListener;
    }
}
