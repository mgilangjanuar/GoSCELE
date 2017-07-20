package com.mgilangjanuar.dev.goscele.Adapters;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
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
            android.app.AlertDialog alert = alertDialog.create();

            LinearLayout container = new LinearLayout(context);
            container.setPadding(0, 20, 0, 20);
            container.setOrientation(LinearLayout.VERTICAL);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.leftMargin = context.getResources().getDimensionPixelSize(R.dimen.dialog_margin);
            params.rightMargin = context.getResources().getDimensionPixelSize(R.dimen.dialog_margin);

            TextView title = new TextView(context);
            title.setText(model.title);
            title.setTextSize(18);
            title.setMaxLines(1);
            title.setEllipsize(TextUtils.TruncateAt.END);
            title.setTypeface(null, Typeface.BOLD);
            title.setLayoutParams(params);
            title.setPadding(0, 0, 0, 20);

            TypedValue typedValue = new TypedValue();
            context.getTheme().resolveAttribute(android.R.attr.selectableItemBackground, typedValue, true);

            Button btnShare = new Button(context);
            btnShare.setOnClickListener(v1 -> {
                ShareContentHelper.share(context, model.title + "\n(" + model.author + " - " + model.date + ")\n\n" + model.url);
                alert.dismiss();
            });
            btnShare.setText("Share");
            btnShare.setBackgroundResource(typedValue.resourceId);
            btnShare.setLayoutParams(params);

            Button btnCopy = new Button(context);
            btnCopy.setOnClickListener(v1 -> {
                ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                clipboardManager.setPrimaryClip(ClipData.newPlainText("label", model.url));
                Toast.makeText(context, "URL Copied!", Toast.LENGTH_SHORT).show();
                alert.dismiss();

            });
            btnCopy.setText("Copy URL");
            btnCopy.setBackgroundResource(typedValue.resourceId);
            btnCopy.setLayoutParams(params);

            container.addView(title);
            container.addView(btnCopy);
            container.addView(btnShare);

            alertDialog.setView(container);
            alert.show();

//            PopupMenu popup = new PopupMenu(context, holder.menuMore);
//            MenuInflater inflater = popup.getMenuInflater();
//            inflater.inflate(R.menu.content_default_menu, popup.getMenu());
//            popup.setOnMenuItemClickListener(item -> {
//                switch (item.getItemId()) {
//                    case R.id.menuitem_share:
//                        ShareContentHelper.share(context, model.title + "\n(" + model.author + " - " + model.date + ")\n\n" + model.url);
//                        break;
//                    case R.id.menuitem_copy_url:
//                        ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
//                        clipboardManager.setPrimaryClip(ClipData.newPlainText("label", model.url));
//                        Toast.makeText(context, "URL Copied!", Toast.LENGTH_SHORT).show();
//                        break;
//                }
//                return false;
//            });
//            popup.show();
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
