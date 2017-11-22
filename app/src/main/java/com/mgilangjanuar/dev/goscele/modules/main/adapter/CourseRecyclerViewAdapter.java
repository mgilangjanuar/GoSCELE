package com.mgilangjanuar.dev.goscele.modules.main.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mgilangjanuar.dev.goscele.R;
import com.mgilangjanuar.dev.goscele.base.BaseFragment;
import com.mgilangjanuar.dev.goscele.base.BaseRecyclerViewAdapter;
import com.mgilangjanuar.dev.goscele.base.BaseViewHolder;
import com.mgilangjanuar.dev.goscele.modules.main.model.CourseModel;
import com.mgilangjanuar.dev.goscele.modules.main.view.CourseAllFragment;
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
        if (fragment instanceof CourseAllFragment && model.isCurrent) {
            holder.title.setTextColor(Color.GRAY);
        }
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Not implemented yet", Toast.LENGTH_SHORT).show();
            }
        });
        if (fragment instanceof CourseCurrentFragment || (fragment instanceof CourseAllFragment && !model.isCurrent)) {
            holder.relativeLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                    alertDialog.setTitle(model.name);

                    LinearLayout container = new LinearLayout(context);
                    container.setPadding(0, 40, 0, 0);
                    container.setOrientation(LinearLayout.VERTICAL);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                    TypedValue typedValue = new TypedValue();
                    context.getTheme().resolveAttribute(android.R.attr.selectableItemBackground, typedValue, true);

                    View divider = new View(context);
                    divider.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1));
                    divider.setBackgroundColor(container.getResources().getColor(android.R.color.darker_gray));

                    Button btnAction = new Button(context);
                    if (model.isCurrent) {
                        btnAction.setText(context.getString(R.string.archive));
                    } else {
                        btnAction.setText(context.getString(R.string.add_to_current));
                    }
                    btnAction.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
                    btnAction.setPadding(50, 0, 50, 0);
                    btnAction.setAllCaps(false);
                    btnAction.setBackgroundResource(typedValue.resourceId);
                    btnAction.setLayoutParams(params);

                    container.addView(divider);
                    container.addView(btnAction);

                    alertDialog.setView(container);
                    final android.app.AlertDialog alert = alertDialog.create();

                    btnAction.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (fragment instanceof CourseCurrentFragment) {
                                list.remove(position);
                                notifyItemRemoved(position);
                                notifyItemRangeChanged(position, getItemCount());
                            } else if (fragment instanceof CourseAllFragment && !model.isCurrent) {
                                holder.title.setTextColor(Color.GRAY);
                                holder.relativeLayout.setOnLongClickListener(new View.OnLongClickListener() {
                                    @Override
                                    public boolean onLongClick(View v) {
                                        return false;
                                    }
                                });
                            }

                            model.isCurrent = !model.isCurrent;
                            model.save();
                            alert.dismiss();
                        }
                    });

                    alert.show();
                    return true;
                }
            });
        }
    }

    @Override
    public void onViewAttachedToWindow(ViewHolder holder) {
        CourseModel model = null;
        for(CourseModel e: list) {
            if (holder.title.getText().toString().equals(e.name)) {
                model = e;
                break;
            }
        }
        if (model != null && fragment instanceof CourseAllFragment) {
            holder.title.setTextColor(model.isCurrent ? Color.GRAY : Color.BLACK);
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

        public ViewHolder(View itemView) {
            super(itemView);
        }

    }
}
