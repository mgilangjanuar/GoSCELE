package com.mgilangjanuar.dev.goscele.modules.forum.detail.view;

import android.os.Bundle;

import com.mgilangjanuar.dev.goscele.R;
import com.mgilangjanuar.dev.goscele.base.BaseFragment;

/**
 * Created by mgilangjanuar (mgilangjanuar@gmail.com)
 *
 * @since 2017
 */
public class ForumCommentFragment extends BaseFragment {

    @Override
    public int findLayout() {
        return R.layout.fragment_forum_comment;
    }

    public static BaseFragment newInstance() {

        Bundle args = new Bundle();

        BaseFragment fragment = new ForumPostFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
