package com.mgilangjanuar.dev.goscele;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.mgilangjanuar.dev.goscele.Adapters.SearchCourseAdapter;
import com.mgilangjanuar.dev.goscele.Presenters.CoursePresenter;

import butterknife.BindView;

public class SearchCourseActivity extends BaseActivity {

    private CoursePresenter presenter;

    @BindView(R.id.toolbar_search_course) Toolbar toolbar;
    @BindView(R.id.recycler_view_search_course) RecyclerView recyclerView;
    @BindView(R.id.button_search_course) ImageButton iBtnSearch;
    @BindView(R.id.text_status_search_course) TextView tvStatus;
    @BindView(R.id.edit_search_course) EditText etSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_course);

        presenter = new CoursePresenter(this, null);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        etSearch.setOnKeyListener((v, keyCode, event) -> {
            if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                iBtnSearch.performClick();
                return true;
            }
            return false;
        });

        iBtnSearch.setOnClickListener(v -> {
            hideKeyboard();
            recyclerView.setVisibility(RecyclerView.GONE);
            tvStatus.setVisibility(TextView.VISIBLE);
            tvStatus.setText(getString(R.string.loading_text));
            tvStatus.setTextColor(getResources().getColor(android.R.color.darker_gray));
            (new Thread(() -> {
                final SearchCourseAdapter adapter = presenter.buildSearchAdapter(etSearch.getText().toString());
                runOnUiThread(() -> {
                    recyclerView.setVisibility(RecyclerView.VISIBLE);
                    recyclerView.setAdapter(adapter);
                    if (recyclerView.getAdapter().getItemCount() == 0) {
                        tvStatus.setText(getString(R.string.empty_text));
                        tvStatus.setTextColor(getResources().getColor(R.color.color_accent));
                    } else {
                        tvStatus.setVisibility(TextView.GONE);
                    }
                });
            })).start();
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
