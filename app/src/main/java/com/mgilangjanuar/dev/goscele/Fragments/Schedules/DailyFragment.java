package com.mgilangjanuar.dev.goscele.Fragments.Schedules;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mgilangjanuar.dev.goscele.R;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

public class DailyFragment extends Fragment {

    RecyclerView recyclerView;
    TextView tvTitleSlidingUpPanel;
    TextView tvStatus;
    SlidingUpPanelLayout slidingUpPanelLayout;
    ImageView iViewDetailDescription;

    public static DailyFragment newInstance(RecyclerView recyclerView, TextView tvTitleSlidingUpPanel, TextView tvStatus, SlidingUpPanelLayout slidingUpPanelLayout, ImageView iViewDetailDescription) {
        DailyFragment fragment = new DailyFragment();
        fragment.recyclerView = recyclerView;
        fragment.tvTitleSlidingUpPanel = tvTitleSlidingUpPanel;
        fragment.tvStatus = tvStatus;
        fragment.slidingUpPanelLayout = slidingUpPanelLayout;
        fragment.iViewDetailDescription = iViewDetailDescription;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_daily, container, false);
    }
}
