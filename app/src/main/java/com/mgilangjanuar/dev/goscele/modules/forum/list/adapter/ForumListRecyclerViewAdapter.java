package com.mgilangjanuar.dev.goscele.modules.forum.list.adapter;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mgilangjanuar.dev.goscele.R;
import com.mgilangjanuar.dev.goscele.base.BaseRecyclerViewAdapter;
import com.mgilangjanuar.dev.goscele.base.BaseViewHolder;
import com.mgilangjanuar.dev.goscele.modules.forum.detail.view.ForumDetailActivity;
import com.mgilangjanuar.dev.goscele.modules.forum.list.model.ForumModel;
import com.mgilangjanuar.dev.goscele.utils.Constant;

import java.util.List;

import butterknife.BindView;

/**
 * Created by mgilangjanuar (mgilangjanuar@gmail.com)
 *
 * @since 2017
 */

public class ForumListRecyclerViewAdapter extends BaseRecyclerViewAdapter<ForumListRecyclerViewAdapter.ViewHolder> {

    private List<ForumModel> list;

    public ForumListRecyclerViewAdapter(List<ForumModel> list) {
        this.list = list;
    }

    @Override
    public int findLayout() {
        return R.layout.layout_forum_list;
    }

    @Override
    public List<?> findList() {
        return list;
    }

    @Override
    public void initialize(final ViewHolder holder, int position) {
        final ForumModel model = list.get(position);
        holder.title.setText(model.title);
        holder.author.setText(model.author);
        holder.lastUpdate.setText("Last update: " + model.lastUpdate);
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ForumDetailActivity.class).putExtra(Constant.URL, model.url);
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(createView(parent));
    }

    public static class ViewHolder extends BaseViewHolder {

        @BindView(R.id.title_forum)
        public TextView title;

        @BindView(R.id.author_forum)
        public TextView author;

        @BindView(R.id.last_update_forum)
        public TextView lastUpdate;

        @BindView(R.id.layout_forum)
        public LinearLayout layout;

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
