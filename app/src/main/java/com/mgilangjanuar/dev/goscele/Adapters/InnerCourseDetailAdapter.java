package com.mgilangjanuar.dev.goscele.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mgilangjanuar.dev.goscele.Helpers.HtmlHandlerHelper;
import com.mgilangjanuar.dev.goscele.InAppBrowserActivity;
import com.mgilangjanuar.dev.goscele.Models.InnerCoursePostModel;
import com.mgilangjanuar.dev.goscele.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by muhammadgilangjanuar on 5/21/17.
 */

public class InnerCourseDetailAdapter extends RecyclerView.Adapter<InnerCourseDetailAdapter.InnerCourseDetailViewHolder> {

    private Context context;
    private List<InnerCoursePostModel> list;

    public InnerCourseDetailAdapter(Context context, List<InnerCoursePostModel> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public InnerCourseDetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new InnerCourseDetailViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_inner_course_detail, parent, false));
    }

    @Override
    public void onBindViewHolder(InnerCourseDetailViewHolder holder, int position) {
        final InnerCoursePostModel model = list.get(position);

        if (!model.title.equals("")) {
            holder.title.setVisibility(TextView.VISIBLE);
            holder.title.setText(model.title);
            holder.title.setOnClickListener(v -> context.startActivity((new Intent(context, InAppBrowserActivity.class)).putExtra("url", model.url)));
        }

        if (!model.comment.equals("")) {
            holder.comment.setVisibility(TextView.VISIBLE);
            HtmlHandlerHelper helper = new HtmlHandlerHelper(context, model.comment);
            helper.setTextViewHTML(holder.comment);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class InnerCourseDetailViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.title_inner_course_post) public TextView title;
        @BindView(R.id.comment_inner_course_post) public TextView comment;

        public InnerCourseDetailViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
