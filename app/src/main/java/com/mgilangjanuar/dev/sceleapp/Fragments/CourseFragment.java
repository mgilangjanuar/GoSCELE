package com.mgilangjanuar.dev.sceleapp.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mgilangjanuar.dev.sceleapp.Adapters.BaseTabViewPagerAdapter;
import com.mgilangjanuar.dev.sceleapp.Fragments.Courses.AllCourseFragment;
import com.mgilangjanuar.dev.sceleapp.Fragments.Courses.CurrentCourseFragment;
import com.mgilangjanuar.dev.sceleapp.MainActivity;
import com.mgilangjanuar.dev.sceleapp.Presenters.CoursePresenter;
import com.mgilangjanuar.dev.sceleapp.R;

public class CourseFragment extends Fragment {

    public CourseFragment() {}

    public static CourseFragment newInstance() {
        CourseFragment fragment = new CourseFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar_course);
        toolbar.setTitle(getActivity().getResources().getString(R.string.title_fragment_course));

        buildTabLayout(view);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_course, container, false);
    }

    private void buildTabLayout(final View view) {
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.view_pager_fragment_course);
        BaseTabViewPagerAdapter fragmentPagerAdapter = new BaseTabViewPagerAdapter(getChildFragmentManager());
        fragmentPagerAdapter.addFragment(CurrentCourseFragment.newInstance(), getResources().getString(R.string.title_fragment_current_course));
        fragmentPagerAdapter.addFragment(AllCourseFragment.newInstance(), getResources().getString(R.string.title_fragment_all_course));
        viewPager.setAdapter(fragmentPagerAdapter);

        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tab_fragment_course);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                updateRecyclerViewAdapter(view, tab.getText().toString());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    public void updateRecyclerViewAdapter(View view, String param) {
        CoursePresenter coursePresenter = new CoursePresenter(getActivity(), view);
        if (param.equalsIgnoreCase(getResources().getString(R.string.title_fragment_all_course))) {
            if (coursePresenter.isDataAllCoursesViewAdapterChanged) {
                RecyclerView recyclerView = (RecyclerView) coursePresenter.getView().findViewById(R.id.recycler_view_all_course);
                recyclerView.setAdapter(coursePresenter.getAllCoursesViewAdapterForce());
                coursePresenter.isDataAllCoursesViewAdapterChanged = false;
            }
        } else {
            if (coursePresenter.isDataCurrentCoursesViewAdapterChanged) {
                RecyclerView recyclerView = (RecyclerView) coursePresenter.getView().findViewById(R.id.recycler_view_current_course);
                recyclerView.setAdapter(coursePresenter.getCurrentCoursesViewAdapterForce());
                coursePresenter.isDataCurrentCoursesViewAdapterChanged = false;
            }
        }
    }
}
