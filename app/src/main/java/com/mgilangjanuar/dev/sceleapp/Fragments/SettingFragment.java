package com.mgilangjanuar.dev.sceleapp.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.mgilangjanuar.dev.sceleapp.Models.AccountModel;
import com.mgilangjanuar.dev.sceleapp.Presenters.SettingPresenter;
import com.mgilangjanuar.dev.sceleapp.R;

import java.util.ArrayList;

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
        ListView listView = (ListView) getActivity().findViewById(R.id.list_settings);
        listView.setAdapter(settingPresenter.buildAdapter());
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 4) {            // position 4 == logout
                    settingPresenter.logoutActionHelper();
                } else if (position == 5) {     // position 5 == toggle in-app browser
                    AccountModel accountModel = new AccountModel(getActivity());
                    accountModel.getSavedName();
                    accountModel.getSavedPassword();
                    accountModel.getSavedUsername();
                    accountModel.isUsingInAppBrowser = accountModel.isUsingInAppBrowser() ? false : true;
                    accountModel.save();
                    setupContents(view);
                }
            }
        });
    }
}
