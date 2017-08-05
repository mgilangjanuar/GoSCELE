package com.mgilangjanuar.dev.goscele.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.mgilangjanuar.dev.goscele.Adapters.BaseTabViewPagerAdapter;
import com.mgilangjanuar.dev.goscele.Fragments.Schedules.DailyFragment;
import com.mgilangjanuar.dev.goscele.Fragments.Schedules.DeadlineFragment;
import com.mgilangjanuar.dev.goscele.Presenters.SchedulePresenter;
import com.mgilangjanuar.dev.goscele.R;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ScheduleFragment extends Fragment implements SchedulePresenter.ScheduleServicePresenter {

    @BindView(R.id.view_pager_fragment_schedule)
    ViewPager viewPager;
    @BindView(R.id.tab_fragment_schedule)
    TabLayout tabLayout;
    @BindView(R.id.recycler_view_schedule)
    RecyclerView recyclerView;
    @BindView(R.id.title_slidingup_panel_schedule)
    TextView tvTitleSlidingUpPanel;
    @BindView(R.id.text_status_schedule)
    TextView tvStatus;
    @BindView(R.id.sliding_layout)
    SlidingUpPanelLayout slidingUpPanelLayout;
    @BindView(R.id.img_detail_description)
    ImageView iViewDetailDescription;
    @BindView(R.id.button_refresh)
    ImageButton buttonRefresh;

    public static ScheduleFragment newInstance() {
        ScheduleFragment fragment = new ScheduleFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schedule, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupSchedule();
    }

    @Override
    public void setupSchedule() {
        BaseTabViewPagerAdapter fragmentPagerAdapter = new BaseTabViewPagerAdapter(getChildFragmentManager());

        DeadlineFragment deadlineFragment = DeadlineFragment.newInstance(recyclerView, tvTitleSlidingUpPanel, tvStatus, slidingUpPanelLayout, iViewDetailDescription);
        fragmentPagerAdapter.addFragment(deadlineFragment, getResources().getString(R.string.title_fragment_deadline));

        DailyFragment dailyFragment = DailyFragment.newInstance(recyclerView, tvTitleSlidingUpPanel, tvStatus, slidingUpPanelLayout, iViewDetailDescription, buttonRefresh);
        fragmentPagerAdapter.addFragment(dailyFragment, getResources().getString(R.string.title_fragment_daily));

        viewPager.setAdapter(fragmentPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getText().equals(getString(R.string.title_fragment_deadline))) {
                    buttonRefresh.setVisibility(ImageButton.GONE);
                    (new Thread(() -> deadlineFragment.setupSchedule())).start();

                } else if (tab.getText().equals(getString(R.string.title_fragment_daily))) {
                    buttonRefresh.setVisibility(ImageButton.VISIBLE);
                    (new Thread(() -> dailyFragment.setupSchedule())).start();
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
}
