package com.mgilangjanuar.dev.goscele.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mgilangjanuar.dev.goscele.Adapters.SettingAdapter;
import com.mgilangjanuar.dev.goscele.Presenters.SettingPresenter;
import com.mgilangjanuar.dev.goscele.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingFragment extends Fragment implements SettingPresenter.SettingServicePresenter {

    private SettingPresenter settingPresenter;

    @BindView(R.id.toolbar_setting)
    Toolbar toolbar;
    @BindView(R.id.recycler_view_setting)
    RecyclerView recyclerView;

    public static SettingFragment newInstance() {
        SettingFragment fragment = new SettingFragment();
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
        toolbar.setTitle(getActivity().getResources().getString(R.string.title_fragment_setting));

        setupContents(view);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void setupContents(View view) {
        settingPresenter = new SettingPresenter(getActivity());

        final SettingAdapter adapter = settingPresenter.buildAdapter();
        if (getActivity() == null) return;
        getActivity().runOnUiThread(() -> {
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), layoutManager.getOrientation()));
            recyclerView.setAdapter(adapter);
        });
    }
}
