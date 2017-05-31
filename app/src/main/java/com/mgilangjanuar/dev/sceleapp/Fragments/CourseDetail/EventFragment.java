package com.mgilangjanuar.dev.sceleapp.Fragments.CourseDetail;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mgilangjanuar.dev.sceleapp.Adapters.CourseDetailEventAdapter;
import com.mgilangjanuar.dev.sceleapp.Presenters.CourseDetailPresenter;
import com.mgilangjanuar.dev.sceleapp.R;

public class EventFragment extends Fragment implements CourseDetailPresenter.CourseDetailServicePresenter {

    CourseDetailPresenter courseDetailPresenter;
    RecyclerView recyclerView;

    public EventFragment() {
        // Required empty public constructor
    }

    public static EventFragment newInstance(CourseDetailPresenter courseDetailPresenter) {
        EventFragment fragment = new EventFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        fragment.courseDetailPresenter = courseDetailPresenter;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_course_detail_event, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        (new Thread(new Runnable() {
            @Override
            public void run() {
                setupCourseDetail(view);
            }
        })).start();
    }

    @Override
    public void setupCourseDetail(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_course_detail_event);
        final TextView status = (TextView) view.findViewById(R.id.text_status_course_event);
        final CourseDetailEventAdapter adapter = courseDetailPresenter.buildEventAdapter();

        if (getActivity() == null) { return; }
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                if (recyclerView.getAdapter() == null || !adapter.equals(recyclerView.getAdapter())) {
                    recyclerView.setAdapter(adapter);
                }
                if (adapter.getItemCount() == 0) {
                    status.setText(getActivity().getResources().getString(R.string.empty_text));
                    status.setTextColor(getActivity().getResources().getColor(R.color.color_accent));
                } else {
                    status.setVisibility(TextView.GONE);
                }
            }
        });

        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_course_detail_event);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        (new Thread(new Runnable() {
                            @Override
                            public void run() {
                                courseDetailPresenter.clearEvent();
                                final CourseDetailEventAdapter adapter = courseDetailPresenter.buildEventAdapter();
                                if (getActivity() == null) { return; }
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        recyclerView.setAdapter(adapter);
                                        swipeRefreshLayout.setRefreshing(false);
                                    }
                                });

                            }
                        })).start();
                    }
                }, 1000);
            }
        });
    }
}
