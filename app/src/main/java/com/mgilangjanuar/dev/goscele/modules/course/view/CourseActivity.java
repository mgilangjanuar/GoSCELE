package com.mgilangjanuar.dev.goscele.modules.course.view;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.mgilangjanuar.dev.goscele.R;
import com.mgilangjanuar.dev.goscele.base.BaseActivity;
import com.mgilangjanuar.dev.goscele.modules.course.adapter.CourseDetailRecyclerViewAdapter;
import com.mgilangjanuar.dev.goscele.modules.course.listener.CourseDetailListener;
import com.mgilangjanuar.dev.goscele.modules.course.presenter.CourseDetailPresenter;
import com.mgilangjanuar.dev.goscele.utils.Constant;

import butterknife.BindView;

public class CourseActivity extends BaseActivity implements CourseDetailListener {

    @BindView(R.id.toolbar_course_detail)
    Toolbar toolbar;

    @BindView(R.id.recycler_view_course_detail)
    RecyclerView recyclerView;

    @BindView(R.id.text_status_course_dashboard)
    TextView status;

    @BindView(R.id.swipe_refresh_course_detail)
    SwipeRefreshLayout swipeRefreshLayout;

    private CourseDetailPresenter presenter;

    @Override
    public void initialize(Bundle savedInstanceState) {
        super.initialize(savedInstanceState);
        presenter = new CourseDetailPresenter(getIntent().getStringExtra(Constant.URL), this);

        getSupportActionBar().setTitle(presenter.getCourseName());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        presenter.runProvider();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.clear();
                presenter.runProvider();
            }
        });
    }

    @Override
    public int findLayout() {
        return R.layout.activity_course;
    }

    @Override
    public Toolbar findToolbar() {
        return toolbar;
    }

    @Override
    public String findTitle() {
        return getString(R.string.loading);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onRetrieveDetail(CourseDetailRecyclerViewAdapter adapter) {
        if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) swipeRefreshLayout.setRefreshing(false);

        if (adapter.getItemCount() > 0) {
            status.setVisibility(TextView.GONE);
            recyclerView.setAdapter(adapter);
        } else {
            status.setText(getString(R.string.empty));
            status.setVisibility(TextView.VISIBLE);
        }
    }

    @Override
    public void onError(String error) {
        if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) swipeRefreshLayout.setRefreshing(false);

        showSnackbar(error);
    }
}
