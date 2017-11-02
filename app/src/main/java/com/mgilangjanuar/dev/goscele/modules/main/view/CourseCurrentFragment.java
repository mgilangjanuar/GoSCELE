package com.mgilangjanuar.dev.goscele.modules.main.view;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.mgilangjanuar.dev.goscele.R;
import com.mgilangjanuar.dev.goscele.base.BaseFragment;
import com.mgilangjanuar.dev.goscele.modules.main.adapter.CourseRecyclerViewAdapter;
import com.mgilangjanuar.dev.goscele.modules.main.listener.CourseCurrentListener;
import com.mgilangjanuar.dev.goscele.modules.main.presenter.CoursePresenter;

import butterknife.BindView;

public class CourseCurrentFragment extends BaseFragment implements CourseCurrentListener {

    @BindView(R.id.recycler_view_current_course)
    RecyclerView recyclerView;

    private CoursePresenter presenter;

    @Override
    public int findLayout() {
        return R.layout.fragment_course_current;
    }

    public static BaseFragment newInstance(CoursePresenter presenter) {

        Bundle args = new Bundle();

        CourseCurrentFragment fragment = new CourseCurrentFragment();
        fragment.presenter = presenter;
        presenter.setCurrentListener(fragment);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void initialize(Bundle savedInstanceState) {
        super.initialize(savedInstanceState);
        presenter.runProvider(this);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this.getContext(), LinearLayoutManager.VERTICAL));
    }

    @Override
    public void onRetrieve(CourseRecyclerViewAdapter adapter) {
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onError(String error) {
        showSnackbar(error);
    }
}
