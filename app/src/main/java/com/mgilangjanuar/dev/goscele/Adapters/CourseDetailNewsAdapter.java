package com.mgilangjanuar.dev.goscele.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mgilangjanuar.dev.goscele.ForumDetail;
import com.mgilangjanuar.dev.goscele.Models.CourseNewsModel;
import com.mgilangjanuar.dev.goscele.R;

import java.util.List;

/**
 * Created by muhammadgilangjanuar on 5/23/17.
 */

public class CourseDetailNewsAdapter extends RecyclerView.Adapter<CourseDetailNewsAdapter.CourseDetailNewsViewHolder> {

    Context context;
    List<CourseNewsModel> list;

    public CourseDetailNewsAdapter(Context context, List<CourseNewsModel> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public CourseDetailNewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CourseDetailNewsAdapter.CourseDetailNewsViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_course_detail_news, parent, false));
    }

    @Override
    public void onBindViewHolder(CourseDetailNewsViewHolder holder, int position) {
        final CourseNewsModel model = list.get(position);
        holder.title.setText(model.title);
        holder.info.setText(model.info);
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ForumDetail.class).putExtra("url", model.url);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class CourseDetailNewsViewHolder extends RecyclerView.ViewHolder {

        public TextView title;
        public TextView info;
        public LinearLayout layout;

        public CourseDetailNewsViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title_course_detail_news);
            info = (TextView) itemView.findViewById(R.id.info_course_detail_news);
            layout = (LinearLayout) itemView.findViewById(R.id.layout_course_detail_news);
        }
    }
}
