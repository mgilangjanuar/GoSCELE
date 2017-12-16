package com.mgilangjanuar.dev.goscele.modules.forum.list.view;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.mgilangjanuar.dev.goscele.R;
import com.mgilangjanuar.dev.goscele.base.BaseActivity;
import com.mgilangjanuar.dev.goscele.modules.forum.list.adapter.ForumListRecyclerViewAdapter;
import com.mgilangjanuar.dev.goscele.modules.forum.list.listener.ForumListListener;
import com.mgilangjanuar.dev.goscele.modules.forum.list.presenter.ForumSearchPresenter;

import butterknife.BindView;
import butterknife.OnClick;

public class ForumSearchActivity extends BaseActivity implements ForumListListener {

    @BindView(R.id.edit_search_forum)
    EditText search;

    @BindView(R.id.text_status_search_forum)
    TextView status;

    @BindView(R.id.recycler_view_search_forum)
    RecyclerView forums;

    private ForumSearchPresenter presenter = new ForumSearchPresenter(this);

    @Override
    public void initialize(Bundle savedInstanceState) {
        super.initialize(savedInstanceState);
        forums.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        forums.setItemAnimator(new DefaultItemAnimator());
        forums.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        search.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    search();
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public int findLayout() {
        return R.layout.activity_forum_search;
    }

    @OnClick(R.id.button_search_forum)
    public void search() {
        hideKeyboard();
        forums.setAdapter(presenter.buildEmptyAdapter());
        status.setVisibility(TextView.VISIBLE);
        status.setText(R.string.loading);
        presenter.search(search.getText().toString());
    }

    @Override
    public void onRetrieve(ForumListRecyclerViewAdapter adapter) {
        if (adapter.getItemCount() == 0) {
            status.setVisibility(TextView.VISIBLE);
            status.setText(R.string.empty);
        } else {
            status.setVisibility(TextView.GONE);
            forums.setAdapter(adapter);
        }
    }

    @Override
    public void onGetTitle(String title) {

    }

    @Override
    public void onError(String error) {
        status.setVisibility(TextView.GONE);
        showSnackbar(error);
    }
}
