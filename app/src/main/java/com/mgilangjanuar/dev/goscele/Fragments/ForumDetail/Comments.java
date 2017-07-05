package com.mgilangjanuar.dev.goscele.Fragments.ForumDetail;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mgilangjanuar.dev.goscele.Adapters.ForumDetailCommentAdapter;
import com.mgilangjanuar.dev.goscele.Presenters.ForumDetailPresenter;
import com.mgilangjanuar.dev.goscele.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Comments extends Fragment implements ForumDetailPresenter.ForumDetailServicePresenter {

    private ForumDetailPresenter forumDetailPresenter;
    private FloatingActionButton actionButton;

    @BindView(R.id.recycler_view_forum_detail) RecyclerView recyclerView;
    @BindView(R.id.text_status_forum_comments) TextView status;
    @BindView(R.id.swipe_refresh_forum_detail) SwipeRefreshLayout swipeRefreshLayout;

    public static Comments newInstance(ForumDetailPresenter forumDetailPresenter, FloatingActionButton actionButton) {
        Comments fragment = new Comments();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        fragment.forumDetailPresenter = forumDetailPresenter;
        fragment.actionButton = actionButton;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forum_comments, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        (new Thread(() -> setupForumDetail(view))).start();
    }

    @Override
    public void setupForumDetail(View view) {
        final ForumDetailCommentAdapter adapter = forumDetailPresenter.buildCommentAdapter();

        if (getActivity() == null) {
            return;
        }
        getActivity().runOnUiThread(() -> {
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(adapter);
            if (adapter.getItemCount() == 0) {
                status.setText(getActivity().getResources().getString(R.string.empty_text));
                status.setTextColor(getActivity().getResources().getColor(R.color.color_accent));
            } else {
                status.setVisibility(TextView.GONE);
            }
        });

        swipeRefreshLayout.setOnRefreshListener(() -> new Handler().postDelayed(() -> (new Thread(() -> {
            forumDetailPresenter.clear();
            final ForumDetailCommentAdapter adapter1 = forumDetailPresenter.buildCommentAdapter();
            if (getActivity() == null) {
                return;
            }
            getActivity().runOnUiThread(() -> {
                recyclerView.setAdapter(adapter1);
                swipeRefreshLayout.setRefreshing(false);
            });
        })).start(), 1000));

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
