package com.mgilangjanuar.dev.sceleapp;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.mgilangjanuar.dev.sceleapp.Adapters.BaseTabViewPagerAdapter;
import com.mgilangjanuar.dev.sceleapp.Fragments.CourseDetail.DashboardFragment;
import com.mgilangjanuar.dev.sceleapp.Fragments.CourseDetail.EventFragment;
import com.mgilangjanuar.dev.sceleapp.Fragments.CourseDetail.NewsFragment;
import com.mgilangjanuar.dev.sceleapp.Presenters.CourseDetailPresenter;

public class CourseDetail extends AppCompatActivity {

    String url;
    CourseDetailPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);

        Bundle bundle = getIntent().getExtras();
        if (bundle == null || bundle.getString("url") == null) {
            Toast.makeText(this, "Broken URL", Toast.LENGTH_SHORT).show();
            onBackPressed();
            return;
        }

        url = bundle.getString("url");
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_course_detail);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        (new Thread(new Runnable() {
            @Override
            public void run() {
                setupCourseDetail();
            }
        })).start();
    }

    private void setupCourseDetail() {
        presenter = new CourseDetailPresenter(this, url);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager_activity_course_detail);
        final BaseTabViewPagerAdapter fragmentPagerAdapter = new BaseTabViewPagerAdapter(getSupportFragmentManager());
        fragmentPagerAdapter.addFragment(DashboardFragment.newInstance(presenter), getResources().getString(R.string.title_course_dashboard));
        fragmentPagerAdapter.addFragment(NewsFragment.newInstance(presenter), getResources().getString(R.string.title_course_news));
        fragmentPagerAdapter.addFragment(EventFragment.newInstance(presenter), getResources().getString(R.string.title_course_event));
        viewPager.setAdapter(fragmentPagerAdapter);

        final TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_course_detail);
        tabLayout.setupWithViewPager(viewPager);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                getSupportActionBar().setTitle(presenter.getCourseModel().name);
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
