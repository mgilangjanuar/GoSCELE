package com.mgilangjanuar.dev.goscele.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mgilangjanuar.dev.goscele.CourseDetailActivity;
import com.mgilangjanuar.dev.goscele.Models.CourseModel;
import com.mgilangjanuar.dev.goscele.Presenters.CoursePresenter;
import com.mgilangjanuar.dev.goscele.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by muhammadgilangjanuar on 5/16/17.
 */

public class CurrentCoursesViewAdapter extends RecyclerView.Adapter<CurrentCoursesViewAdapter.CurrentCoursesViewHolder> {

    private Context context;
    private List<CourseModel> list;
    private CoursePresenter coursePresenter;

    public CurrentCoursesViewAdapter(Context context, CoursePresenter coursePresenter, List<CourseModel> list) {
        this.context = context;
        this.coursePresenter = coursePresenter;
        this.list = list;
    }

    @Override
    public CurrentCoursesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CurrentCoursesViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_course, parent, false));
    }

    @Override
    public void onBindViewHolder(final CurrentCoursesViewHolder holder, final int position) {
        final CourseModel courseModel = list.get(position);
        holder.title.setText(courseModel.name);

        holder.buttonAction.setText("Archive");
        holder.buttonAction.getBackground().setColorFilter(context.getResources().getColor(android.R.color.holo_red_light), PorterDuff.Mode.MULTIPLY);
        holder.buttonAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                coursePresenter.removeFromCurrentByIndex(position);
                list.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, getItemCount());
                coursePresenter.isDataAllCoursesViewAdapterChanged = true;
            }
        });

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity((new Intent(context, CourseDetailActivity.class)).putExtra("url", courseModel.url));
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class CurrentCoursesViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.layout_course_list)
        public RelativeLayout relativeLayout;
        @BindView(R.id.title_course_name)
        public TextView title;
        @BindView(R.id.button_course)
        public Button buttonAction;

        public CurrentCoursesViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
