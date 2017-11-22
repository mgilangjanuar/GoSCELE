package com.mgilangjanuar.dev.goscele.modules.main.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mgilangjanuar.dev.goscele.R;
import com.mgilangjanuar.dev.goscele.base.BaseRecyclerViewAdapter;
import com.mgilangjanuar.dev.goscele.base.BaseViewHolder;
import com.mgilangjanuar.dev.goscele.modules.main.model.ScheduleCourseModel;

import java.util.List;

import butterknife.BindView;

/**
 * Created by mgilangjanuar (mgilangjanuar@gmail.com)
 *
 * @since 2017
 */

public class ScheduleDailyDetailRecyclerViewAdapter extends BaseRecyclerViewAdapter<ScheduleDailyDetailRecyclerViewAdapter.ViewHolder> {

    private List<ScheduleCourseModel> list;

    public ScheduleDailyDetailRecyclerViewAdapter(List<ScheduleCourseModel> list) {
        this.list = list;
    }

    @Override
    public int findLayout() {
        return R.layout.layout_schedule_course_detail;
    }

    @Override
    public List<?> findList() {
        return list;
    }

    @Override
    public void initialize(ViewHolder holder, int position) {
        ScheduleCourseModel model = list.get(position);
        holder.courseName.setText(model.courseName);
        holder.courseClass.setText(model.courseClass);
        holder.description.setText(model.code + " (" + model.credits + " SKS)");
        holder.lecturer.setText(model.lecturer.replace("- ", "\n- ").trim());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(createView(parent));
    }

    public static class ViewHolder extends BaseViewHolder {

        @BindView(R.id.course_description)
        TextView description;

        @BindView(R.id.course_name)
        TextView courseName;

        @BindView(R.id.course_class)
        TextView courseClass;

        @BindView(R.id.course_lecturer)
        TextView lecturer;

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
