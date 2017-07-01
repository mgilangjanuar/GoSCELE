package com.mgilangjanuar.dev.goscele;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mgilangjanuar.dev.goscele.Adapters.ForumAdapter;
import com.mgilangjanuar.dev.goscele.Presenters.ForumPresenter;

public class ForumActivity extends AppCompatActivity implements ForumPresenter.ForumServicePresenter {

    ForumPresenter forumPresenter;
    String url;

    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum);

        Bundle bundle = getIntent().getExtras();
        if (bundle == null || bundle.getString("url") == null) {
            Toast.makeText(this, "Broken URL", Toast.LENGTH_SHORT).show();
            onBackPressed();
            return;
        }
        url = bundle.getString("url");
        forumPresenter = new ForumPresenter(this, url);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_forum);
        toolbar.setTitle(getResources().getString(R.string.loading_text));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        (new Thread(new Runnable() {
            @Override
            public void run() {
                final String title = forumPresenter.getTitle();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        getSupportActionBar().setTitle(title);
                    }
                });
            }
        })).start();

        (new Thread(new Runnable() {
            @Override
            public void run() {
                setupForum();
            }
        })).start();
    }

    @Override
    public void setupForum() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_forum);
        final ForumAdapter adapter = forumPresenter.buildAdapter();
        final TextView status = (TextView) findViewById(R.id.text_status_forum);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(adapter);
                if (adapter.getItemCount() == 0) {
                    status.setText(getResources().getString(R.string.empty_text));
                    status.setTextColor(getResources().getColor(R.color.color_accent));
                } else {
                    status.setVisibility(TextView.GONE);
                }
            }
        });

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_forum);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        (new Thread(new Runnable() {
                            @Override
                            public void run() {
                                forumPresenter.clear();
                                final ForumAdapter adapter = forumPresenter.buildAdapter();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        recyclerView.setAdapter(adapter);
                                        swipeRefreshLayout.setRefreshing(false);
                                    }
                                });
                            }
                        })).start();
                    }
                }, 1000);
            }
        });

        final FloatingActionButton actionButton = (FloatingActionButton) findViewById(R.id.fab_forum);
        if (forumPresenter.isCanSendNews()) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    actionButton.setVisibility(FloatingActionButton.VISIBLE);
                }
            });
            actionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    buildAlertDialog();
                }
            });

            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        actionButton.show();
                    }
                    super.onScrollStateChanged(recyclerView, newState);
                }

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    if (dy > 0 || dy < 0 && actionButton.isShown()) {
                        actionButton.hide();
                    }
                }
            });
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void buildAlertDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Add New Thread");
        alert.setMessage("Write your thread here:");

        final EditText title = new EditText(this);
        title.setSingleLine(true);
        title.setHint("Subject");

        final EditText message = new EditText(this);
        message.setSingleLine(false);
        message.setMaxLines(7);
        message.setHint("Content");

        LinearLayout container = new LinearLayout(this);
        container.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams params = new  LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.leftMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);
        params.rightMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);
        title.setLayoutParams(params);
        container.addView(title);
        message.setLayoutParams(params);
        container.addView(message);

        alert.setView(container);

        alert.setPositiveButton("Send", null);

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
            }
        });

        final AlertDialog alertDialog = alert.create();

        alertDialog.setOnShowListener( new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.DKGRAY);
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.DKGRAY);

                Button button = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        final String titleText = title.getText().toString().trim();
                        final String messageText = message.getText().toString().replaceAll("\\n", "<br />");
                        if (titleText.equals("") || messageText.equals("")) {
                            Toast.makeText(getApplicationContext(), "Title and message are required fields", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Please wait...", Toast.LENGTH_LONG).show();
                            (new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    forumPresenter.sendNews(titleText, messageText);
                                    refreshingForum();
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(getApplicationContext(), "Sent!", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            })).start();
                            alertDialog.dismiss();
                        }
                    }
                });
            }
        });
        alertDialog.show();
    }

    public void refreshingForum() {
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                (new Thread(new Runnable() {
                    @Override
                    public void run() {
                        forumPresenter.clear();
                        final ForumAdapter adapter = forumPresenter.buildAdapter();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                recyclerView.setAdapter(adapter);
                                swipeRefreshLayout.setRefreshing(false);
                            }
                        });
                    }
                })).start();
            }
        });
    }
}
