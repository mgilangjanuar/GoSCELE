package com.mgilangjanuar.dev.goscele.modules.course.adapter;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mgilangjanuar.dev.goscele.R;
import com.mgilangjanuar.dev.goscele.base.BaseRecyclerViewAdapter;
import com.mgilangjanuar.dev.goscele.base.BaseViewHolder;
import com.mgilangjanuar.dev.goscele.modules.browser.view.BrowserActivity;
import com.mgilangjanuar.dev.goscele.modules.course.model.CourseDetailInnerModel;
import com.mgilangjanuar.dev.goscele.utils.Constant;

import org.sufficientlysecure.htmltextview.HtmlHttpImageGetter;
import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by mgilangjanuar (mgilangjanuar@gmail.com)
 *
 * @since 2017
 */

public class CourseDetailInnerRecyclerViewAdapter extends BaseRecyclerViewAdapter<CourseDetailInnerRecyclerViewAdapter.ViewHolder> {

    List<CourseDetailInnerModel> list;

    public CourseDetailInnerRecyclerViewAdapter(List<CourseDetailInnerModel> list) {
        this.list = list;
    }

    @Override
    public int findLayout() {
        return R.layout.layout_inner_course_detail;
    }

    @Override
    public List<?> findList() {
        return list;
    }

    @Override
    public void initialize(ViewHolder holder, int position) {
        final CourseDetailInnerModel model = list.get(position);

        if (!model.title.equals("")) {
            holder.title.setVisibility(TextView.VISIBLE);
            holder.title.setText(model.title);
            holder.title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), BrowserActivity.class);
                    intent.putExtra(Constant.URL, model.url);
                    v.getContext().startActivity(intent);
                }
            });
        }

        if (!model.comment.equals("")) {
            holder.layoutComment.setVisibility(TextView.VISIBLE);
            holder.comment.setHtml(model.comment, new HtmlHttpImageGetter(holder.comment));
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(createView(parent));
    }

    public static class ViewHolder extends BaseViewHolder {

        @BindView(R.id.title_inner_course_post)
        TextView title;

        @BindView(R.id.comment_inner_course_post)
        HtmlTextView comment;

        @BindView(R.id.layout_comment_inner_course_post)
        LinearLayout layoutComment;

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
