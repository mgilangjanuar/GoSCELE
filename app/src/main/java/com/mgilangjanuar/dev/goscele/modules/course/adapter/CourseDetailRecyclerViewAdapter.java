package com.mgilangjanuar.dev.goscele.modules.course.adapter;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mgilangjanuar.dev.goscele.R;
import com.mgilangjanuar.dev.goscele.base.BaseRecyclerViewAdapter;
import com.mgilangjanuar.dev.goscele.base.BaseViewHolder;
import com.mgilangjanuar.dev.goscele.modules.course.model.CourseDetailModel;

import org.sufficientlysecure.htmltextview.HtmlHttpImageGetter;
import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by mgilangjanuar (mgilangjanuar@gmail.com)
 *
 * @since 2017
 */

public class CourseDetailRecyclerViewAdapter extends BaseRecyclerViewAdapter<CourseDetailRecyclerViewAdapter.ViewHolder> {

    private List<CourseDetailModel> list;

    public CourseDetailRecyclerViewAdapter(List<CourseDetailModel> list) {
        this.list = list;
    }

    @Override
    public int findLayout() {
        return R.layout.layout_course_detail;
    }

    @Override
    public List<?> findList() {
        return list;
    }

    @Override
    public void initialize(ViewHolder holder, int position) {
        CourseDetailModel model = list.get(position);
        holder.title.setText(model.title);

        if (TextUtils.isEmpty(model.summary)) {
            holder.summary.setVisibility(HtmlTextView.GONE);
        } else {
            holder.summary.setVisibility(HtmlTextView.VISIBLE);
            holder.summary.setHtml(model.summary, new HtmlHttpImageGetter(holder.summary));
        }

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(holder.itemView.getContext());
        holder.content.setLayoutManager(layoutManager);
        holder.content.setItemAnimator(new DefaultItemAnimator());
        holder.content.setAdapter(new CourseDetailInnerRecyclerViewAdapter(model.courseDetailInnerModels()));
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(createView(parent));
    }

    public static class ViewHolder extends BaseViewHolder {

        @BindView(R.id.title_course_detail)
        TextView title;

        @BindView(R.id.summary_course_detail)
        HtmlTextView summary;

        @BindView(R.id.content_course_detail)
        RecyclerView content;

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
