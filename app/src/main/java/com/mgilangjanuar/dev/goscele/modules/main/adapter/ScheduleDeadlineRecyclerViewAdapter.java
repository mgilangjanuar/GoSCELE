package com.mgilangjanuar.dev.goscele.modules.main.adapter;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mgilangjanuar.dev.goscele.R;
import com.mgilangjanuar.dev.goscele.base.BaseRecyclerViewAdapter;
import com.mgilangjanuar.dev.goscele.base.BaseViewHolder;
import com.mgilangjanuar.dev.goscele.modules.main.model.ScheduleDeadlineModel;

import org.sufficientlysecure.htmltextview.HtmlHttpImageGetter;
import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.util.Date;
import java.util.List;

import butterknife.BindView;

/**
 * Created by mgilangjanuar (mgilangjanuar@gmail.com)
 *
 * @since 2017
 */

public class ScheduleDeadlineRecyclerViewAdapter extends BaseRecyclerViewAdapter<ScheduleDeadlineRecyclerViewAdapter.ViewHolder> {

    public Date date;

    private List<ScheduleDeadlineModel> list;

    public ScheduleDeadlineRecyclerViewAdapter(Date date, List<ScheduleDeadlineModel> list) {
        this.date = date;
        this.list = list;
    }

    @Override
    public int findLayout() {
        return R.layout.layout_schedule_deadline;
    }

    @Override
    public List<?> findList() {
        return list;
    }

    @Override
    public void initialize(ViewHolder holder, int position) {
        ScheduleDeadlineModel model = list.get(position);
        holder.title.setText(model.title);
        holder.subTitle.setText(model.courseModel.name);
        holder.time.setText(model.time);

        if (TextUtils.isEmpty(model.description)) {
            holder.description.setVisibility(TextView.GONE);
        } else {
            holder.description.setVisibility(TextView.VISIBLE);
            holder.description.setHtml(model.description, new HtmlHttpImageGetter(holder.description));
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(createView(parent));
    }

    public class ViewHolder extends BaseViewHolder {

        @BindView(R.id.title_schedule)
        TextView title;

        @BindView(R.id.subtitle_schedule)
        TextView subTitle;

        @BindView(R.id.description_schedule)
        HtmlTextView description;

        @BindView(R.id.time_schedule)
        TextView time;

        @BindView(R.id.title_card)
        LinearLayout layout;

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
