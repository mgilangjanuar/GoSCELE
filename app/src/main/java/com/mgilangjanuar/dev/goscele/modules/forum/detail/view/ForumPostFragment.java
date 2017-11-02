package com.mgilangjanuar.dev.goscele.modules.forum.detail.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import com.mgilangjanuar.dev.goscele.R;
import com.mgilangjanuar.dev.goscele.base.BaseActivity;
import com.mgilangjanuar.dev.goscele.base.BaseFragment;
import com.mgilangjanuar.dev.goscele.modules.forum.detail.listener.ForumPostListener;
import com.mgilangjanuar.dev.goscele.modules.forum.detail.model.ForumDetailModel;
import com.mgilangjanuar.dev.goscele.modules.forum.detail.presenter.ForumDetailPresenter;
import com.mgilangjanuar.dev.goscele.utils.WebViewContentUtil;

import butterknife.BindView;

/**
 * Created by mgilangjanuar (mgilangjanuar@gmail.com)
 *
 * @since 2017
 */
public class ForumPostFragment extends BaseFragment implements ForumPostListener {

    @BindView(R.id.title_forum_detail)
    TextView title;
    @BindView(R.id.date_forum_detail)
    TextView date;
    @BindView(R.id.author_forum_detail)
    TextView author;
    @BindView(R.id.content_forum_detail)
    WebView content;
    @BindView(R.id.button_delete_post)
    Button btnDelete;

    private ForumDetailPresenter presenter;

    public static BaseFragment newInstance(ForumDetailPresenter presenter) {
        Bundle args = new Bundle();
        ForumPostFragment fragment = new ForumPostFragment();
        fragment.presenter = presenter;
        fragment.presenter.setPostListener(fragment);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int findLayout() {
        return R.layout.fragment_forum_post;
    }

    @Override
    public void initialize(Bundle savedInstanceState) {
        super.initialize(savedInstanceState);
        presenter.runProvider(this);
    }

    @Override
    public void onRetrievePost(ForumDetailModel model) {
        ((BaseActivity) getActivity()).getSupportActionBar().setTitle(model.title);
        title.setText(model.title);
        date.setText(model.date);
        author.setText(model.author);
        WebViewContentUtil.setWebView(content, model.content);

        if (!TextUtils.isEmpty(model.deleteUrl)) {
            btnDelete.setVisibility(Button.VISIBLE);
            btnDelete.getBackground().setColorFilter(getContext().getResources().getColor(android.R.color.holo_red_light), PorterDuff.Mode.MULTIPLY);

            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setCancelable(false);
                    builder.setTitle(getString(R.string.delete_thread));
                    builder.setMessage(getString(R.string.are_you_sure_want_to_delete_this));
                    builder.setInverseBackgroundForced(true);
                    builder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            btnDelete.setClickable(false);
                            ((BaseActivity) getActivity()).showToast(getString(R.string.please_wait));
                            presenter.deletePost();
                        }
                    });
                    builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    final AlertDialog alert = builder.create();

                    alert.setOnShowListener(new DialogInterface.OnShowListener() {
                        @Override
                        public void onShow(DialogInterface dialog) {
                            alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.DKGRAY);
                            alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.DKGRAY);
                        }
                    });
                    alert.show();
                }
            });
        }
    }

    @Override
    public void onError(String error) {
        showSnackbar(error);
    }
}
