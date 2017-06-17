package com.mgilangjanuar.dev.sceleapp.Fragments.ForumDetail;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.mgilangjanuar.dev.sceleapp.Forum;
import com.mgilangjanuar.dev.sceleapp.ForumDetail;
import com.mgilangjanuar.dev.sceleapp.Helpers.HtmlHandlerHelper;
import com.mgilangjanuar.dev.sceleapp.Presenters.ForumDetailPresenter;
import com.mgilangjanuar.dev.sceleapp.R;

public class Post extends Fragment implements ForumDetailPresenter.ForumDetailServicePresenter {

    ForumDetailPresenter forumDetailPresenter;

    public Post() {
        // Required empty public constructor
    }

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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_forum_post, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        (new Thread(new Runnable() {
            @Override
            public void run() {
                setupForumDetail(view);
            }
        })).start();
    }

    @Override
    public void setupForumDetail(View view) {

        forumDetailPresenter.buildPostModel();

        final TextView title = (TextView) view.findViewById(R.id.title_forum_detail);
        final TextView date = (TextView) view.findViewById(R.id.date_forum_detail);
        final TextView author = (TextView) view.findViewById(R.id.author_forum_detail);
        final TextView content = (TextView) view.findViewById(R.id.content_forum_detail);
        final Button button = (Button) view.findViewById(R.id.button_delete_post);
        final HtmlHandlerHelper helper = new HtmlHandlerHelper(getActivity(), forumDetailPresenter.getForumDetailModel().getSavedContent());

        if (getActivity() == null) { return; }
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((ForumDetail) getActivity()).getSupportActionBar().setTitle(forumDetailPresenter.getForumDetailModel().getSavedTitle());
                title.setText(forumDetailPresenter.getForumDetailModel().getSavedTitle());
                date.setText(forumDetailPresenter.getForumDetailModel().getSavedDate());
                author.setText(forumDetailPresenter.getForumDetailModel().getSavedAuthor());
                helper.setTextViewHTML(content);
                if (! forumDetailPresenter.getForumDetailModel().getSavedDeleteUrl().equals("")) {
                    button.setVisibility(Button.VISIBLE);
                    button.getBackground().setColorFilter(getContext().getResources().getColor(android.R.color.holo_red_light), PorterDuff.Mode.MULTIPLY);
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            builder.setCancelable(false);
                            builder.setTitle("Delete Thread");
                            builder.setMessage("Are you sure want to delete this?");
                            builder.setInverseBackgroundForced(true);
                            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    button.setClickable(false);
                                    Toast.makeText(getContext(), "Please wait...", Toast.LENGTH_LONG).show();

                                    (new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            forumDetailPresenter.deletePost();
                                            if (getActivity() == null) { return; }
                                            getActivity().runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    getActivity().onBackPressed();
                                                    Toast.makeText(getContext(), "Deleted! Please refresh the forum", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }
                                    })).start();
                                }
                            });
                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            final AlertDialog alert = builder.create();

                            alert.setOnShowListener( new DialogInterface.OnShowListener() {
                                @Override
                                public void onShow(DialogInterface arg0) {
                                    alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.DKGRAY);
                                    alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.DKGRAY);
                                }
                            });
                            alert.show();
                        }
                    });
                }
            }
        });
    }
}
