package com.mgilangjanuar.dev.goscele.modules.main.adapter;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mgilangjanuar.dev.goscele.R;
import com.mgilangjanuar.dev.goscele.base.BaseRecyclerViewAdapter;
import com.mgilangjanuar.dev.goscele.base.BaseViewHolder;
import com.mgilangjanuar.dev.goscele.modules.main.model.ScheduleDailyGroupModel;
import com.mgilangjanuar.dev.goscele.modules.main.model.ScheduleDailyModel;

import java.util.List;

import butterknife.BindView;

/**
 * Created by mgilangjanuar (mgilangjanuar@gmail.com)
 *
 * @since 2017
 */

public class ScheduleDailyRecyclerViewAdapter extends BaseRecyclerViewAdapter<ScheduleDailyRecyclerViewAdapter.ViewHolder> {

    private List<ScheduleDailyGroupModel> list;

    public ScheduleDailyRecyclerViewAdapter(List<ScheduleDailyGroupModel> list) {
        this.list = list;
    }

    @Override
    public int findLayout() {
        return R.layout.layout_schedule_daily;
    }

    @Override
    public List<?> findList() {
        return list;
    }

    @Override
    public void initialize(ViewHolder holder, int position) {
        ScheduleDailyGroupModel model = list.get(position);
        holder.title.setText(model.day);

        CharSequence builder = null;
        for (ScheduleDailyModel e : model.scheduleDailyModels()) {
            String course = e.desc.substring(0, e.desc.indexOf(" Ruang: "));
            String room = e.desc.substring(e.desc.indexOf(" Ruang: ") + 8);
            SpannableString title = new SpannableString(course);
            title.setSpan(new RelativeSizeSpan(1.2f), 0, title.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            SpannableString time = new SpannableString("R. " + room + "\t\t" + e.time);
            time.setSpan(new ForegroundColorSpan(holder.itemView.getContext().getResources().getColor(android.R.color.darker_gray)), 0, time.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            if (builder == null) {
                builder = TextUtils.concat(title, "\n", time + "\n\n");
            } else {
                builder = TextUtils.concat(builder, title, "\n", time + "\n\n");
            }
        }
        holder.content.setText(builder == null ? "(empty)\n\n" : builder);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(createView(parent));
    }

    public static class ViewHolder extends BaseViewHolder {

        @BindView(R.id.title_schedule_daily)
        TextView title;

        @BindView(R.id.content_schedule_daily)
        TextView content;

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
