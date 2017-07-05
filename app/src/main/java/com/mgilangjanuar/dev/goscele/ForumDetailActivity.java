package com.mgilangjanuar.dev.goscele;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.mgilangjanuar.dev.goscele.Adapters.BaseTabViewPagerAdapter;
import com.mgilangjanuar.dev.goscele.Fragments.ForumDetail.Comments;
import com.mgilangjanuar.dev.goscele.Fragments.ForumDetail.Post;
import com.mgilangjanuar.dev.goscele.Presenters.ForumDetailPresenter;

public class ForumDetailActivity extends AppCompatActivity {

    String url;
    ForumDetailPresenter forumDetailPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum_detail);

        Bundle bundle = getIntent().getExtras();
        if (bundle == null || bundle.getString("url") == null) {
            Toast.makeText(this, "Broken URL", Toast.LENGTH_SHORT).show();
            onBackPressed();
            return;
        }

        url = bundle.getString("url");
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_forum_detail);
        toolbar.setTitle(getResources().getString(R.string.loading_text));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        (new Thread(new Runnable() {
            @Override
            public void run() {
                setupForumDetail();
            }
        })).start();
    }

    public void setupForumDetail() {
        forumDetailPresenter = new ForumDetailPresenter(this, url);

        final FloatingActionButton actionButton = (FloatingActionButton) findViewById(R.id.fab_forum_comment);
        actionButton.hide();

        final ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager_activity_forum_detail);
        final BaseTabViewPagerAdapter baseTabViewPagerAdapter = new BaseTabViewPagerAdapter(getSupportFragmentManager());
        baseTabViewPagerAdapter.addFragment(Post.newInstance(forumDetailPresenter), getResources().getString(R.string.title_forum_detail_post));
        baseTabViewPagerAdapter.addFragment(Comments.newInstance(forumDetailPresenter, actionButton), getResources().getString(R.string.title_forum_detail_comments));

        final TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_forum_detail);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                viewPager.setAdapter(baseTabViewPagerAdapter);
                tabLayout.setupWithViewPager(viewPager);
            }
        });
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buildAlertDialog();
            }
        });

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getText().toString().equals(getResources().getString(R.string.title_forum_detail_comments))) {
                    actionButton.show();
                } else {
                    actionButton.hide();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void buildAlertDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Reply ForumActivity");
        alert.setMessage("Write your reply here:");

        final EditText edittext = new EditText(this);
        edittext.setSingleLine(false);
        edittext.setMaxLines(7);

        FrameLayout container = new FrameLayout(this);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.leftMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);
        params.rightMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);
        edittext.setLayoutParams(params);
        container.addView(edittext);

        alert.setView(container);

        alert.setPositiveButton("Send", null);

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
            }
        });

        final AlertDialog alertDialog = alert.create();

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.DKGRAY);
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.DKGRAY);

                Button button = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        final String message = edittext.getText().toString().replaceAll("\\n", "<br />");
                        if (message.equals("")) {
                            Toast.makeText(getApplicationContext(), "Message couldn't be blank", Toast.LENGTH_SHORT).show();
                        } else {
                            (new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    forumDetailPresenter.sendComment(message);
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
}
