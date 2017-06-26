package com.mgilangjanuar.dev.goscele.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mgilangjanuar.dev.goscele.Adapters.HomePostAdapter;
import com.mgilangjanuar.dev.goscele.ForumActivity;
import com.mgilangjanuar.dev.goscele.MainActivity;
import com.mgilangjanuar.dev.goscele.Presenters.HomePresenter;
import com.mgilangjanuar.dev.goscele.R;

public class HomeFragment extends Fragment implements HomePresenter.HomeServicePresenter {

    HomePresenter homePresenter;
    RecyclerView recyclerView;

    public HomeFragment() {}

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar_home);
        toolbar.setTitle(getActivity().getResources().getString(R.string.title_fragment_home_alt));
        ((MainActivity) getActivity()).setSupportActionBar(toolbar);

        (new Thread(new Runnable() {
            @Override
            public void run() {
                setupHome(view);
            }
        })).start();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void setupHome(View view) {
        if (getActivity() == null) { return; }
        homePresenter = new HomePresenter(getActivity(), view);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_home);

        final HomePostAdapter adapter = homePresenter.buildAdapter();
        final TextView status = (TextView) view.findViewById(R.id.text_status_home);

        if (getActivity() == null) { return; }
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(adapter);
                if (adapter.getItemCount() == 0) {
                    status.setText(getActivity().getResources().getString(R.string.empty_text));
                    status.setTextColor(getActivity().getResources().getColor(R.color.color_accent));
                } else {
                    status.setVisibility(TextView.GONE);
                }
            }
        });

        final SwipeRefreshLayout refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_home);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        (new Thread(new Runnable() {
                            @Override
                            public void run() {
                                homePresenter.clear();
                                final HomePostAdapter adapter = homePresenter.buildAdapter();
                                if (getActivity() == null) { return; }
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        recyclerView.setAdapter(adapter);
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.home_forum_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String url = "";
        switch (item.getItemId()) {
            case (R.id.item_pengumuman_akademis):
                url = "https://scele.cs.ui.ac.id/mod/forum/view.php?id=1";
                break;
            case (R.id.item_forum_kurikulum_2016):
                url = "https://scele.cs.ui.ac.id/mod/forum/view.php?id=471";
                break;
            case (R.id.item_forum_umum):
                url = "https://scele.cs.ui.ac.id/mod/forum/view.php?id=2";
                break;
            case (R.id.item_forum_kp_st_ta):
                url = "https://scele.cs.ui.ac.id/mod/forum/view.php?id=3";
                break;
            case (R.id.item_forum_perpustakaan):
                url = "https://scele.cs.ui.ac.id/mod/forum/view.php?id=4";
                break;
            case (R.id.item_forum_beasiswa):
                url = "https://scele.cs.ui.ac.id/mod/forum/view.php?id=5";
                break;
            case (R.id.item_forum_asisten):
                url = "https://scele.cs.ui.ac.id/mod/forum/view.php?id=5420";
                break;
            case (R.id.item_forum_feedback):
                url = "https://scele.cs.ui.ac.id/mod/forum/view.php?id=6";
                break;
            case (R.id.item_forum_usul):
                url = "https://scele.cs.ui.ac.id/mod/forum/view.php?id=7";
                break;
            case (R.id.item_forum_santai):
                url = "https://scele.cs.ui.ac.id/mod/forum/view.php?id=8";
                break;
            case (R.id.item_forum_tanya_jawab):
                url = "https://scele.cs.ui.ac.id/mod/forum/view.php?id=9";
                break;
            case (R.id.item_forum_lowongan):
                url = "https://scele.cs.ui.ac.id/mod/forum/view.php?id=10";
                break;
            case (R.id.item_forum_peraturan_akademis):
                url = "https://scele.cs.ui.ac.id/mod/forum/view.php?id=11";
                break;
            case (R.id.item_forum_lost_and_found):
                url = "https://scele.cs.ui.ac.id/mod/forum/view.php?id=12";
                break;
            case (R.id.item_forum_ukm):
                url = "https://scele.cs.ui.ac.id/mod/forum/view.php?id=13";
                break;
            case (R.id.item_forum_kompetisi):
                url = "https://scele.cs.ui.ac.id/mod/forum/view.php?id=14";
                break;
            case (R.id.item_forum_informasi_wisudawan):
                url = "https://scele.cs.ui.ac.id/mod/forum/view.php?id=15";
                break;
            case (R.id.item_forum_helpdesk):
                url = "https://scele.cs.ui.ac.id/mod/forum/view.php?id=16";
                break;
            case (R.id.item_forum_labsitter):
                url = "https://scele.cs.ui.ac.id/mod/forum/view.php?id=17";
                break;
        }
        Intent intent = new Intent(getActivity(), ForumActivity.class).putExtra("url", url);
        startActivity(intent);
        return super.onOptionsItemSelected(item);
    }
}
