package com.mgilangjanuar.dev.goscele.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.mgilangjanuar.dev.goscele.BaseActivity;
import com.mgilangjanuar.dev.goscele.ForumDetailActivity;
import com.mgilangjanuar.dev.goscele.Helpers.HtmlHandlerHelper;
import com.mgilangjanuar.dev.goscele.Models.ForumCommentModel;
import com.mgilangjanuar.dev.goscele.Presenters.ForumDetailPresenter;
import com.mgilangjanuar.dev.goscele.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by muhammadgilangjanuar on 5/25/17.
 */

public class ForumDetailCommentAdapter extends RecyclerView.Adapter<ForumDetailCommentAdapter.ForumDetailCommentViewHolder> {

    private Context context;
    private List<ForumCommentModel> list;
    private ForumDetailPresenter presenter;

    public ForumDetailCommentAdapter(Context context, List<ForumCommentModel> list, ForumDetailPresenter presenter) {
        this.context = context;
        this.list = list;
        this.presenter = presenter;
    }

    @Override
    public ForumDetailCommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ForumDetailCommentViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_forum_comment, parent, false));
    }

    @Override
    public void onBindViewHolder(ForumDetailCommentViewHolder holder, final int position) {
        final ForumCommentModel model = list.get(position);
        holder.author.setText(model.author);
        holder.date.setText(model.date);

        HtmlHandlerHelper helper = new HtmlHandlerHelper(context, model.content);
        helper.setTextViewHTML(holder.content);

        if (!model.deleteUrl.equals("")) {
            holder.delete.setVisibility(Button.VISIBLE);
            holder.delete.getBackground().setColorFilter(context.getResources().getColor(android.R.color.holo_red_light), PorterDuff.Mode.MULTIPLY);
            holder.delete.setOnClickListener(v -> {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setCancelable(false);
                builder.setTitle("Delete Comment");
                builder.setMessage("Are you sure want to delete this?");
                builder.setInverseBackgroundForced(true);
                builder.setPositiveButton("Yes", (dialog, which) -> {
                    (new Thread(() -> {
                        presenter.deleteComment(model);
                        ((ForumDetailActivity) context).runOnUiThread(() -> ((BaseActivity) context).showToast("Deleted!"));
                    })).start();
                    list.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, getItemCount());
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
    }

    @Override
    public void onViewRecycled(ForumDetailCommentViewHolder holder) {
        super.onViewRecycled(holder);
        holder.delete.setVisibility(Button.GONE);
    }

    @Override
    public int getItemCount() {
        if (list == null) {
            return 0;
        }
        return list.size();
    }

    public class ForumDetailCommentViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.author_forum_comment)
        public TextView author;
        @BindView(R.id.date_forum_comment)
        public TextView date;
        @BindView(R.id.content_forum_comment)
        public TextView content;
        @BindView(R.id.button_delete_comment)
        public Button delete;

        public ForumDetailCommentViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
