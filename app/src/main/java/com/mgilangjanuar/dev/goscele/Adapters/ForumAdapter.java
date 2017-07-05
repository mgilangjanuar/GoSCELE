package com.mgilangjanuar.dev.goscele.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mgilangjanuar.dev.goscele.ForumDetailActivity;
import com.mgilangjanuar.dev.goscele.Models.ForumModel;
import com.mgilangjanuar.dev.goscele.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by muhammadgilangjanuar on 5/31/17.
 */

public class ForumAdapter extends RecyclerView.Adapter<ForumAdapter.ForumViewHolder> {

    private Context context;
    private List<ForumModel> list;

    public ForumAdapter(Context context, List<ForumModel> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ForumViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ForumViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_forum, parent, false));
    }

    @Override
    public void onBindViewHolder(ForumViewHolder holder, int position) {
        final ForumModel model = list.get(position);
        holder.title.setText(model.title);
        holder.author.setText(model.author);
        holder.lastUpdate.setText("Last update: " + model.lastUpdate);
        holder.layout.setOnClickListener(v -> {
            Intent intent = new Intent(context, ForumDetailActivity.class).putExtra("url", model.url);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ForumViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.title_forum) public TextView title;
        @BindView(R.id.author_forum) public TextView author;
        @BindView(R.id.last_update_forum) public TextView lastUpdate;
        @BindView(R.id.layout_forum) public LinearLayout layout;

        public ForumViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}
