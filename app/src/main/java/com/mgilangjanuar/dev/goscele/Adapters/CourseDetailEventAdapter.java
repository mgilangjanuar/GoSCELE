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

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by muhammadgilangjanuar on 5/22/17.
 */

public class CourseDetailEventAdapter extends RecyclerView.Adapter<CourseDetailEventAdapter.CourseDetailEventViewHolder> {

    private Context context;
    private List<CourseEventModel> list;

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
        holder.layout.setOnClickListener(v -> context.startActivity((new Intent(context, InAppBrowserActivity.class)).putExtra("url", model.url)));
    }

    @Override
    public int getItemCount() {
        if (list == null) {
            return 0;
        }
        return list.size();
    }

    public class CourseDetailEventViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.title_course_detail_event) public TextView title;
        @BindView(R.id.info_course_detail_event) public TextView info;
        @BindView(R.id.layout_course_detail_event) public LinearLayout layout;

        public CourseDetailEventViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
