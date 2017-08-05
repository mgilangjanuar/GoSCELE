package com.mgilangjanuar.dev.goscele.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mgilangjanuar.dev.goscele.Models.ScheduleDailyModel;
import com.mgilangjanuar.dev.goscele.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by gilang on 8/5/17.
 */

public class ScheduleDailyAdapter extends RecyclerView.Adapter<ScheduleDailyAdapter.ScheduleDailyViewHolder> {

    private Context context;
    private List<ScheduleDailyModel.ListSchedule> list;

    public ScheduleDailyAdapter(Context context, List<ScheduleDailyModel.ListSchedule> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ScheduleDailyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ScheduleDailyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_schedule_daily, parent, false));
    }

    @Override
    public void onBindViewHolder(ScheduleDailyViewHolder holder, int position) {
        ScheduleDailyModel.ListSchedule model = list.get(position);
        holder.title.setText(model.day);

        CharSequence builder = null;
        for (ScheduleDailyModel.Schedule e : model.scheduleList) {
            String course = e.desc.substring(0, e.desc.indexOf(" Ruang: "));
            String room = e.desc.substring(e.desc.indexOf(" Ruang: ") + 8);
            SpannableString title = new SpannableString(course);
            title.setSpan(new RelativeSizeSpan(1.2f), 0, title.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            SpannableString time = new SpannableString("R. " + room + "\t\t" + e.time);
            time.setSpan(new ForegroundColorSpan(context.getResources().getColor(android.R.color.darker_gray)), 0, time.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            if (builder == null) {
                builder = TextUtils.concat(title, "\n", time + "\n\n");
            } else {
                builder = TextUtils.concat(builder, title, "\n", time + "\n\n");
            }
        }
        holder.content.setText(builder == null ? "(empty)\n\n" : builder);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ScheduleDailyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.title_schedule_daily)
        TextView title;
        @BindView(R.id.content_schedule_daily)
        TextView content;

        public ScheduleDailyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
