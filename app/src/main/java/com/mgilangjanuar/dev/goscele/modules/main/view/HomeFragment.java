package com.mgilangjanuar.dev.goscele.modules.main.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.mgilangjanuar.dev.goscele.R;
import com.mgilangjanuar.dev.goscele.base.BaseFragment;
import com.mgilangjanuar.dev.goscele.modules.main.adapter.HomeRecyclerViewAdapter;
import com.mgilangjanuar.dev.goscele.modules.main.listener.HomeListener;
import com.mgilangjanuar.dev.goscele.modules.main.presenter.HomePresenter;

import butterknife.BindView;

/**
 * Created by mgilangjanuar (mgilangjanuar@gmail.com)
 *
 * @since 2017
 */

public class HomeFragment extends BaseFragment implements HomeListener {

    @BindView(R.id.toolbar_home)
    Toolbar toolbar;

    @BindView(R.id.recycler_view_home)
    RecyclerView recyclerView;

    @BindView(R.id.text_status_home)
    TextView textStatus;

    @BindView(R.id.swipe_refresh_home)
    SwipeRefreshLayout swipeRefreshLayout;

    HomePresenter presenter = new HomePresenter(this);

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void initialize(Bundle savedInstanceState) {
        super.initialize(savedInstanceState);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        presenter.runProvider();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.runProvider(true);
            }
        });
    }

    @Override
    public int findLayout() {
        return R.layout.fragment_home;
    }

    @Override
    public Toolbar findToolbar() {
        return toolbar;
    }

    @Override
    public String findTitle() {
        return getString(R.string.title_fragment_home);
    }

    @Override
    public void onRetrieve(HomeRecyclerViewAdapter adapter) {
        if (adapter.getItemCount() > 0) {
            textStatus.setVisibility(TextView.GONE);
        } else {
            textStatus.setText(R.string.empty);
        }
        recyclerView.setAdapter(adapter);
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onError() {
        showSnackbar(R.string.connection_problem);
    }
}
