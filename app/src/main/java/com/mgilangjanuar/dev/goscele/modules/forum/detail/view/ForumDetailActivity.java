package com.mgilangjanuar.dev.goscele.modules.forum.detail.view;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.mgilangjanuar.dev.goscele.R;
import com.mgilangjanuar.dev.goscele.base.BaseActivity;
import com.mgilangjanuar.dev.goscele.modules.forum.detail.presenter.ForumDetailPresenter;
import com.mgilangjanuar.dev.goscele.utils.Constant;
import com.mgilangjanuar.dev.goscele.utils.ShareContentUtil;
import com.mgilangjanuar.dev.goscele.utils.TabPagerAdapterUtil;

import butterknife.BindView;

/**
 * Created by mgilangjanuar (mgilangjanuar@gmail.com)
 *
 * @since 2017
 */
public class ForumDetailActivity extends BaseActivity {

    @BindView(R.id.toolbar_forum_detail)
    Toolbar toolbar;
    @BindView(R.id.view_pager_activity_forum_detail)
    ViewPager viewPager;
    @BindView(R.id.tab_forum_detail)
    TabLayout tabLayout;

    private ForumDetailPresenter presenter;

    @Override
    public int findLayout() {
        return R.layout.activity_forum_detail;
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
    public void initialize(Bundle savedInstanceState) {
        super.initialize(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        presenter = new ForumDetailPresenter(this, getIntent().getExtras().getString(Constant.URL));

        TabPagerAdapterUtil tabPagerAdapter = new TabPagerAdapterUtil(getSupportFragmentManager());
        tabPagerAdapter.addFragment(ForumPostFragment.newInstance(presenter), getString(R.string.post));
        tabPagerAdapter.addFragment(ForumCommentFragment.newInstance(presenter), getString(R.string.comment));

        viewPager.setAdapter(tabPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public int findOptionsMenu() {
        return R.menu.content_default_menu;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuitem_share:
                if (presenter.isCanShareContent())
                    ShareContentUtil.share(this, presenter.getContentModel());
                break;
            case R.id.menuitem_copy_url:
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                clipboardManager.setPrimaryClip(ClipData.newPlainText(Constant.LABEL, presenter.url));
                showSnackbar(R.string.url_copied);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
