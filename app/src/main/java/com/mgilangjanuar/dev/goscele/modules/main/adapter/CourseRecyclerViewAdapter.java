package com.mgilangjanuar.dev.goscele.modules.main.adapter;

import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mgilangjanuar.dev.goscele.R;
import com.mgilangjanuar.dev.goscele.base.BaseFragment;
import com.mgilangjanuar.dev.goscele.base.BaseRecyclerViewAdapter;
import com.mgilangjanuar.dev.goscele.base.BaseViewHolder;
import com.mgilangjanuar.dev.goscele.modules.main.listener.CourseAllListener;
import com.mgilangjanuar.dev.goscele.modules.main.listener.CourseCurrentListener;
import com.mgilangjanuar.dev.goscele.modules.main.model.CourseModel;
import com.mgilangjanuar.dev.goscele.modules.main.presenter.CoursePresenter;
import com.mgilangjanuar.dev.goscele.modules.main.view.CourseCurrentFragment;

import java.util.List;

import butterknife.BindView;

/**
 * Created by mgilangjanuar (mgilangjanuar@gmail.com)
 *
 * @since 2017
 */

public class CourseRecyclerViewAdapter extends BaseRecyclerViewAdapter<CourseRecyclerViewAdapter.ViewHolder> {

    private Context context;
    private BaseFragment fragment;
    private List<CourseModel> list;


    public CourseRecyclerViewAdapter(Context context, BaseFragment fragment, List<CourseModel> list) {
        this.context = context;
        this.fragment = fragment;
        this.list = list;
    }

    @Override
    public int findLayout() {
        return R.layout.layout_course;
    }

    @Override
    public List<?> findList() {
        return list;
    }

    @Override
    public void initialize(final ViewHolder holder, final int position) {
        final CourseModel model = list.get(position);
        holder.title.setText(model.name);
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Not implemented yet", Toast.LENGTH_SHORT).show();
            }
        });

        if (fragment instanceof CourseCurrentFragment) {
            holder.buttonAction.setText(context.getString(R.string.archive));
            holder.buttonAction.getBackground().setColorFilter(context.getResources().getColor(android.R.color.holo_red_light), PorterDuff.Mode.MULTIPLY);
            holder.buttonAction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    model.isCurrent = false;
                    model.save();
                    list.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, getItemCount());
                }
            });
        } else {
            holder.enableButton(!model.isCurrent);
            holder.buttonAction.setText(context.getString(R.string.add_to_current));
            holder.buttonAction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    model.isCurrent = true;
                    model.save();
                    holder.enableButton(false);
                }
            });
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(createView(parent));
    }

    public class ViewHolder extends BaseViewHolder {

        @BindView(R.id.layout_course_list)
        public RelativeLayout relativeLayout;

        @BindView(R.id.title_course_name)
        public TextView title;
        
        @BindView(R.id.button_course)
        public Button buttonAction;

        public ViewHolder(View itemView) {
            super(itemView);
        }

        public void enableButton(boolean enable) {
            if (enable) {
                buttonAction.getBackground().setColorFilter(context.getResources().getColor(android.R.color.holo_green_dark), PorterDuff.Mode.MULTIPLY);
            } else {
                buttonAction.getBackground().setColorFilter(context.getResources().getColor(android.R.color.darker_gray), PorterDuff.Mode.MULTIPLY);
            }
            buttonAction.setEnabled(enable);
        }
    }
}
