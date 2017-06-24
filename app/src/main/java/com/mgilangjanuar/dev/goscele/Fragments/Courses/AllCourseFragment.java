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

public class AllCourseFragment extends Fragment implements CoursePresenter.CourseServicePresenter {

    CoursePresenter coursePresenter;
    RecyclerView recyclerView;

    public AllCourseFragment() {
        // Required empty public constructor
    }

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
        return inflater.inflate(R.layout.fragment_all_course, container, false);
    }

    @Override
    public void setupCourses(final View view) {
        coursePresenter = new CoursePresenter(getActivity(), view);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_all_course);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this.getContext(), LinearLayoutManager.VERTICAL));

        updateAdapter(view);

        final SwipeRefreshLayout refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_course);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        (new Thread(new Runnable() {
                            @Override
                            public void run() {
                                coursePresenter.clear();
                                updateAdapter(view);
                                if (getActivity() == null) { return; }
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        refreshLayout.setRefreshing(false);
                                    }
                                });
                            }
                        })).start();
                    }
                }, 1000);
            }
        });
    }

    private void updateAdapter(final View view) {
        (new Thread(new Runnable() {
            @Override
            public void run() {
                final AllCoursesViewAdapter adapter = coursePresenter.getAllCoursesViewAdapter();
                final TextView status = (TextView) view.findViewById(R.id.text_status_all_course);

                if (getActivity() == null) { return; }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.setAdapter(adapter);
                        if (adapter.getItemCount() == 0) {
                            status.setText(getActivity().getResources().getString(R.string.empty_text));
                            status.setTextColor(getActivity().getResources().getColor(R.color.color_accent));
                        } else {
                            status.setVisibility(TextView.GONE);
                        }
                    }
                });
            }
        })).start();
    }
}
