package com.mgilangjanuar.dev.sceleapp.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mgilangjanuar.dev.sceleapp.Helpers.HtmlHandlerHelper;
import com.mgilangjanuar.dev.sceleapp.Models.ForumCommentModel;
import com.mgilangjanuar.dev.sceleapp.R;

import java.util.List;

/**
 * Created by muhammadgilangjanuar on 5/25/17.
 */

public class ForumDetailCommentAdapter extends RecyclerView.Adapter<ForumDetailCommentAdapter.ForumDetailCommentViewHolder> {

    Context context;
    List<ForumCommentModel> list;

    public ForumDetailCommentAdapter(Context context, List<ForumCommentModel> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ForumDetailCommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ForumDetailCommentViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_forum_comment, parent, false));
    }

    @Override
    public void onBindViewHolder(ForumDetailCommentViewHolder holder, int position) {
        ForumCommentModel model = list.get(position);
        holder.author.setText(model.author);
        holder.date.setText(model.date);

        HtmlHandlerHelper helper = new HtmlHandlerHelper(context, model.content);
        helper.setTextViewHTML(holder.content);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ForumDetailCommentViewHolder extends RecyclerView.ViewHolder {

        public TextView author;
        public TextView date;
        public TextView content;

        public ForumDetailCommentViewHolder(View itemView) {
            super(itemView);
            author = (TextView) itemView.findViewById(R.id.author_forum_comment);
            date = (TextView) itemView.findViewById(R.id.date_forum_comment);
            content = (TextView) itemView.findViewById(R.id.content_forum_comment);
        }
    }
}
