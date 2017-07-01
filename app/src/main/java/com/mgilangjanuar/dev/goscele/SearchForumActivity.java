package com.mgilangjanuar.dev.goscele;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.mgilangjanuar.dev.goscele.Adapters.ForumAdapter;
import com.mgilangjanuar.dev.goscele.Presenters.ForumPresenter;

public class SearchForumActivity extends BaseActivity {

    ForumPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_forum);

        presenter = new ForumPresenter(this, null);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_search_forum);
        toolbar.setTitle(getString(R.string.title_activity_search_forum));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view_search_forum);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        final ImageButton submit = (ImageButton) findViewById(R.id.button_search_forum);
        final TextView status = (TextView) findViewById(R.id.text_status_search_forum);
        final EditText editText = (EditText) findViewById(R.id.edit_search_forum);
        editText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    submit.performClick();
                    return true;
                }
                return false;
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                recyclerView.setVisibility(RecyclerView.GONE);
                status.setVisibility(TextView.VISIBLE);
                status.setText(getString(R.string.loading_text));
                status.setTextColor(getResources().getColor(android.R.color.darker_gray));
                (new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final ForumAdapter adapter = presenter.buildSearchAdapter(editText.getText().toString());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                recyclerView.setVisibility(RecyclerView.VISIBLE);
                                recyclerView.setAdapter(adapter);
                                if (recyclerView.getAdapter().getItemCount() == 0) {
                                    status.setText(getString(R.string.empty_text));
                                    status.setTextColor(getResources().getColor(R.color.color_accent));
                                } else {
                                    status.setVisibility(TextView.GONE);
                                }
                            }
                        });
                    }
                })).start();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
