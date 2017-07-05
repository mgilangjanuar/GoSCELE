package com.mgilangjanuar.dev.goscele.Fragments.Courses;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mgilangjanuar.dev.goscele.Adapters.AllCoursesViewAdapter;
import com.mgilangjanuar.dev.goscele.Presenters.CoursePresenter;
import com.mgilangjanuar.dev.goscele.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AllCourseFragment extends Fragment implements CoursePresenter.CourseServicePresenter {

    private CoursePresenter coursePresenter;

    @BindView(R.id.recycler_view_all_course)
    RecyclerView recyclerView;
    @BindView(R.id.swipe_refresh_course)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.text_status_all_course)
    TextView tvStatus;

    public static AllCourseFragment newInstance() {
        AllCourseFragment fragment = new AllCourseFragment();
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
        View view = inflater.inflate(R.layout.fragment_all_course, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void setupCourses(final View view) {
        coursePresenter = new CoursePresenter(getActivity(), view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this.getContext(), LinearLayoutManager.VERTICAL));

        updateAdapter();

        refreshLayout.setOnRefreshListener(() -> new Handler().postDelayed(() -> (new Thread(() -> {
            coursePresenter.clear();
            updateAdapter();
            if (getActivity() == null) {
                return;
            }
            getActivity().runOnUiThread(() -> refreshLayout.setRefreshing(false));
        })).start(), 1000));
    }

    private void updateAdapter() {
        (new Thread(() -> {
            final AllCoursesViewAdapter adapter = coursePresenter.getAllCoursesViewAdapter();

            if (getActivity() == null) {
                return;
            }
            getActivity().runOnUiThread(() -> {
                recyclerView.setAdapter(adapter);
                if (adapter.getItemCount() == 0) {
                    tvStatus.setText(getActivity().getResources().getString(R.string.empty_text));
                    tvStatus.setTextColor(getActivity().getResources().getColor(R.color.color_accent));
                } else {
                    tvStatus.setVisibility(TextView.GONE);
                }
            });
        })).start();
    }
}
