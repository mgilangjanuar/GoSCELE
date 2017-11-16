package com.mgilangjanuar.dev.goscele.modules.main.view;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.mgilangjanuar.dev.goscele.R;
import com.mgilangjanuar.dev.goscele.base.BaseFragment;
import com.mgilangjanuar.dev.goscele.utils.TabPagerAdapterUtil;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import butterknife.BindView;

public class ScheduleFragment extends BaseFragment {

    @BindView(R.id.view_pager_fragment_schedule)
    ViewPager viewPager;

    @BindView(R.id.tab_fragment_schedule)
    TabLayout tabLayout;

    @BindView(R.id.recycler_view_schedule)
    RecyclerView recyclerView;

    @BindView(R.id.title_slidingup_panel_schedule)
    TextView titleSlidingUpPanel;

    @BindView(R.id.text_status_schedule)
    TextView status;

    @BindView(R.id.sliding_layout)
    SlidingUpPanelLayout slidingUpPanelLayout;

    @BindView(R.id.img_detail_description)
    ImageView viewDetailDescription;

    @BindView(R.id.button_refresh)
    ImageButton buttonRefresh;

    public static ScheduleFragment newInstance() {

        Bundle args = new Bundle();

        ScheduleFragment fragment = new ScheduleFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int findLayout() {
        return R.layout.fragment_schedule;
    }

    @Override
    public void initialize(Bundle savedInstanceState) {
        super.initialize(savedInstanceState);
        final TabPagerAdapterUtil fragmentPagerAdapter = new TabPagerAdapterUtil(getChildFragmentManager());

        fragmentPagerAdapter.addFragment(ScheduleDeadlineFragment.newInstance(titleSlidingUpPanel, recyclerView, slidingUpPanelLayout, status), getString(R.string.title_fragment_deadline));
        fragmentPagerAdapter.addFragment(ScheduleDailyFragment.newInstance(), getString(R.string.title_fragment_daily));

        viewPager.setAdapter(fragmentPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);


        slidingUpPanelLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                viewDetailDescription.setImageResource(slideOffset > 0.5 ? R.drawable.ic_sliding_down : R.drawable.ic_sliding_up);
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {

            }
        });
    }

    @Override
    public String findTitle() {
        return getString(R.string.schedule_fragment_title);
    }
}
