package com.mgilangjanuar.dev.goscele.Adapters;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
            android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(context);
            alertDialog.setTitle(model.title);

            LinearLayout container = new LinearLayout(context);
            container.setPadding(0, 40, 0, 0);
            container.setOrientation(LinearLayout.VERTICAL);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            TypedValue typedValue = new TypedValue();
            context.getTheme().resolveAttribute(android.R.attr.selectableItemBackground, typedValue, true);

            View divider = new View(context);
            divider.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1));
            divider.setBackgroundColor(container.getResources().getColor(android.R.color.darker_gray));

            Button btnShare = new Button(context);
            btnShare.setText("Share");
            btnShare.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
            btnShare.setPadding(50, 0, 50, 0);
            btnShare.setAllCaps(false);
            btnShare.setBackgroundResource(typedValue.resourceId);
            btnShare.setLayoutParams(params);

            View divider1 = new View(context);
            divider1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1));
            divider1.setBackgroundColor(container.getResources().getColor(android.R.color.darker_gray));

            Button btnCopy = new Button(context);
            btnCopy.setText("Copy URL");
            btnCopy.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
            btnCopy.setPadding(50, 0, 50, 0);
            btnCopy.setAllCaps(false);
            btnCopy.setBackgroundResource(typedValue.resourceId);
            btnCopy.setLayoutParams(params);

            container.addView(divider);
            container.addView(btnCopy);
            container.addView(divider1);
            container.addView(btnShare);

            alertDialog.setView(container);
            android.app.AlertDialog alert = alertDialog.create();

            btnShare.setOnClickListener(v1 -> {
                ShareContentHelper.share(context, model.title + "\n(" + model.author + " - " + model.date + ")\n\n" + model.url);
                alert.dismiss();
            });
            btnCopy.setOnClickListener(v1 -> {
                ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                clipboardManager.setPrimaryClip(ClipData.newPlainText("label", model.url));
                Toast.makeText(context, "URL Copied!", Toast.LENGTH_SHORT).show();
                alert.dismiss();

            });

            alert.show();
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
