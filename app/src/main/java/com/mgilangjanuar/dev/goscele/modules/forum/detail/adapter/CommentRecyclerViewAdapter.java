package com.mgilangjanuar.dev.goscele.modules.forum.detail.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.mgilangjanuar.dev.goscele.R;
import com.mgilangjanuar.dev.goscele.base.BaseRecyclerViewAdapter;
import com.mgilangjanuar.dev.goscele.base.BaseViewHolder;
import com.mgilangjanuar.dev.goscele.modules.forum.detail.listener.ForumDeleteListener;
import com.mgilangjanuar.dev.goscele.modules.forum.detail.model.ForumCommentModel;
import com.mgilangjanuar.dev.goscele.modules.forum.detail.provider.DeleteProvider;

import org.sufficientlysecure.htmltextview.HtmlHttpImageGetter;
import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by mgilangjanuar (mgilangjanuar@gmail.com)
 *
 * @since 2017
 */

public class CommentRecyclerViewAdapter extends BaseRecyclerViewAdapter<CommentRecyclerViewAdapter.ViewHolder> {

    private Context context;
    private List<ForumCommentModel> list;
    private ForumDeleteListener listener;

    public CommentRecyclerViewAdapter(Context context, List<ForumCommentModel> list, ForumDeleteListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @Override
    public int findLayout() {
        return R.layout.layout_forum_comment;
    }

    @Override
    public List<?> findList() {
        return list;
    }

    @Override
    public void initialize(ViewHolder holder, final int position) {
        final ForumCommentModel model = list.get(position);
        holder.author.setText(model.author);
        holder.date.setText(model.date);
        holder.content.setHtml(model.content, new HtmlHttpImageGetter(holder.content));

        if (!TextUtils.isEmpty(model.deleteUrl)) {
            holder.delete.setVisibility(Button.VISIBLE);
            holder.delete.getBackground().setColorFilter(context.getResources().getColor(android.R.color.holo_red_light), PorterDuff.Mode.MULTIPLY);
            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    builder.setCancelable(false);
                    builder.setTitle(v.getContext().getString(R.string.delete_comment));
                    builder.setMessage(v.getContext().getString(R.string.are_you_sure_want_to_delete_this));
                    builder.setInverseBackgroundForced(true);
                    builder.setPositiveButton(v.getContext().getString(R.string.yes), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            new DeleteProvider(model.deleteUrl, listener).run();
                            list.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, getItemCount());
                        }
                    });
                    builder.setNegativeButton(v.getContext().getString(R.string.cancel), new DialogInterface.OnClickListener() {
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
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(createView(parent));
    }

    public class ViewHolder extends BaseViewHolder {

        @BindView(R.id.author_forum_comment)
        TextView author;
        @BindView(R.id.date_forum_comment)
        TextView date;
        @BindView(R.id.content_forum_comment)
        HtmlTextView content;
        @BindView(R.id.button_delete_comment)
        Button delete;

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
