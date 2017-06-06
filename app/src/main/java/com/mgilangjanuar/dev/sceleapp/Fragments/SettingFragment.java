package com.mgilangjanuar.dev.sceleapp.Fragments;

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
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.mgilangjanuar.dev.sceleapp.Adapters.SettingAdapter;
import com.mgilangjanuar.dev.sceleapp.Models.AccountModel;
import com.mgilangjanuar.dev.sceleapp.Presenters.SettingPresenter;
import com.mgilangjanuar.dev.sceleapp.R;

import java.util.ArrayList;
import java.util.Map;

public class SettingFragment extends Fragment implements SettingPresenter.SettingServicePresenter {

    private SettingPresenter settingPresenter;

    public SettingFragment() {}

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
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar_setting);
        toolbar.setTitle(getActivity().getResources().getString(R.string.title_fragment_setting));

        setupContents(view);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_setting, container, false);
    }

    @Override
    public void setupContents(View view) {
        settingPresenter = new SettingPresenter(getActivity(), view);

        final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_setting);
        final SettingAdapter adapter = settingPresenter.buildAdapter();
        if (getActivity() == null) { return; }
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), layoutManager.getOrientation()));
                recyclerView.setAdapter(adapter);
            }
        });
    }
}
