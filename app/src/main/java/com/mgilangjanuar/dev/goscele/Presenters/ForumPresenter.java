package com.mgilangjanuar.dev.goscele.Presenters;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.mgilangjanuar.dev.goscele.Adapters.ForumAdapter;
import com.mgilangjanuar.dev.goscele.BaseActivity;
import com.mgilangjanuar.dev.goscele.Models.ForumModel;
import com.mgilangjanuar.dev.goscele.Models.ListForumModel;
import com.mgilangjanuar.dev.goscele.R;
import com.mgilangjanuar.dev.goscele.Services.ForumService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by muhammadgilangjanuar on 5/31/17.
 */

public class ForumPresenter {
    public String url;
    private Activity activity;
    private ListForumModel listForumModel;
    private ListForumModel listForumSearchModel;
    private ForumService forumService;

    public ForumPresenter(Activity activity, String url) {
        this.activity = activity;
        this.url = url;
        listForumModel = new ListForumModel(activity);
        listForumSearchModel = new ListForumModel(activity);
        forumService = new ForumService(url);
    }

    public ForumAdapter buildAdapter() {
        if (listForumModel.getSavedUrl() == null
                || !listForumModel.getSavedUrl().equals(url)
                || listForumModel.getSavedForumModelList() == null) {
            buildModel();
        }
        return new ForumAdapter(activity, listForumModel.getSavedForumModelList());
    }

    public void buildModel() {
        try {
            listForumModel.clear();
            listForumModel.url = url;
            listForumModel.forumModelList = new ArrayList<>();
            for (Map<String, String> e : forumService.getForums()) {
                ForumModel model = new ForumModel();
                model.url = e.get("url");
                model.title = e.get("title");
                model.author = e.get("author");
                model.lastUpdate = e.get("lastUpdate");
                model.repliesNumber = e.get("repliesNumber");
                listForumModel.forumModelList.add(model);
            }
            listForumModel.save();
        } catch (IOException e) {
            Log.e("ForumPresenter", String.valueOf(e.getMessage()));
        }
    }

    public String getTitle() {
        try {
            return forumService.getTitle();
        } catch (IOException e) {
            Log.e("ForumPresenter", String.valueOf(e.getMessage()));
        }
        return activity.getResources().getString(R.string.app_name);
    }

    public void clear() {
        listForumModel.clear();
    }

    public boolean isCanSendNews() {
        try {
            return forumService.isCanPostForum();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void sendNews(String title, String message) {
        try {
            forumService.postForum(title, message);
        } catch (IOException e) {
            Log.e("ForumPresenter", String.valueOf(e.getMessage()));
        }
    }

    public ForumAdapter buildSearchAdapter(String keyword) {
        buildSearchModel(keyword);
        return new ForumAdapter(activity, listForumSearchModel.forumModelList);
    }

    public void buildSearchModel(String keyword) {
        try {
            listForumSearchModel.forumModelList = new ArrayList<>();
            for (Map<String, String> e : forumService.searchForum(keyword)) {
                ForumModel model = new ForumModel();
                model.url = e.get("url");
                model.title = e.get("title");
                model.author = e.get("author");
                model.lastUpdate = e.get("lastUpdate");
                model.repliesNumber = e.get("repliesNumber");
                listForumSearchModel.forumModelList.add(model);
            }

            String count = forumService.searchForumInfo(keyword).get("count");
            if (!count.equals("") && Integer.parseInt(count) > 50) {
                ((BaseActivity) activity).showToast("Please try to be more specific");
            }
        } catch (IOException e) {
            Log.e("ForumPresenter", String.valueOf(e.getMessage()));
        }
    }

    public void buildAlertDialog(final RecyclerView recyclerView, final SwipeRefreshLayout swipeRefreshLayout) {
        AlertDialog.Builder alert = new AlertDialog.Builder(activity);
        alert.setTitle("Add New Thread");
        alert.setMessage("Write your thread here:");

        final EditText etTitle = new EditText(activity);
        etTitle.setSingleLine(true);
        etTitle.setHint("Subject");

        final EditText etMessage = new EditText(activity);
        etMessage.setSingleLine(false);
        etMessage.setMaxLines(7);
        etMessage.setHint("Content");

        LinearLayout container = new LinearLayout(activity);
        container.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.leftMargin = activity.getResources().getDimensionPixelSize(R.dimen.dialog_margin);
        params.rightMargin = activity.getResources().getDimensionPixelSize(R.dimen.dialog_margin);
        etTitle.setLayoutParams(params);
        container.addView(etTitle);
        etMessage.setLayoutParams(params);
        container.addView(etMessage);

        alert.setView(container);

        alert.setPositiveButton("Send", null);

        alert.setNegativeButton("Cancel", (dialog, whichButton) -> dialog.dismiss());

        final AlertDialog alertDialog = alert.create();

        alertDialog.setOnShowListener(arg0 -> {
            alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.DKGRAY);
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.DKGRAY);

            Button button = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
            button.setOnClickListener(view -> {
                final String titleText = etTitle.getText().toString().trim();
                final String messageText = etMessage.getText().toString().replaceAll("\\n", "<br />");
                if (titleText.equals("") || messageText.equals("")) {
                    ((BaseActivity) activity).showToast("Title and message are required fields");
                } else {
                    ((BaseActivity) activity).showToast("Please wait...");
                    (new Thread(() -> {
                        sendNews(titleText, messageText);
                        refreshingForum(recyclerView, swipeRefreshLayout);
                        activity.runOnUiThread(() -> ((BaseActivity) activity).showToast("Sent!"));
                    })).start();
                    alertDialog.dismiss();
                }
            });
        });
        alertDialog.show();
    }

    private void refreshingForum(final RecyclerView recyclerView, final SwipeRefreshLayout swipeRefreshLayout) {
        swipeRefreshLayout.post(() -> {
            swipeRefreshLayout.setRefreshing(true);
            (new Thread(() -> {
                clear();
                final ForumAdapter adapter = buildAdapter();
                activity.runOnUiThread(() -> {
                    recyclerView.setAdapter(adapter);
                    swipeRefreshLayout.setRefreshing(false);
                });
            })).start();
        });
    }

    public interface ForumServicePresenter {
        void setupForum();
    }
}
