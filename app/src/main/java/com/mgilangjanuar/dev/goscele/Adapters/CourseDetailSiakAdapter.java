package com.mgilangjanuar.dev.goscele.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mgilangjanuar.dev.goscele.Models.CourseDetailSiakModel;
import com.mgilangjanuar.dev.goscele.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by gilang on 8/5/17.
 */

public class CourseDetailSiakAdapter extends RecyclerView.Adapter<CourseDetailSiakAdapter.CourseDetailSiakViewHolder> {

    private List<CourseDetailSiakModel.Course> list;

    public CourseDetailSiakAdapter(List<CourseDetailSiakModel.Course> list) {
        this.list = list;
    }

    @Override
    public CourseDetailSiakViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CourseDetailSiakViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_course_detail_siak, parent, false));
    }

    @Override
    public void onBindViewHolder(CourseDetailSiakViewHolder holder, int position) {
        CourseDetailSiakModel.Course model = list.get(position);
        holder.courseName.setText(model.courseName);
        holder.courseClass.setText(model.courseClass);
        holder.description.setText(model.code + " (" + model.credits + " SKS)");
        holder.lecturer.setText(model.lecturer.replace("- ", "\n- ").trim());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class CourseDetailSiakViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.course_description)
        TextView description;
        @BindView(R.id.course_name)
        TextView courseName;
        @BindView(R.id.course_class)
        TextView courseClass;
        @BindView(R.id.course_lecturer)
        TextView lecturer;

        public CourseDetailSiakViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
