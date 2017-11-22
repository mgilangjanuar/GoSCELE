package com.mgilangjanuar.dev.goscele.modules.main.view;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
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

    @BindView(R.id.recycler_view_deadline)
    RecyclerView recyclerViewDeadline;

    @BindView(R.id.recycler_view_daily)
    RecyclerView recyclerViewDaily;

    @BindView(R.id.title_slidingup_panel_schedule)
    TextView titleSlidingUpPanel;

    @BindView(R.id.title_slidingup_panel_schedule_course)
    TextView titleSlidingUpPanelCourse;

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

        fragmentPagerAdapter.addFragment(ScheduleDeadlineFragment.newInstance(titleSlidingUpPanel, recyclerViewDeadline, slidingUpPanelLayout, status), getString(R.string.title_fragment_deadline));
        fragmentPagerAdapter.addFragment(ScheduleDailyFragment.newInstance(buttonRefresh, recyclerViewDaily), getString(R.string.title_fragment_daily));

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

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getText().toString().equals(getString(R.string.title_fragment_deadline))) {
                    buttonRefresh.setVisibility(ImageButton.GONE);

                    recyclerViewDeadline.setVisibility(RecyclerView.VISIBLE);
                    recyclerViewDaily.setVisibility(RecyclerView.GONE);
                    slidingUpPanelLayout.setScrollableView(recyclerViewDeadline);

                    titleSlidingUpPanel.setVisibility(TextView.VISIBLE);
                    titleSlidingUpPanelCourse.setVisibility(TextView.GONE);

                    status.setVisibility(TextView.VISIBLE);
                } else {
                    buttonRefresh.setVisibility(ImageButton.VISIBLE);

                    recyclerViewDeadline.setVisibility(RecyclerView.GONE);
                    recyclerViewDaily.setVisibility(RecyclerView.VISIBLE);
                    slidingUpPanelLayout.setScrollableView(recyclerViewDaily);

                    titleSlidingUpPanel.setVisibility(TextView.GONE);
                    titleSlidingUpPanelCourse.setVisibility(TextView.VISIBLE);

                    status.setVisibility(TextView.GONE);
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
