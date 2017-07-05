package com.mgilangjanuar.dev.goscele.Fragments.Courses;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mgilangjanuar.dev.goscele.Presenters.CoursePresenter;
import com.mgilangjanuar.dev.goscele.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CurrentCourseFragment extends Fragment implements CoursePresenter.CourseServicePresenter {

    private CoursePresenter coursePresenter;

    @BindView(R.id.recycler_view_current_course) RecyclerView recyclerView;

    public static CurrentCourseFragment newInstance() {
        CurrentCourseFragment fragment = new CurrentCourseFragment();
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
        setupCourses(view);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_current_course, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void setupCourses(View view) {
        coursePresenter = new CoursePresenter(getActivity(), view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this.getContext(), LinearLayoutManager.VERTICAL));

        recyclerView.setAdapter(coursePresenter.getCurrentCoursesViewAdapter());
    }
}
