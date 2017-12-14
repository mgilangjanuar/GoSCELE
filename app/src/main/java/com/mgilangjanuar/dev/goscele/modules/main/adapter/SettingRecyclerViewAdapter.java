package com.mgilangjanuar.dev.goscele.modules.main.adapter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.mgilangjanuar.dev.goscele.R;
import com.mgilangjanuar.dev.goscele.base.BaseRecyclerViewAdapter;
import com.mgilangjanuar.dev.goscele.base.BaseViewHolder;
import com.mgilangjanuar.dev.goscele.modules.auth.view.AuthActivity;
import com.mgilangjanuar.dev.goscele.modules.common.model.CookieModel;
import com.mgilangjanuar.dev.goscele.modules.common.model.DataViewModel;
import com.mgilangjanuar.dev.goscele.modules.common.model.UserModel;

import java.util.List;

import butterknife.BindView;

/**
 * Created by mgilangjanuar (mgilangjanuar@gmail.com)
 *
 * @since 2017
 */

public class SettingRecyclerViewAdapter extends BaseRecyclerViewAdapter<SettingRecyclerViewAdapter.ViewHolder> {

    private List<DataViewModel> list;

    public SettingRecyclerViewAdapter(List<DataViewModel> list) {
        this.list = list;
    }

    @Override
    public int findLayout() {
        return R.layout.layout_setting;
    }

    @Override
    public List<?> findList() {
        return list;
    }

    @Override
    public void initialize(ViewHolder holder, final int position) {
        DataViewModel model = list.get(position);
        holder.title.setText(model.title);
        holder.subtitle.setText(model.content);

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (position == 2) {
                    new AlertDialog.Builder(v.getContext())
                            .setTitle("Logout")
                            .setMessage("Are you sure?")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    UserModel userModel = new UserModel().find().executeSingle();
                                    userModel.delete();

                                    List<CookieModel> cookies = new CookieModel().find().execute();
                                    for (CookieModel cookie: cookies) cookie.delete();

                                    Intent intent = new Intent(v.getContext(), AuthActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    v.getContext().startActivity(intent);
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .show();
                }
            }
        });
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(createView(parent));
    }

    public static class ViewHolder extends BaseViewHolder {

        @BindView(R.id.title_setting)
        TextView title;

        @BindView(R.id.subtitle_setting)
        TextView subtitle;

        @BindView(R.id.relative_layout_setting)
        RelativeLayout layout;

        @BindView(R.id.toggle_button_setting)
        ToggleButton toggleButton;

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
