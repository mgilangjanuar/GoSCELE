package com.mgilangjanuar.dev.goscele.modules.main.adapter;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mgilangjanuar.dev.goscele.R;
import com.mgilangjanuar.dev.goscele.base.BaseRecyclerViewAdapter;
import com.mgilangjanuar.dev.goscele.base.BaseViewHolder;
import com.mgilangjanuar.dev.goscele.modules.forum.detail.view.ForumDetailActivity;
import com.mgilangjanuar.dev.goscele.modules.main.model.HomeModel;
import com.mgilangjanuar.dev.goscele.utils.Constant;
import com.mgilangjanuar.dev.goscele.utils.ShareContentUtil;

import org.sufficientlysecure.htmltextview.HtmlHttpImageGetter;
import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by mgilangjanuar (mgilangjanuar@gmail.com)
 *
 * @since 2017
 */

public class HomeRecyclerViewAdapter extends BaseRecyclerViewAdapter<HomeRecyclerViewAdapter.ViewHolder> {

    private List<HomeModel> list;

    public HomeRecyclerViewAdapter(List<HomeModel> list) {
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(createView(parent));
    }

    @Override
    public int findLayout() {
        return R.layout.layout_home_post;
    }

    @Override
    public List<?> findList() {
        return list;
    }

    @Override
    public void initialize(ViewHolder holder, int position) {
        final HomeModel model = list.get(position);
        holder.model = model;

        holder.title.setText(model.title);
        holder.author.setText(model.author);
        holder.info.setText(model.date);

        holder.content.setHtml(model.content, new HtmlHttpImageGetter(holder.content));
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ForumDetailActivity.class).putExtra(Constant.URL, model.url);
                v.getContext().startActivity(intent);
            }
        });
        holder.menuMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buildMenuView(v.getContext(), model);
            }
        });
    }

    private void buildMenuView(Context context, final HomeModel model) {
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
        btnShare.setText(context.getString(R.string.share));
        btnShare.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
        btnShare.setPadding(50, 0, 50, 0);
        btnShare.setAllCaps(false);
        btnShare.setBackgroundResource(typedValue.resourceId);
        btnShare.setLayoutParams(params);

        View divider1 = new View(context);
        divider1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1));
        divider1.setBackgroundColor(container.getResources().getColor(android.R.color.darker_gray));

        Button btnCopy = new Button(context);
        btnCopy.setText(context.getString(R.string.copy_url));
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
        final android.app.AlertDialog alert = alertDialog.create();

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareContentUtil.share(v.getContext(), model.title + "\n(" + model.author + " - " + model.date + ")\n\n" + model.url);
                alert.dismiss();
            }
        });
        btnCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboardManager = (ClipboardManager) v.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                clipboardManager.setPrimaryClip(ClipData.newPlainText(Constant.LABEL, model.url));
                Toast.makeText(v.getContext(), v.getContext().getString(R.string.url_copied), Toast.LENGTH_SHORT).show();
                alert.dismiss();
            }
        });

        alert.show();
    }

    public class ViewHolder extends BaseViewHolder {

        @BindView(R.id.title_home)
        TextView title;

        @BindView(R.id.author_home)
        TextView author;

        @BindView(R.id.info_home)
        TextView info;

        @BindView(R.id.content_home)
        HtmlTextView content;

        @BindView(R.id.main_layout_home)
        LinearLayout layout;

        @BindView(R.id.menu_more_home)
        ImageButton menuMore;

        private HomeModel model;

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

}
