package com.mgilangjanuar.dev.sceleapp;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.mgilangjanuar.dev.sceleapp.Adapters.BaseTabViewPagerAdapter;
import com.mgilangjanuar.dev.sceleapp.Adapters.ForumDetailCommentAdapter;
import com.mgilangjanuar.dev.sceleapp.Fragments.ForumDetail.Comments;
import com.mgilangjanuar.dev.sceleapp.Fragments.ForumDetail.Post;
import com.mgilangjanuar.dev.sceleapp.Presenters.ForumDetailPresenter;

public class ForumDetail extends AppCompatActivity {

    String url;
    ForumDetailPresenter forumDetailPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum_detail);

        Bundle bundle = getIntent().getExtras();
        if (bundle == null || bundle.getString("url") == null) {
            Toast.makeText(this, "Broken URL", Toast.LENGTH_SHORT).show();
            onBackPressed();
            return;
        }

        url = bundle.getString("url");
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_forum_detail);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        (new Thread(new Runnable() {
            @Override
            public void run() {
                setupForumDetail();
            }
        })).start();
    }

    public void setupForumDetail() {
        forumDetailPresenter = new ForumDetailPresenter(this, url);
        forumDetailPresenter.buildCommentAdapter();

        final ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager_activity_forum_detail);
        final BaseTabViewPagerAdapter baseTabViewPagerAdapter = new BaseTabViewPagerAdapter(getSupportFragmentManager());
        baseTabViewPagerAdapter.addFragment(Post.newInstance(forumDetailPresenter), getResources().getString(R.string.title_forum_detail_post));
        baseTabViewPagerAdapter.addFragment(Comments.newInstance(forumDetailPresenter), getResources().getString(R.string.title_forum_detail_comments));

        final TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_forum_detail);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                viewPager.setAdapter(baseTabViewPagerAdapter);
                tabLayout.setupWithViewPager(viewPager);
                getSupportActionBar().setTitle(forumDetailPresenter.getForumDetailModel().getSavedTitle());
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
