package com.mgilangjanuar.dev.goscele.modules.forum.list.view;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.mgilangjanuar.dev.goscele.R;
import com.mgilangjanuar.dev.goscele.base.BaseActivity;
import com.mgilangjanuar.dev.goscele.modules.forum.list.adapter.ForumListRecyclerViewAdapter;
import com.mgilangjanuar.dev.goscele.modules.forum.list.listener.ForumListListener;
import com.mgilangjanuar.dev.goscele.modules.forum.list.presenter.ForumListPresenter;
import com.mgilangjanuar.dev.goscele.utils.Constant;

import butterknife.BindView;

public class ForumListActivity extends BaseActivity implements ForumListListener {

    @BindView(R.id.toolbar_forum)
    Toolbar toolbar;

    @BindView(R.id.recycler_view_forum)
    RecyclerView recyclerView;

    @BindView(R.id.text_status_forum)
    TextView status;

    @BindView(R.id.swipe_refresh_forum)
    SwipeRefreshLayout swipeRefreshLayout;

    private String url;
    private ForumListPresenter presenter = new ForumListPresenter(this);

    @Override
    public void initialize(Bundle savedInstanceState) {
        super.initialize(savedInstanceState);
        url = getIntent().getStringExtra(Constant.URL);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        presenter.retrieveForums(url, false);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.retrieveForums(url, true);
            }
        });
    }

    @Override
    public int findLayout() {
        return R.layout.activity_forum_list;
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
    public void onRetrieve(ForumListRecyclerViewAdapter adapter) {
        if (adapter.getItemCount() == 0) {
            status.setVisibility(TextView.VISIBLE);
            status.setText(getString(R.string.empty));
        } else {
            status.setVisibility(TextView.GONE);
        }
        swipeRefreshLayout.setRefreshing(false);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onGetTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    @Override
    public void onError(String error) {
        status.setVisibility(TextView.GONE);
        swipeRefreshLayout.setRefreshing(false);
        showSnackbar(error);
    }
}
