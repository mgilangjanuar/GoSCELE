package com.mgilangjanuar.dev.sceleapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import com.mgilangjanuar.dev.sceleapp.CourseDetail;
import com.mgilangjanuar.dev.sceleapp.Helpers.HtmlHandlerHelper;
import com.mgilangjanuar.dev.sceleapp.InAppBrowser;
import com.mgilangjanuar.dev.sceleapp.Models.ScheduleModel;
import com.mgilangjanuar.dev.sceleapp.R;

import org.xml.sax.XMLReader;

/**
 * Created by muhammadgilangjanuar on 5/17/17.
 */

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder> {

    Context context;
    List<ScheduleModel> list;

    public class ScheduleViewHolder extends RecyclerView.ViewHolder {

        public TextView title;
        public TextView subTitle;
        public TextView description;
        public TextView time;
        public Button share;
        public Button course;
        public LinearLayout layout;

        public ScheduleViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title_schedule);
            subTitle = (TextView) itemView.findViewById(R.id.subtitle_schedule);
            description = (TextView) itemView.findViewById(R.id.description_schedule);
            time = (TextView) itemView.findViewById(R.id.time_schedule);
            share = (Button) itemView.findViewById(R.id.button_share_schedule);
            course = (Button) itemView.findViewById(R.id.button_view_course_in_schedule);
            layout = (LinearLayout) itemView.findViewById(R.id.title_card);
        }
    }

    public ScheduleAdapter(Context context, List<ScheduleModel> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ScheduleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ScheduleViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_schedule, parent, false));
    }

    @Override
    public void onBindViewHolder(final ScheduleViewHolder holder, int position) {
        final ScheduleModel scheduleModel = list.get(position);
        holder.title.setText(scheduleModel.title);
        holder.subTitle.setText(scheduleModel.courseModel.name);
        holder.time.setText(scheduleModel.time);

        if (scheduleModel.description.equals("")) {
            holder.description.setVisibility(TextView.GONE);
        } else {
            holder.description.setVisibility(TextView.VISIBLE);
            HtmlHandlerHelper htmlHandlerHelper = new HtmlHandlerHelper(context, scheduleModel.description);
            htmlHandlerHelper.setTextViewHTML(holder.description);
        }

        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, scheduleModel.courseModel.name + "\n\n" + scheduleModel.title + "\n" + "("+scheduleModel.date + " " +scheduleModel.time+")" + "\n\n" + scheduleModel.url);
                holder.itemView.getContext().startActivity(Intent.createChooser(shareIntent, "Share Link"));
            }
        });

        holder.course.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity((new Intent(context, CourseDetail.class)).putExtra("url", scheduleModel.courseModel.url));
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}
