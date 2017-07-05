package com.mgilangjanuar.dev.goscele.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mgilangjanuar.dev.goscele.InAppBrowserActivity;
import com.mgilangjanuar.dev.goscele.Models.CourseEventModel;
import com.mgilangjanuar.dev.goscele.R;

import java.util.List;

/**
 * Created by muhammadgilangjanuar on 5/22/17.
 */

public class CourseDetailEventAdapter extends RecyclerView.Adapter<CourseDetailEventAdapter.CourseDetailEventViewHolder> {

    Context context;
    List<CourseEventModel> list;

    public CourseDetailEventAdapter(Context context, List<CourseEventModel> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public CourseDetailEventAdapter.CourseDetailEventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CourseDetailEventViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_course_detail_event, parent, false));
    }

    @Override
    public void onBindViewHolder(final CourseDetailEventAdapter.CourseDetailEventViewHolder holder, int position) {
        final CourseEventModel model = list.get(position);
        holder.title.setText(model.title);
        holder.info.setText(model.info);
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity((new Intent(context, InAppBrowserActivity.class)).putExtra("url", model.url));
            }
        });
    }

    @Override
    public int getItemCount() {
        if (list == null) {
            return 0;
        }
        return list.size();
    }

    public class CourseDetailEventViewHolder extends RecyclerView.ViewHolder {

        public TextView title;
        public TextView info;
        public LinearLayout layout;

        public CourseDetailEventViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title_course_detail_event);
            info = (TextView) itemView.findViewById(R.id.info_course_detail_event);
            layout = (LinearLayout) itemView.findViewById(R.id.layout_course_detail_event);
        }
    }
}
