package com.mgilangjanuar.dev.goscele.modules.course.adapter;

import android.content.Intent;
import android.provider.Browser;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mgilangjanuar.dev.goscele.R;
import com.mgilangjanuar.dev.goscele.base.BaseRecyclerViewAdapter;
import com.mgilangjanuar.dev.goscele.base.BaseViewHolder;
import com.mgilangjanuar.dev.goscele.modules.browser.view.BrowserActivity;
import com.mgilangjanuar.dev.goscele.modules.course.model.CourseSearchModel;
import com.mgilangjanuar.dev.goscele.modules.course.view.CourseActivity;
import com.mgilangjanuar.dev.goscele.utils.Constant;

import java.util.List;

import butterknife.BindView;

/**
 * Created by mgilangjanuar (mgilangjanuar@gmail.com)
 *
 * @since 2017
 */

public class CourseSearchRecyclerViewAdapter extends BaseRecyclerViewAdapter<CourseSearchRecyclerViewAdapter.ViewHolder> {

    private List<CourseSearchModel> list;

    public CourseSearchRecyclerViewAdapter(List<CourseSearchModel> list) {
        this.list = list;
    }

    @Override
    public int findLayout() {
        return R.layout.layout_search_course;
    }

    @Override
    public List<?> findList() {
        return list;
    }

    @Override
    public void initialize(ViewHolder holder, int position) {
        final CourseSearchModel model = list.get(position);
        holder.name.setText(model.name);
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), BrowserActivity.class);
                intent.putExtra(Constant.URL, model.url);
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(createView(parent));
    }

    public static class ViewHolder extends BaseViewHolder {

        @BindView(R.id.layout_course_search)
        LinearLayout layout;

        @BindView(R.id.title_course_name_search)
        TextView name;

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
