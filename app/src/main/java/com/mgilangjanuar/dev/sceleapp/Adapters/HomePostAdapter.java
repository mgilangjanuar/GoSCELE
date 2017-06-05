package com.mgilangjanuar.dev.sceleapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mgilangjanuar.dev.sceleapp.ForumDetail;
import com.mgilangjanuar.dev.sceleapp.Helpers.HtmlHandlerHelper;
import com.mgilangjanuar.dev.sceleapp.Models.HomePostModel;
import com.mgilangjanuar.dev.sceleapp.R;

import java.util.List;

/**
 * Created by muhammadgilangjanuar on 5/21/17.
 */

public class HomePostAdapter extends RecyclerView.Adapter<HomePostAdapter.HomePostViewHolder> {
    Context context;
    List<HomePostModel> list;

    public HomePostAdapter(Context context, List<HomePostModel> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public HomePostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new HomePostViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_home_post, parent, false));
    }

    @Override
    public void onBindViewHolder(HomePostViewHolder holder, int position) {
        final HomePostModel model = list.get(position);
        holder.title.setText(model.title);
        holder.author.setText(model.author);
        holder.info.setText(model.date);

        HtmlHandlerHelper helper = new HtmlHandlerHelper(context, model.content);
        helper.setTextViewHTML(holder.content);

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ForumDetail.class).putExtra("url", model.url);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class HomePostViewHolder extends RecyclerView.ViewHolder {

        public TextView title;
        public TextView author;
        public TextView info;
        public TextView content;
        public LinearLayout layout;

        public HomePostViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title_home);
            author = (TextView) itemView.findViewById(R.id.author_home);
            info = (TextView) itemView.findViewById(R.id.info_home);
            content = (TextView) itemView.findViewById(R.id.content_home);
            layout = (LinearLayout) itemView.findViewById(R.id.main_layout_home);
        }
    }


}
