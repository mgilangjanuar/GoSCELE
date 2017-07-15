package com.mgilangjanuar.dev.goscele;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import com.mgilangjanuar.dev.goscele.Adapters.BaseTabViewPagerAdapter;
import com.mgilangjanuar.dev.goscele.Fragments.CourseDetail.DashboardFragment;
import com.mgilangjanuar.dev.goscele.Fragments.CourseDetail.EventFragment;
import com.mgilangjanuar.dev.goscele.Fragments.CourseDetail.NewsFragment;
import com.mgilangjanuar.dev.goscele.Presenters.CourseDetailPresenter;

import butterknife.BindView;

public class CourseDetailActivity extends BaseActivity {

    @BindView(R.id.toolbar_course_detail)
    Toolbar toolbar;
    @BindView(R.id.view_pager_activity_course_detail)
    ViewPager viewPager;
    @BindView(R.id.tab_course_detail)
    TabLayout tabLayout;
    private String url;
    private CourseDetailPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);

        url = getIntent().getExtras().getString("url");
        presenter = new CourseDetailPresenter(this, url);
        toolbar.setTitle(getResources().getString(R.string.loading_text));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        (new Thread(() -> setupCourseDetail())).start();
    }

    private void setupCourseDetail() {
        final BaseTabViewPagerAdapter fragmentPagerAdapter = new BaseTabViewPagerAdapter(getSupportFragmentManager());
        fragmentPagerAdapter.addFragment(DashboardFragment.newInstance(presenter), getResources().getString(R.string.title_course_dashboard));
        fragmentPagerAdapter.addFragment(NewsFragment.newInstance(presenter), getResources().getString(R.string.title_course_news));
        fragmentPagerAdapter.addFragment(EventFragment.newInstance(presenter), getResources().getString(R.string.title_course_event));
        viewPager.setAdapter(fragmentPagerAdapter);

        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
