package com.mgilangjanuar.dev.goscele;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.mgilangjanuar.dev.goscele.Adapters.ForumAdapter;
import com.mgilangjanuar.dev.goscele.Presenters.ForumPresenter;

import butterknife.BindView;

public class ForumActivity extends BaseActivity implements ForumPresenter.ForumServicePresenter {

    private ForumPresenter presenter;
    private String url;

    @BindView(R.id.toolbar_forum) Toolbar toolbar;
    @BindView(R.id.recycler_view_forum) RecyclerView recyclerView;
    @BindView(R.id.text_status_forum) TextView status;
    @BindView(R.id.swipe_refresh_forum) SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum);

        url = getIntent().getExtras().getString("url");
        presenter = new ForumPresenter(this, url);

        toolbar.setTitle(getResources().getString(R.string.loading_text));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        (new Thread(() -> {
            final String title = presenter.getTitle();
            runOnUiThread(() -> getSupportActionBar().setTitle(title));
        })).start();

        (new Thread(() -> setupForum())).start();
    }

    @Override
    public void setupForum() {
        final ForumAdapter adapter = presenter.buildAdapter();
        runOnUiThread(() -> {
            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(adapter);
            if (adapter.getItemCount() == 0) {
                status.setText(getResources().getString(R.string.empty_text));
                status.setTextColor(getResources().getColor(R.color.color_accent));
            } else {
                status.setVisibility(TextView.GONE);
            }
        });

        swipeRefreshLayout.setOnRefreshListener(() -> new Handler().postDelayed(() -> (new Thread(() -> {
            presenter.clear();
            final ForumAdapter adapter1 = presenter.buildAdapter();
            runOnUiThread(() -> {
                recyclerView.setAdapter(adapter1);
                swipeRefreshLayout.setRefreshing(false);
            });
        })).start(), 1000));

        final FloatingActionButton actionButton = (FloatingActionButton) findViewById(R.id.fab_forum);
        if (presenter.isCanSendNews()) {
            runOnUiThread(() -> actionButton.setVisibility(FloatingActionButton.VISIBLE));
            actionButton.setOnClickListener(v -> presenter.buildAlertDialog(recyclerView, swipeRefreshLayout));

            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        actionButton.show();
                    }
                    super.onScrollStateChanged(recyclerView, newState);
                }

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    if (dy > 0 || dy < 0 && actionButton.isShown()) {
                        actionButton.hide();
                    }
                }
            });
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
