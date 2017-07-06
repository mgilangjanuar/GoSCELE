package com.mgilangjanuar.dev.goscele.Presenters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.mgilangjanuar.dev.goscele.Adapters.ForumAdapter;
import com.mgilangjanuar.dev.goscele.Adapters.ForumDetailCommentAdapter;
import com.mgilangjanuar.dev.goscele.BaseActivity;
import com.mgilangjanuar.dev.goscele.ForumActivity;
import com.mgilangjanuar.dev.goscele.Models.ForumCommentModel;
import com.mgilangjanuar.dev.goscele.Models.ForumDetailModel;
import com.mgilangjanuar.dev.goscele.Models.ListForumModel;
import com.mgilangjanuar.dev.goscele.R;
import com.mgilangjanuar.dev.goscele.Services.ForumService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by muhammadgilangjanuar on 5/25/17.
 */

public class ForumDetailPresenter {
    private Activity activity;
    private String url;

    private ForumDetailModel forumDetailModel;
    private ForumService forumService;

    public interface ForumDetailServicePresenter {
        void setupForumDetail(View view);
    }

    public ForumDetailPresenter(Activity activity, String url) {
        this.activity = activity;
        this.url = url;
        forumDetailModel = new ForumDetailModel(activity);
        forumService = new ForumService(url);
    }

    public ForumDetailCommentAdapter buildCommentAdapter() {
        if (forumDetailModel.getSavedUrl() == null
                || !forumDetailModel.getSavedUrl().equals(url)
                || forumDetailModel.getSavedForumCommentModelList() == null) {
            buildModel();
        }
        return new ForumDetailCommentAdapter(activity, forumDetailModel.getSavedForumCommentModelList(), this);
    }

    public void buildPostModel() {
        if (forumDetailModel.getSavedUrl() != null
                && forumDetailModel.getSavedUrl().equals(url)) {
            return;
        }
        try {
            clear();
            Map<String, Object> data = forumService.getForumDetails();
            forumDetailModel.url = (String) data.get("url");
            forumDetailModel.title = (String) data.get("title");
            forumDetailModel.author = (String) data.get("author");
            forumDetailModel.date = (String) data.get("date");
            forumDetailModel.content = (String) data.get("content");
            forumDetailModel.deleteUrl = (String) data.get("deleteUrl");
            forumDetailModel.save();
        } catch (IOException e) {
            Log.e("ForumDetailPresenter", e.getMessage());
        }
    }

    public void buildModel() {
        try {
            Map<String, Object> data = forumService.getForumDetails();
            forumDetailModel.forumCommentModelList = new ArrayList<>();
            for (Map<String, String> e : (List<Map<String, String>>) data.get("forumCommentModelList")) {
                ForumCommentModel forumCommentModel = new ForumCommentModel();
                forumCommentModel.author = e.get("author");
                forumCommentModel.date = e.get("date");
                forumCommentModel.content = e.get("content");
                forumCommentModel.deleteUrl = e.get("deleteUrl");
                forumDetailModel.forumCommentModelList.add(forumCommentModel);
            }
            forumDetailModel.save();
        } catch (IOException e) {
            Log.e("ForumDetailPresenter", e.getMessage());
        }
    }

    public void clear() {
        forumDetailModel.clear();
    }

    public ForumDetailModel getForumDetailModel() {
        return forumDetailModel;
    }

    public void sendComment(String message) {
        try {
            forumService.postForumComment(message);
        } catch (IOException e) {
            Log.e("ForumDetailPresenter", e.getMessage());
        }
    }

    public void deleteComment(ForumCommentModel model) {
        try {
            forumService.deleteForumComment(model.deleteUrl);
        } catch (IOException e) {
            Log.e("ForumDetailPresenter", e.getMessage());
        }
    }

    public void deletePost() {
        try {
            forumService.deleteForum(forumDetailModel.deleteUrl);
        } catch (IOException e) {
            Log.e("ForumDetailPresenter", e.getMessage());
        }
    }

    public void buildAlertDialog(RecyclerView recyclerView, SwipeRefreshLayout swipeRefreshLayout) {
        AlertDialog.Builder alert = new AlertDialog.Builder(activity);
        alert.setTitle("Reply ForumActivity");
        alert.setMessage("Write your reply here:");

        final EditText edittext = new EditText(activity);
        edittext.setSingleLine(false);
        edittext.setMaxLines(7);

        FrameLayout container = new FrameLayout(activity);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.leftMargin = activity.getResources().getDimensionPixelSize(R.dimen.dialog_margin);
        params.rightMargin = activity.getResources().getDimensionPixelSize(R.dimen.dialog_margin);
        edittext.setLayoutParams(params);
        container.addView(edittext);

        alert.setView(container);

        alert.setPositiveButton("Send", null);
        alert.setNegativeButton("Cancel", (dialog, whichButton) -> dialog.dismiss());

        final AlertDialog alertDialog = alert.create();
        alertDialog.setOnShowListener(arg0 -> {
            alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.DKGRAY);
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.DKGRAY);

            Button button = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
            button.setOnClickListener(view -> {
                final String message = edittext.getText().toString().replaceAll("\\n", "<br />");
                if (message.equals("")) {
                    ((BaseActivity) activity).showToast("Message couldn't be blank");
                } else {
                    ((BaseActivity) activity).showToast("Please wait...");
                    (new Thread(() -> {
                        sendComment(message);
                        refreshingComment(recyclerView, swipeRefreshLayout);
                        activity.runOnUiThread(() -> ((BaseActivity) activity).showToast("Sent!"));
                    })).start();
                    alertDialog.dismiss();
                }
            });
        });
        alertDialog.show();
    }

    private void refreshingComment(RecyclerView recyclerView, SwipeRefreshLayout swipeRefreshLayout) {
        swipeRefreshLayout.post(() -> {
            swipeRefreshLayout.setRefreshing(true);
            (new Thread(() -> {
                clear();
                ForumDetailCommentAdapter adapter = buildCommentAdapter();
                activity.runOnUiThread(() -> {
                    recyclerView.setAdapter(adapter);
                    swipeRefreshLayout.setRefreshing(false);
                });
            })).start();
        });
    }

    public void clearForum() {
        ListForumModel listForumModel = new ListForumModel(activity);
        Intent intent = new Intent(activity, ForumActivity.class).putExtra("url", listForumModel.getSavedUrl());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        activity.startActivity(intent);
        ForumPresenter presenter = new ForumPresenter(activity, listForumModel.getSavedUrl());
        presenter.clear();
    }
}
