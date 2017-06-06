package com.mgilangjanuar.dev.sceleapp;

import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;

import com.mgilangjanuar.dev.sceleapp.Adapters.ForumAdapter;
import com.mgilangjanuar.dev.sceleapp.Presenters.ForumPresenter;

public class Forum extends AppCompatActivity implements ForumPresenter.ForumServicePresenter {

    ForumPresenter forumPresenter;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum);

        Bundle bundle = getIntent().getExtras();
        if (bundle == null || bundle.getString("url") == null) {
            Toast.makeText(this, "Broken URL", Toast.LENGTH_SHORT).show();
            onBackPressed();
            return;
        }
        url = bundle.getString("url");

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_forum);
        toolbar.setTitle(getResources().getString(R.string.loading_text));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        (new Thread(new Runnable() {
            @Override
            public void run() {
                setupForum();
            }
        })).start();
    }

    @Override
    public void setupForum() {
        forumPresenter = new ForumPresenter(this, url);

        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view_forum);
        final ForumAdapter adapter = forumPresenter.buildAdapter();
        final String title = forumPresenter.getTitle();
        final TextView status = (TextView) findViewById(R.id.text_status_forum);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                getSupportActionBar().setTitle(title);

                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(adapter);
                if (adapter.getItemCount() == 0) {
                    status.setText(getResources().getString(R.string.empty_text));
                    status.setTextColor(getResources().getColor(R.color.color_accent));
                } else {
                    status.setVisibility(TextView.GONE);
                }
            }
        });

        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_forum);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        (new Thread(new Runnable() {
                            @Override
                            public void run() {
                                forumPresenter.clear();
                                final ForumAdapter adapter = forumPresenter.buildAdapter();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        recyclerView.setAdapter(adapter);
                                        swipeRefreshLayout.setRefreshing(false);
                                    }
                                });
                            }
                        })).start();
                    }
                }, 1000);
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
