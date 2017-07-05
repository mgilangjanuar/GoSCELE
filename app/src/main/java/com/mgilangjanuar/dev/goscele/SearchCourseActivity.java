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

public class SearchCourseActivity extends BaseActivity {

    CoursePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_course);

        presenter = new CoursePresenter(this, null);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_search_course);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view_search_course);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        final ImageButton submit = (ImageButton) findViewById(R.id.button_search_course);
        final TextView status = (TextView) findViewById(R.id.text_status_search_course);
        final EditText editText = (EditText) findViewById(R.id.edit_search_course);
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
                        final SearchCourseAdapter adapter = presenter.buildSearchAdapter(editText.getText().toString());
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
