package com.mgilangjanuar.dev.goscele.Fragments.ForumDetail;

import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.mgilangjanuar.dev.goscele.BaseActivity;
import com.mgilangjanuar.dev.goscele.ForumDetailActivity;
import com.mgilangjanuar.dev.goscele.Helpers.HtmlHandlerHelper;
import com.mgilangjanuar.dev.goscele.Presenters.ForumDetailPresenter;
import com.mgilangjanuar.dev.goscele.Presenters.ForumPresenter;
import com.mgilangjanuar.dev.goscele.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Post extends Fragment implements ForumDetailPresenter.ForumDetailServicePresenter {

    private ForumDetailPresenter forumDetailPresenter;

    @BindView(R.id.title_forum_detail)
    TextView tvTitle;
    @BindView(R.id.date_forum_detail)
    TextView tvDate;
    @BindView(R.id.author_forum_detail)
    TextView tvAuthor;
    @BindView(R.id.content_forum_detail)
    TextView tvContent;
    @BindView(R.id.button_delete_post)
    Button btnDelete;

    public static Post newInstance(ForumDetailPresenter forumDetailPresenter) {
        Post fragment = new Post();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        fragment.forumDetailPresenter = forumDetailPresenter;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forum_post, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        (new Thread(() -> setupForumDetail(view))).start();
    }

    @Override
    public void setupForumDetail(View view) {
        forumDetailPresenter.buildPostModel();

        final HtmlHandlerHelper helper = new HtmlHandlerHelper(getActivity(), forumDetailPresenter.getForumDetailModel().getSavedContent());

        if (getActivity() == null) return;
        getActivity().runOnUiThread(() -> {
            ((ForumDetailActivity) getActivity()).getSupportActionBar().setTitle(forumDetailPresenter.getForumDetailModel().getSavedTitle());
            tvTitle.setText(forumDetailPresenter.getForumDetailModel().getSavedTitle());
            tvDate.setText(forumDetailPresenter.getForumDetailModel().getSavedDate());
            tvAuthor.setText(forumDetailPresenter.getForumDetailModel().getSavedAuthor());
            helper.setTextViewHTML(tvContent);
            if (!forumDetailPresenter.getForumDetailModel().getSavedDeleteUrl().equals("")) {
                btnDelete.setVisibility(Button.VISIBLE);
                btnDelete.getBackground().setColorFilter(getContext().getResources().getColor(android.R.color.holo_red_light), PorterDuff.Mode.MULTIPLY);
                btnDelete.setOnClickListener(v -> {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setCancelable(false);
                    builder.setTitle("Delete Thread");
                    builder.setMessage("Are you sure want to delete this?");
                    builder.setInverseBackgroundForced(true);
                    builder.setPositiveButton("Yes", (dialog, which) -> {
                        btnDelete.setClickable(false);
                        ((BaseActivity) getActivity()).showToast("Please wait...");

                        (new Thread(() -> {
                            forumDetailPresenter.deletePost();
                            if (getActivity() == null) return;
                            getActivity().runOnUiThread(() -> {
                                getActivity().onBackPressed();
                                forumDetailPresenter.clearForum();
                            });
                        })).start();
                    });
                    builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
                    final AlertDialog alert = builder.create();

                    alert.setOnShowListener(arg0 -> {
                        alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.DKGRAY);
                        alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.DKGRAY);
                    });
                    alert.show();
                });
            }
        });
    }
}
