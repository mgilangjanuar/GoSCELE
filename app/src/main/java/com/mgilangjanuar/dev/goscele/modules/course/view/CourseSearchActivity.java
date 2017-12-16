package com.mgilangjanuar.dev.goscele.modules.course.view;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.mgilangjanuar.dev.goscele.R;
import com.mgilangjanuar.dev.goscele.base.BaseActivity;
import com.mgilangjanuar.dev.goscele.modules.course.adapter.CourseSearchRecyclerViewAdapter;
import com.mgilangjanuar.dev.goscele.modules.course.listener.CourseSearchListener;
import com.mgilangjanuar.dev.goscele.modules.course.presenter.CourseSearchPresenter;

import butterknife.BindView;
import butterknife.OnClick;

public class CourseSearchActivity extends BaseActivity implements CourseSearchListener {

    @BindView(R.id.edit_search_course)
    EditText search;

    @BindView(R.id.text_status_search_course)
    TextView status;

    @BindView(R.id.recycler_view_search_course)
    RecyclerView courses;

    private CourseSearchPresenter presenter = new CourseSearchPresenter(this);

    @Override
    public void initialize(Bundle savedInstanceState) {
        super.initialize(savedInstanceState);

        courses.setLayoutManager(new LinearLayoutManager(this));
        courses.setItemAnimator(new DefaultItemAnimator());
        courses.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        search.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    search();
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public int findLayout() {
        return R.layout.activity_course_search;
    }

    @OnClick(R.id.button_search_course)
    public void search() {
        hideKeyboard();
        courses.setAdapter(presenter.buildEmptyAdapter());
        status.setVisibility(TextView.VISIBLE);
        status.setText(R.string.loading);
        presenter.search(search.getText().toString());
    }

    @Override
    public void onRetrieve(CourseSearchRecyclerViewAdapter adapter) {
        if (adapter.getItemCount() == 0) {
            status.setVisibility(TextView.VISIBLE);
            status.setText(R.string.empty);
        } else {
            status.setVisibility(TextView.GONE);
            courses.setAdapter(adapter);
        }
    }

    @Override
    public void onError(String error) {
        showSnackbar(error);
    }
}
