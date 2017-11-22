package com.mgilangjanuar.dev.goscele.modules.main.view;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.mgilangjanuar.dev.goscele.R;
import com.mgilangjanuar.dev.goscele.base.BaseActivity;
import com.mgilangjanuar.dev.goscele.base.BaseFragment;
import com.mgilangjanuar.dev.goscele.modules.main.adapter.ScheduleDailyDetailRecyclerViewAdapter;
import com.mgilangjanuar.dev.goscele.modules.main.adapter.ScheduleDailyRecyclerViewAdapter;
import com.mgilangjanuar.dev.goscele.modules.main.listener.ScheduleDailyDetailListener;
import com.mgilangjanuar.dev.goscele.modules.main.listener.ScheduleDailyListener;
import com.mgilangjanuar.dev.goscele.modules.main.presenter.ScheduleDailyPresenter;

import butterknife.BindView;
import butterknife.OnClick;

public class ScheduleDailyFragment extends BaseFragment implements ScheduleDailyListener, ScheduleDailyDetailListener {

    @BindView(R.id.recycler_view_daily_schedule)
    RecyclerView recyclerViewDaily;

    @BindView(R.id.siak_status)
    LinearLayout siakStatusLayout;

    @BindView(R.id.button_sync)
    Button buttonSync;

    private ImageButton buttonRefresh;
    private RecyclerView recyclerViewDetail;

    private ProgressDialog progressDialog;

    private ScheduleDailyPresenter presenter = new ScheduleDailyPresenter(this, this);

    public static ScheduleDailyFragment newInstance(ImageButton buttonRefresh, RecyclerView recyclerView) {
        ScheduleDailyFragment fragment = new ScheduleDailyFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        fragment.buttonRefresh = buttonRefresh;
        fragment.recyclerViewDetail = recyclerView;
        return fragment;
    }

    @Override
    public int findLayout() {
        return R.layout.fragment_schedule_daily;
    }

    @Override
    public void initialize(Bundle savedInstanceState) {
        super.initialize(savedInstanceState);
        recyclerViewDaily.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        recyclerViewDaily.setItemAnimator(new DefaultItemAnimator());

        recyclerViewDetail.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        recyclerViewDetail.setItemAnimator(new DefaultItemAnimator());

        buttonRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.clearDailySchedule();

                progressDialog = ((BaseActivity) getActivity()).showProgress(R.string.loading);
                progressDialog.show();
                presenter.runProvider();
            }
        });

        if (presenter.isModelEmpty()) {
            siakStatusLayout.setVisibility(LinearLayout.VISIBLE);
        } else {
            presenter.runProvider();
        }
    }

    @OnClick(R.id.button_sync)
    public void onSync() {
        progressDialog = ((BaseActivity) getActivity()).showProgress(R.string.loading);
        progressDialog.show();
        presenter.runProvider();
    }

    @Override
    public void onRetrieveDailySchedule(ScheduleDailyRecyclerViewAdapter adapter) {
        if (progressDialog != null && progressDialog.isShowing()) progressDialog.cancel();
        recyclerViewDaily.setAdapter(adapter);
        siakStatusLayout.setVisibility(LinearLayout.GONE);
    }

    @Override
    public void onRetrieveCourseDetail(ScheduleDailyDetailRecyclerViewAdapter adapter) {
        recyclerViewDetail.setAdapter(adapter);
    }

    @Override
    public void onError(String error) {
        if (getActivity() != null) ((BaseActivity) getActivity()).showSnackbar(error);
        if (progressDialog != null && progressDialog.isShowing()) progressDialog.cancel();
    }
}
