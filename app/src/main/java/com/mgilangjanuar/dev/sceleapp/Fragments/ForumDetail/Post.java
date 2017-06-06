package com.mgilangjanuar.dev.sceleapp.Fragments.ForumDetail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

        forumDetailPresenter.buildCommentAdapter();

        final TextView title = (TextView) view.findViewById(R.id.title_forum_detail);
        final TextView date = (TextView) view.findViewById(R.id.date_forum_detail);
        final TextView author = (TextView) view.findViewById(R.id.author_forum_detail);
        final TextView content = (TextView) view.findViewById(R.id.content_forum_detail);
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
            }
        });
    }
}
