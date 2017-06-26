package com.mgilangjanuar.dev.goscele.Adapters;

import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mgilangjanuar.dev.goscele.Helpers.HtmlHandlerHelper;
import com.mgilangjanuar.dev.goscele.Models.CoursePostModel;
import com.mgilangjanuar.dev.goscele.R;

import java.util.List;

/**
 * Created by muhammadgilangjanuar on 5/21/17.
 */

public class CourseDetailAdapter extends RecyclerView.Adapter<CourseDetailAdapter.CourseDetailViewHolder> {

    Context context;
    List<CoursePostModel> list;

    public CourseDetailAdapter(Context context, List<CoursePostModel> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public CourseDetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CourseDetailViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_course_detail_dashboard, parent, false));
    }

    @Override
    public void onBindViewHolder(CourseDetailViewHolder holder, int position) {
        CoursePostModel model = list.get(position);
        holder.title.setText(model.title);

        HtmlHandlerHelper helper = new HtmlHandlerHelper(context, model.summary);
        helper.setTextViewHTML(holder.summary);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        holder.content.setLayoutManager(layoutManager);
        holder.content.setItemAnimator(new DefaultItemAnimator());
        holder.content.setAdapter(new InnerCourseDetailAdapter(context, model.innerCoursePostModelList));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class CourseDetailViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView summary;
        public RecyclerView content;

        public CourseDetailViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title_course_detail);
            summary = (TextView) itemView.findViewById(R.id.summary_course_detail);
            content = (RecyclerView) itemView.findViewById(R.id.content_course_detail);
        }
    }
}
