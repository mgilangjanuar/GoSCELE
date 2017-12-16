package com.mgilangjanuar.dev.goscele.modules.course.presenter;

import android.text.TextUtils;

import com.mgilangjanuar.dev.goscele.modules.course.adapter.CourseSearchRecyclerViewAdapter;
import com.mgilangjanuar.dev.goscele.modules.course.listener.CourseSearchListener;
import com.mgilangjanuar.dev.goscele.modules.course.model.CourseSearchModel;
import com.mgilangjanuar.dev.goscele.modules.course.provider.CourseSearchProvider;

import java.util.ArrayList;

/**
 * Created by mgilangjanuar (mgilangjanuar@gmail.com)
 *
 * @since 2017
 */

public class CourseSearchPresenter {

    private CourseSearchListener listener;

    public CourseSearchPresenter(CourseSearchListener listener) {
        this.listener = listener;
    }

    private boolean validate(String query) {
        return !TextUtils.isEmpty(query);
    }

    public CourseSearchRecyclerViewAdapter buildEmptyAdapter() {
        return new CourseSearchRecyclerViewAdapter(new ArrayList<CourseSearchModel>());
    }

    public void search(String query) {
        if (validate(query)) {
            new CourseSearchProvider(listener, query).run();
        } else {
            listener.onError("Please fill that required field");
        }
    }
}
