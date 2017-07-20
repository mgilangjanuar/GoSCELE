package com.mgilangjanuar.dev.goscele;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.mgilangjanuar.dev.goscele.Adapters.BaseTabViewPagerAdapter;
import com.mgilangjanuar.dev.goscele.Fragments.ForumDetail.CommentsFragment;
import com.mgilangjanuar.dev.goscele.Fragments.ForumDetail.PostFragment;
import com.mgilangjanuar.dev.goscele.Helpers.ShareContentHelper;
import com.mgilangjanuar.dev.goscele.Presenters.ForumDetailPresenter;

import butterknife.BindView;

public class ForumDetailActivity extends BaseActivity {

    @BindView(R.id.toolbar_forum_detail)
    Toolbar toolbar;
    @BindView(R.id.fab_forum_comment)
    FloatingActionButton actionButton;
    @BindView(R.id.view_pager_activity_forum_detail)
    ViewPager viewPager;
    @BindView(R.id.tab_forum_detail)
    TabLayout tabLayout;
    private String url;
    private ForumDetailPresenter presenter;

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
        baseTabViewPagerAdapter.addFragment(PostFragment.newInstance(presenter), getResources().getString(R.string.title_forum_detail_post));
        baseTabViewPagerAdapter.addFragment(CommentsFragment.newInstance(presenter, actionButton), getResources().getString(R.string.title_forum_detail_comments));

        runOnUiThread(() -> {
            viewPager.setAdapter(baseTabViewPagerAdapter);
            tabLayout.setupWithViewPager(viewPager);
        });

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.content_default_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuitem_share:
                if (presenter.isCanShareContent())
                    ShareContentHelper.share(this, presenter.getContentModel());
                break;
            case R.id.menuitem_copy_url:
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                clipboardManager.setPrimaryClip(ClipData.newPlainText("label", url));
                Toast.makeText(this, "URL Copied!", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
