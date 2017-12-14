package com.mgilangjanuar.dev.goscele.modules.main.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.mgilangjanuar.dev.goscele.R;
import com.mgilangjanuar.dev.goscele.base.BaseFragment;
import com.mgilangjanuar.dev.goscele.modules.forum.list.view.ForumListActivity;
import com.mgilangjanuar.dev.goscele.modules.main.adapter.HomeRecyclerViewAdapter;
import com.mgilangjanuar.dev.goscele.modules.main.listener.HomeListener;
import com.mgilangjanuar.dev.goscele.modules.main.presenter.HomePresenter;

import butterknife.BindView;

/**
 * Created by mgilangjanuar (mgilangjanuar@gmail.com)
 *
 * @since 2017
 */

public class HomeFragment extends BaseFragment implements HomeListener {

    @BindView(R.id.toolbar_home)
    Toolbar toolbar;

    @BindView(R.id.recycler_view_home)
    RecyclerView recyclerView;

    @BindView(R.id.text_status_home)
    TextView textStatus;

    @BindView(R.id.swipe_refresh_home)
    SwipeRefreshLayout swipeRefreshLayout;

    HomePresenter presenter = new HomePresenter(this);

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void initialize(Bundle savedInstanceState) {
        super.initialize(savedInstanceState);
        setHasOptionsMenu(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        presenter.runProvider();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.runProvider(true);
            }
        });
    }

    @Override
    public int findLayout() {
        return R.layout.fragment_home;
    }

    @Override
    public Toolbar findToolbar() {
        return toolbar;
    }

    @Override
    public String findTitle() {
        return getString(R.string.title_fragment_home);
    }

    @Override
    public void onRetrieve(HomeRecyclerViewAdapter adapter) {
        if (adapter.getItemCount() > 0) {
            textStatus.setVisibility(TextView.GONE);
        } else {
            textStatus.setText(R.string.empty);
        }
        recyclerView.setAdapter(adapter);
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onError() {
        showSnackbar(R.string.connection_problem);
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
            case (R.id.academic_announcement):
                url = "https://scele.cs.ui.ac.id/mod/forum/view.php?id=1";
                break;
            case (R.id.forum_curriculum):
                url = "https://scele.cs.ui.ac.id/mod/forum/view.php?id=471";
                break;
            case (R.id.forum_general):
                url = "https://scele.cs.ui.ac.id/mod/forum/view.php?id=2";
                break;
            case (R.id.forum_kp_st_ta):
                url = "https://scele.cs.ui.ac.id/mod/forum/view.php?id=3";
                break;
            case (R.id.forum_library):
                url = "https://scele.cs.ui.ac.id/mod/forum/view.php?id=4";
                break;
            case (R.id.forum_scholarship):
                url = "https://scele.cs.ui.ac.id/mod/forum/view.php?id=5";
                break;
            case (R.id.forum_assistant):
                url = "https://scele.cs.ui.ac.id/mod/forum/view.php?id=5420";
                break;
            case (R.id.forum_feedback):
                url = "https://scele.cs.ui.ac.id/mod/forum/view.php?id=6";
                break;
            case (R.id.forum_suggestion):
                url = "https://scele.cs.ui.ac.id/mod/forum/view.php?id=7";
                break;
            case (R.id.forum_lounge):
                url = "https://scele.cs.ui.ac.id/mod/forum/view.php?id=8";
                break;
            case (R.id.forum_question_answer):
                url = "https://scele.cs.ui.ac.id/mod/forum/view.php?id=9";
                break;
            case (R.id.forum_vacancy):
                url = "https://scele.cs.ui.ac.id/mod/forum/view.php?id=10";
                break;
            case (R.id.forum_academic_rule):
                url = "https://scele.cs.ui.ac.id/mod/forum/view.php?id=11";
                break;
            case (R.id.forum_lost_found):
                url = "https://scele.cs.ui.ac.id/mod/forum/view.php?id=12";
                break;
            case (R.id.forum_ukm):
                url = "https://scele.cs.ui.ac.id/mod/forum/view.php?id=13";
                break;
            case (R.id.forum_competition_info):
                url = "https://scele.cs.ui.ac.id/mod/forum/view.php?id=14";
                break;
            case (R.id.forum_graduate_info):
                url = "https://scele.cs.ui.ac.id/mod/forum/view.php?id=15";
                break;
            case (R.id.forum_helpdesk):
                url = "https://scele.cs.ui.ac.id/mod/forum/view.php?id=16";
                break;
            case (R.id.forum_labsitter):
                url = "https://scele.cs.ui.ac.id/mod/forum/view.php?id=17";
                break;
        }
        Intent intent = new Intent(getActivity(), ForumListActivity.class).putExtra("url", url);
        startActivity(intent);
        return super.onOptionsItemSelected(item);
    }
}
