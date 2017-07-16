package com.mgilangjanuar.dev.goscele.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mgilangjanuar.dev.goscele.ForumDetailActivity;
import com.mgilangjanuar.dev.goscele.Helpers.ShareContentHelper;
import com.mgilangjanuar.dev.goscele.Helpers.WebViewContentHelper;
import com.mgilangjanuar.dev.goscele.Models.HomePostModel;
import com.mgilangjanuar.dev.goscele.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by muhammadgilangjanuar on 5/21/17.
 */

public class HomePostAdapter extends RecyclerView.Adapter<HomePostAdapter.HomePostViewHolder> {
    private Context context;
    private List<HomePostModel> list;

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
        WebViewContentHelper.setWebView(holder.content, model.content);

        holder.layout.setOnClickListener(v -> {
            Intent intent = new Intent(context, ForumDetailActivity.class).putExtra("url", model.url);
            context.startActivity(intent);
        });

        holder.menuMore.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(context, holder.menuMore);
            MenuInflater inflater = popup.getMenuInflater();
            inflater.inflate(R.menu.content_default_menu, popup.getMenu());
            popup.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.menuitem_share) {
                    ShareContentHelper.share(context, model.title + "\n(" + model.author + " - " + model.date + ")\n\n" + model.url);
                }
                return false;
            });
            popup.show();
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class HomePostViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.title_home)
        public TextView title;
        @BindView(R.id.author_home)
        public TextView author;
        @BindView(R.id.info_home)
        public TextView info;
        @BindView(R.id.content_home)
        public WebView content;
        @BindView(R.id.main_layout_home)
        public LinearLayout layout;
        @BindView(R.id.menu_more_home)
        ImageButton menuMore;

        public HomePostViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
