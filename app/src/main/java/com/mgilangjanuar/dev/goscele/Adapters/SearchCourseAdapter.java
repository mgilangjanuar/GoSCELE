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
import com.mgilangjanuar.dev.goscele.Models.CourseModel;
import com.mgilangjanuar.dev.goscele.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by mjanuar on 26/06/17.
 */

public class SearchCourseAdapter extends RecyclerView.Adapter<SearchCourseAdapter.SearchCourseViewHolder> {
    private Context context;
    private List<CourseModel> list;

    public SearchCourseAdapter(Context context, List<CourseModel> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public SearchCourseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SearchCourseViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_search_course, parent, false));
    }

    @Override
    public void onBindViewHolder(SearchCourseViewHolder holder, int position) {
        final CourseModel model = list.get(position);
        holder.title.setText(model.name);
        holder.layout.setOnClickListener(v -> context.startActivity(new Intent(context, InAppBrowserActivity.class).putExtra("url", model.url)));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class SearchCourseViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.title_course_name_search)
        public TextView title;
        @BindView(R.id.layout_course_search)
        public LinearLayout layout;

        public SearchCourseViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}
