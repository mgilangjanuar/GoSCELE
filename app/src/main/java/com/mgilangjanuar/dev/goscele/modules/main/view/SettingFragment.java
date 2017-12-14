package com.mgilangjanuar.dev.goscele.modules.main.view;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.mgilangjanuar.dev.goscele.R;
import com.mgilangjanuar.dev.goscele.base.BaseFragment;
import com.mgilangjanuar.dev.goscele.modules.main.presenter.SettingPresenter;

import butterknife.BindView;

public class SettingFragment extends BaseFragment {

    @BindView(R.id.toolbar_setting)
    Toolbar toolbar;

    @BindView(R.id.recycler_view_setting)
    RecyclerView recyclerView;

    private SettingPresenter presenter = new SettingPresenter();

    public static SettingFragment newInstance() {
        SettingFragment fragment = new SettingFragment();
        return fragment;
    }

    @Override
    public void initialize(Bundle savedInstanceState) {
        super.initialize(savedInstanceState);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(presenter.buildAdapter());
    }

    @Override
    public int findLayout() {
        return R.layout.fragment_setting;
    }

    @Override
    public Toolbar findToolbar() {
        return toolbar;
    }

    @Override
    public String findTitle() {
        return getString(R.string.settings_title_fragment);
    }
}
