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

/**
 * Created by muhammadgilangjanuar on 5/31/17.
 */

public class ForumAdapter extends RecyclerView.Adapter<ForumAdapter.ForumViewHolder> {

    Context context;
    List<ForumModel> list;

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
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ForumDetailActivity.class).putExtra("url", model.url);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ForumViewHolder extends RecyclerView.ViewHolder {

        public TextView title;
        public TextView author;
        public TextView lastUpdate;
        public LinearLayout layout;

        public ForumViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title_forum);
            author = (TextView) itemView.findViewById(R.id.author_forum);
            lastUpdate = (TextView) itemView.findViewById(R.id.last_update_forum);
            layout = (LinearLayout) itemView.findViewById(R.id.layout_forum);
        }
    }


}
