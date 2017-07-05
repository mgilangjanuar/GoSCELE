package com.mgilangjanuar.dev.goscele;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import com.mgilangjanuar.dev.goscele.Adapters.BaseTabViewPagerAdapter;
import com.mgilangjanuar.dev.goscele.Fragments.ForumDetail.Comments;
import com.mgilangjanuar.dev.goscele.Fragments.ForumDetail.Post;
import com.mgilangjanuar.dev.goscele.Presenters.ForumDetailPresenter;

import butterknife.BindView;

public class ForumDetailActivity extends BaseActivity {

    private String url;
    private ForumDetailPresenter presenter;

    @BindView(R.id.toolbar_forum_detail)
    Toolbar toolbar;
    @BindView(R.id.fab_forum_comment)
    FloatingActionButton actionButton;
    @BindView(R.id.view_pager_activity_forum_detail)
    ViewPager viewPager;
    @BindView(R.id.tab_forum_detail)
    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum_detail);

        url = getIntent().getExtras().getString("url");
        toolbar.setTitle(getResources().getString(R.string.loading_text));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        (new Thread(() -> setupForumDetail())).start();
    }

    public void setupForumDetail() {
        presenter = new ForumDetailPresenter(this, url);

        actionButton.hide();

        final BaseTabViewPagerAdapter baseTabViewPagerAdapter = new BaseTabViewPagerAdapter(getSupportFragmentManager());
        baseTabViewPagerAdapter.addFragment(Post.newInstance(presenter), getResources().getString(R.string.title_forum_detail_post));
        baseTabViewPagerAdapter.addFragment(Comments.newInstance(presenter, actionButton), getResources().getString(R.string.title_forum_detail_comments));

        runOnUiThread(() -> {
            viewPager.setAdapter(baseTabViewPagerAdapter);
            tabLayout.setupWithViewPager(viewPager);
        });
        actionButton.setOnClickListener(v -> presenter.buildAlertDialog());

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getText().toString().equals(getResources().getString(R.string.title_forum_detail_comments))) {
                    actionButton.show();
                } else {
                    actionButton.hide();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
