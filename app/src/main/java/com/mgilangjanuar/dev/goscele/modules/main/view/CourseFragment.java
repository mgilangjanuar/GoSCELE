package com.mgilangjanuar.dev.goscele.modules.main.view;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.mgilangjanuar.dev.goscele.R;
import com.mgilangjanuar.dev.goscele.base.BaseFragment;
import com.mgilangjanuar.dev.goscele.modules.main.presenter.CoursePresenter;
import com.mgilangjanuar.dev.goscele.utils.TabPagerAdapterUtil;

import butterknife.BindView;

public class CourseFragment extends BaseFragment {

    @BindView(R.id.view_pager_fragment_course)
    ViewPager viewPager;

    @BindView(R.id.tab_fragment_course)
    TabLayout tabLayout;

    private CoursePresenter presenter = new CoursePresenter();

    public static CourseFragment newInstance() {

        Bundle args = new Bundle();

        CourseFragment fragment = new CourseFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int findLayout() {
        return R.layout.fragment_course;
    }

    @Override
    public void initialize(Bundle savedInstanceState) {
        super.initialize(savedInstanceState);
        final TabPagerAdapterUtil fragmentPagerAdapter = new TabPagerAdapterUtil(getChildFragmentManager());
        fragmentPagerAdapter.addFragment(CourseCurrentFragment.newInstance(presenter), getResources().getString(R.string.title_fragment_current_course));
        fragmentPagerAdapter.addFragment(CourseAllFragment.newInstance(presenter), getResources().getString(R.string.title_fragment_all_course));
        viewPager.setAdapter(fragmentPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getText().toString().equals(getString(R.string.title_fragment_current_course))) {
                    presenter.runProvider(fragmentPagerAdapter.getItem(0));
                } else {
                    presenter.runProvider(fragmentPagerAdapter.getItem(1));
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
