package com.mgilangjanuar.dev.goscele.modules.main.adapter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
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

public class AboutRecyclerViewAdapter extends BaseRecyclerViewAdapter<AboutRecyclerViewAdapter.ViewHolder> {

    private List<DataViewModel> list;

    public AboutRecyclerViewAdapter(List<DataViewModel> list) {
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
        final DataViewModel model = list.get(position);
        holder.title.setText(model.title);
        holder.subtitle.setText(model.content);

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if ("Logout".equals(model.title)) {
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
                } else if ("GitHub Repository".equals(model.title)) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("https://github.com/mgilangjanuar/GoSCELE"));
                    v.getContext().startActivity(intent);
                } else if ("License".equals(model.title)) {
                    new AlertDialog.Builder(v.getContext())
                            .setTitle("License")
                            .setMessage("MIT License\n" +
                                    "\n" +
                                    "Copyright (c) 2017 M Gilang Januar\n" +
                                    "\n" +
                                    "Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the \"Software\"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:\n" +
                                    "\n" +
                                    "The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.\n" +
                                    "\n" +
                                    "THE SOFTWARE IS PROVIDED \"AS IS\", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.\n")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .show();
                } else if ("Development Channel".equals(model.title)) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("https://t.me/joinchat/BBhj00GbOxkbUNh4AyW1Ug"));
                    v.getContext().startActivity(intent);
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
