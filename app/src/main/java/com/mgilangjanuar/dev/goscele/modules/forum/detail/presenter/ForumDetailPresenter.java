package com.mgilangjanuar.dev.goscele.modules.forum.detail.presenter;

import com.mgilangjanuar.dev.goscele.R;
import com.mgilangjanuar.dev.goscele.base.BaseFragment;
import com.mgilangjanuar.dev.goscele.base.BasePresenter;
import com.mgilangjanuar.dev.goscele.modules.forum.detail.adapter.CommentRecyclerViewAdapter;
import com.mgilangjanuar.dev.goscele.modules.forum.detail.listener.ForumCommentListener;
import com.mgilangjanuar.dev.goscele.modules.forum.detail.listener.ForumDeleteListener;
import com.mgilangjanuar.dev.goscele.modules.forum.detail.listener.ForumPostListener;
import com.mgilangjanuar.dev.goscele.modules.forum.detail.model.ForumDetailModel;
import com.mgilangjanuar.dev.goscele.modules.forum.detail.provider.DeleteProvider;
import com.mgilangjanuar.dev.goscele.modules.forum.detail.provider.ForumDetailProvider;
import com.mgilangjanuar.dev.goscele.modules.forum.detail.view.ForumDetailActivity;
import com.mgilangjanuar.dev.goscele.modules.forum.detail.view.ForumPostFragment;

/**
 * Created by mgilangjanuar (mgilangjanuar@gmail.com)
 *
 * @since 2017
 */

public class ForumDetailPresenter extends BasePresenter {

    public String url;

    private ForumDetailActivity activity;
    private ForumPostListener postListener;
    private ForumCommentListener commentListener;
    private ForumDetailModel model;

    public ForumDetailPresenter(ForumDetailActivity activity, String url) {
        this.activity = activity;
        this.url = url;
    }

    public boolean isCanShareContent() {
        model = new ForumDetailModel().find().where("url = ?", url).executeSingle();
        return model != null;
    }

    public String getContentModel() {
        return model.title + "\n(" + model.author + " - " + model.date + ")\n\n" + model.url;
    }

    public void runProvider(final BaseFragment fragment, boolean force) {
        if (postListener != null && commentListener != null) {
            model = new ForumDetailModel().find().where("url = ?", url).executeSingle();
            ForumDeleteListener forumDeleteListener = new ForumDeleteListener() {
                @Override
                public void onDeleted() {
                    activity.showToast(R.string.comment_deleted);
                    runProvider(fragment, true);
                }

                @Override
                public void onError(String error) {
                    activity.showToast(error);
                }
            };
            if (model == null || force) {
                new ForumDetailProvider(activity, url, commentListener, postListener, forumDeleteListener).run();
            } else {
                if (fragment instanceof ForumPostFragment) {
                    postListener.onRetrievePost(model);
                } else {
                    commentListener.onRetrieveComments(new CommentRecyclerViewAdapter(activity, model.comments(), forumDeleteListener));
                }
            }
        }
    }

    public void runProvider(BaseFragment fragment) {
        runProvider(fragment, false);
    }

    public void deletePost() {
        new DeleteProvider(model.deleteUrl, new ForumDeleteListener() {
            @Override
            public void onDeleted() {
                activity.showToast(activity.getString(R.string.thread_deleted));
                activity.finish();
            }

            @Override
            public void onError(String error) {
                activity.showToast(error);
            }
        }).run();
    }

    public void setPostListener(ForumPostListener postListener) {
        this.postListener = postListener;
    }

    public void setCommentListener(ForumCommentListener commentListener) {
        this.commentListener = commentListener;
    }
}
