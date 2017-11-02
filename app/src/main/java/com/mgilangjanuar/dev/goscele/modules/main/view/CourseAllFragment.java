package com.mgilangjanuar.dev.goscele.modules.main.view;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.mgilangjanuar.dev.goscele.R;
import com.mgilangjanuar.dev.goscele.base.BaseFragment;
import com.mgilangjanuar.dev.goscele.modules.main.adapter.CourseRecyclerViewAdapter;
import com.mgilangjanuar.dev.goscele.modules.main.listener.CourseAllListener;
import com.mgilangjanuar.dev.goscele.modules.main.presenter.CoursePresenter;

import butterknife.BindView;

public class CourseAllFragment extends BaseFragment implements CourseAllListener {

    @BindView(R.id.recycler_view_all_course)
    RecyclerView recyclerView;

    @BindView(R.id.swipe_refresh_course)
    SwipeRefreshLayout refreshLayout;

    @BindView(R.id.text_status_all_course)
    TextView textStatus;

    private CoursePresenter presenter;

    @Override
    public int findLayout() {
        return R.layout.fragment_course_all;
    }

    public static BaseFragment newInstance(CoursePresenter presenter) {

        Bundle args = new Bundle();

        CourseAllFragment fragment = new CourseAllFragment();
        fragment.presenter = presenter;
        presenter.setAllListener(fragment);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void initialize(Bundle savedInstanceState) {
        super.initialize(savedInstanceState);
        presenter.runProvider(this);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this.getContext(), LinearLayoutManager.VERTICAL));

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.runProvider(CourseAllFragment.this, true);
            }
        });
    }

    @Override
    public void onRetrieve(CourseRecyclerViewAdapter adapter) {
        if (adapter.getItemCount() > 0) {
            textStatus.setVisibility(TextView.GONE);
        } else {
            textStatus.setText(R.string.empty);
        }
        refreshLayout.setRefreshing(false);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onError(String error) {
        showSnackbar(error);
    }
}
