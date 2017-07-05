package com.mgilangjanuar.dev.goscele.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mgilangjanuar.dev.goscele.ForumDetailActivity;
import com.mgilangjanuar.dev.goscele.Models.CourseNewsModel;
import com.mgilangjanuar.dev.goscele.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by muhammadgilangjanuar on 5/23/17.
 */

public class CourseDetailNewsAdapter extends RecyclerView.Adapter<CourseDetailNewsAdapter.CourseDetailNewsViewHolder> {

    private Context context;
    private List<CourseNewsModel> list;

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
        holder.layout.setOnClickListener(v -> {
            Intent intent = new Intent(context, ForumDetailActivity.class).putExtra("url", model.url);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class CourseDetailNewsViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.title_course_detail_news) public TextView title;
        @BindView(R.id.info_course_detail_news) public TextView info;
        @BindView(R.id.layout_course_detail_news) public LinearLayout layout;

        public CourseDetailNewsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
