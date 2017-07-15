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

public class AllCoursesViewAdapter extends RecyclerView.Adapter<AllCoursesViewAdapter.AllCoursesViewHolder> {

    private List<CourseModel> list;
    private Context context;
    private CoursePresenter coursePresenter;

    public AllCoursesViewAdapter(Context context, CoursePresenter coursePresenter, List<CourseModel> list) {
        this.context = context;
        this.coursePresenter = coursePresenter;
        this.list = list;
    }

    @Override
    public AllCoursesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AllCoursesViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_course, parent, false));
    }

    @Override
    public void onBindViewHolder(final AllCoursesViewHolder holder, int position) {
        final CourseModel courseModel = list.get(position);
        holder.title.setText(courseModel.name);
        holder.buttonAction.setText("Add To Current");
        holder.buttonAction.setOnClickListener(v -> {
            coursePresenter.addToCurrent(courseModel);
            holder.disableButton();
            CoursePresenter.isDataCurrentCoursesViewAdapterChanged = true;
        });

        holder.relativeLayout.setOnClickListener(v -> context.startActivity((new Intent(context, CourseDetailActivity.class)).putExtra("url", courseModel.url)));

        if (coursePresenter.alreadyExistInCurrent(courseModel)) {
            holder.disableButton();
        } else {
            holder.enableButton();
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onViewRecycled(final AllCoursesViewHolder holder) {
        super.onViewRecycled(holder);

        if (coursePresenter.alreadyExistInCurrent(new CourseModel() {{
            name = holder.title.getText().toString();
        }})) {
            holder.disableButton();
        } else {
            holder.enableButton();
        }
    }

    public class AllCoursesViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.layout_course_list)
        public RelativeLayout relativeLayout;
        @BindView(R.id.title_course_name)
        public TextView title;
        @BindView(R.id.button_course)
        public Button buttonAction;

        public AllCoursesViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void enableButton() {
            buttonAction.getBackground().setColorFilter(context.getResources().getColor(android.R.color.holo_green_dark), PorterDuff.Mode.MULTIPLY);
            buttonAction.setEnabled(true);
        }

        public void disableButton() {
            buttonAction.getBackground().setColorFilter(context.getResources().getColor(android.R.color.darker_gray), PorterDuff.Mode.MULTIPLY);
            buttonAction.setEnabled(false);
        }
    }


}
