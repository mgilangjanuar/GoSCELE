package com.mgilangjanuar.dev.goscele.Fragments.CourseDetail;

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

import com.mgilangjanuar.dev.goscele.Adapters.CourseDetailEventAdapter;
import com.mgilangjanuar.dev.goscele.Presenters.CourseDetailPresenter;
import com.mgilangjanuar.dev.goscele.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EventFragment extends Fragment implements CourseDetailPresenter.CourseDetailServicePresenter {

    @BindView(R.id.recycler_view_course_detail_event)
    RecyclerView recyclerView;
    @BindView(R.id.text_status_course_event)
    TextView tvStatus;
    @BindView(R.id.swipe_refresh_course_detail_event)
    SwipeRefreshLayout swipeRefreshLayout;
    private CourseDetailPresenter presenter;

    public static EventFragment newInstance(CourseDetailPresenter courseDetailPresenter) {
        EventFragment fragment = new EventFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        fragment.presenter = courseDetailPresenter;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_course_detail_event, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        (new Thread(() -> setupCourseDetail(view))).start();
    }

    @Override
    public void setupCourseDetail(View view) {
        final CourseDetailEventAdapter adapter = presenter.buildEventAdapter();

        if (getActivity() == null) return;
        getActivity().runOnUiThread(() -> {
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
            if (recyclerView.getAdapter() == null || !adapter.equals(recyclerView.getAdapter())) {
                recyclerView.setAdapter(adapter);
            }
            if (adapter.getItemCount() == 0) {
                tvStatus.setText(getActivity().getResources().getString(R.string.empty_text));
                tvStatus.setTextColor(getActivity().getResources().getColor(R.color.color_accent));
            } else {
                tvStatus.setVisibility(TextView.GONE);
            }
        });

        swipeRefreshLayout.setOnRefreshListener(() -> new Handler().postDelayed(() -> (new Thread(() -> {
            presenter.clearEvent();
            final CourseDetailEventAdapter adapter1 = presenter.buildEventAdapter();
            if (getActivity() == null) return;
            getActivity().runOnUiThread(() -> {
                recyclerView.setAdapter(adapter1);
                swipeRefreshLayout.setRefreshing(false);
            });

        })).start(), 1000));
    }
}
