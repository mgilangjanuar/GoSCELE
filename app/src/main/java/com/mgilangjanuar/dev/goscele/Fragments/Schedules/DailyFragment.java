package com.mgilangjanuar.dev.goscele.Fragments.Schedules;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.mgilangjanuar.dev.goscele.Adapters.ScheduleDailyAdapter;
import com.mgilangjanuar.dev.goscele.AuthActivity;
import com.mgilangjanuar.dev.goscele.BaseActivity;
import com.mgilangjanuar.dev.goscele.MainActivity;
import com.mgilangjanuar.dev.goscele.Presenters.AuthPresenter;
import com.mgilangjanuar.dev.goscele.Presenters.SchedulePresenter;
import com.mgilangjanuar.dev.goscele.R;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DailyFragment extends Fragment implements SchedulePresenter.ScheduleServicePresenter {

    @BindView(R.id.recycler_view_daily_schedule)
    RecyclerView recyclerViewDaily;

    RecyclerView recyclerView;
    TextView tvTitleSlidingUpPanel;
    TextView tvStatus;
    SlidingUpPanelLayout slidingUpPanelLayout;
    ImageView iViewDetailDescription;
    ImageButton buttonRefresh;

    private ProgressDialog progress;
    private SchedulePresenter presenter;

    public static DailyFragment newInstance(RecyclerView recyclerView, TextView tvTitleSlidingUpPanel, TextView tvStatus, SlidingUpPanelLayout slidingUpPanelLayout, ImageView iViewDetailDescription, ImageButton buttonRefresh) {
        DailyFragment fragment = new DailyFragment();
        fragment.recyclerView = recyclerView;
        fragment.tvTitleSlidingUpPanel = tvTitleSlidingUpPanel;
        fragment.tvStatus = tvStatus;
        fragment.slidingUpPanelLayout = slidingUpPanelLayout;
        fragment.iViewDetailDescription = iViewDetailDescription;
        fragment.buttonRefresh = buttonRefresh;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_daily, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter = new SchedulePresenter(getActivity());
        progress = new ProgressDialog(getActivity());
    }


    @Override
    public void setupSchedule() {
        setAdapter();

        getActivity().runOnUiThread(() -> {
            progress.setMessage("Loading...");
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setIndeterminate(false);
            progress.setCancelable(false);
        });
        buttonRefresh.setOnClickListener(v -> {
            progress.show();
            (new Thread(() -> {
                if (presenter.refreshScheduleDaily()) {
                    getActivity().runOnUiThread(() -> recyclerViewDaily.setVisibility(RecyclerView.GONE));
                    setAdapter();
                } else {
                    getActivity().runOnUiThread(() -> {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setCancelable(false);
                        builder.setTitle("Re-fetch Data");
                        builder.setMessage("This action need your credential. Want to continue?");
                        builder.setInverseBackgroundForced(true);
                        builder.setPositiveButton("Yes", (dialog, which) -> {
                            AuthPresenter authPresenter = new AuthPresenter(getActivity());
                            authPresenter.showProgressDialog();
                            (new Thread(() -> {
                                boolean isLogout = authPresenter.logout();
                                getActivity().runOnUiThread(() -> {
                                    if (isLogout) {
                                        ((BaseActivity) getActivity()).forceRedirect(new Intent(getContext(), AuthActivity.class));
                                    }
                                    authPresenter.dismissProgressDialog();
                                });
                            })).start();
                        });
                        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
                        final AlertDialog alert = builder.create();

                        alert.setOnShowListener(arg0 -> {
                            alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.DKGRAY);
                            alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.DKGRAY);
                        });
                        alert.show();
                    });
                }
                progress.cancel();
            })).start();
        });
    }

    private void setAdapter() {
        ScheduleDailyAdapter adapter = presenter.buildScheduleDailyAdapter();

        if (getActivity() == null) return;
        getActivity().runOnUiThread(() -> {
            tvTitleSlidingUpPanel.setText("Courses Detail");
            if (adapter != null) {
                recyclerViewDaily.setVisibility(RecyclerView.VISIBLE);
                recyclerViewDaily.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
                recyclerViewDaily.setItemAnimator(new DefaultItemAnimator());
                recyclerViewDaily.setAdapter(adapter);
            }
        });
    }
}
