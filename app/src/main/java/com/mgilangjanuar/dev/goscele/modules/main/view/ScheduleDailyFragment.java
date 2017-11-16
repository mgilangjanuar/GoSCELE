package com.mgilangjanuar.dev.goscele.modules.main.view;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mgilangjanuar.dev.goscele.R;
import com.mgilangjanuar.dev.goscele.base.BaseFragment;

public class ScheduleDailyFragment extends BaseFragment {

    public static ScheduleDailyFragment newInstance() {
        ScheduleDailyFragment fragment = new ScheduleDailyFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int findLayout() {
        return R.layout.fragment_schedule_daily;
    }
}
