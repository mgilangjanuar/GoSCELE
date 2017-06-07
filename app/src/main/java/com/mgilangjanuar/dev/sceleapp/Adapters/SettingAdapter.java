package com.mgilangjanuar.dev.sceleapp.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.mgilangjanuar.dev.sceleapp.Models.AccountModel;
import com.mgilangjanuar.dev.sceleapp.Presenters.SettingPresenter;
import com.mgilangjanuar.dev.sceleapp.R;

import java.util.List;
import java.util.Map;

/**
 * Created by muhammadgilangjanuar on 6/6/17.
 */

public class SettingAdapter extends RecyclerView.Adapter<SettingAdapter.SettingViewHolder> {

    Context context;
    List<Map<String, String>> list;
    SettingPresenter presenter;

    public SettingAdapter(Context context, List<Map<String, String>> list, SettingPresenter presenter) {
        this.context = context;
        this.list = list;
        this.presenter = presenter;
    }

    @Override
    public SettingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SettingViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_setting, parent, false));
    }

    @Override
    public void onBindViewHolder(SettingViewHolder holder, int position) {
        Map<String, String> model = list.get(position);
        holder.title.setText(model.get("title"));
        holder.subtitle.setText(model.get("content"));
        if (model.get("title").equals("")) {
            holder.relativeLayout.setClickable(false);
        }
        if (model.get("title").contains("Logout")) {
            holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    presenter.logoutActionHelper();
                }
            });
        }
        if (model.get("title").contains("In-App Browser")) {
            AccountModel accountModel = new AccountModel(context);
            holder.toggle.setVisibility(ToggleButton.VISIBLE);
            holder.toggle.setChecked(accountModel.isUsingInAppBrowser() ? true : false);
            holder.toggle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AccountModel accountModel = new AccountModel(context);
                    accountModel.getSavedName();
                    accountModel.getSavedPassword();
                    accountModel.getSavedUsername();
                    accountModel.isUsingInAppBrowser = accountModel.isUsingInAppBrowser() ? false : true;
                    accountModel.save();
                }
            });
        }
        if (model.get("title").contains("Credits and Contributions")) {
            holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LayoutInflater inflater= LayoutInflater.from(context);
                    View view=inflater.inflate(R.layout.layout_credits, null);

                    TextView textview=(TextView)view.findViewById(R.id.content_credits);
                    textview.setText(Html.fromHtml("" +
                            "<br /><h3>Main Developer</h3>" +
                            "<p>M Gilang Januar</p>" +
                            "<br /><h3>Contributor</h3>" +
                            "<p>Sumarliyanti</p>" +
                            "<br /><h3>Beta Tester</h3>" +
                            "<p>" +
                            "Burhan Sidqi<br />" +
                            "Fandika Okdiba<br />" +
                            "Ayu Annisa<br />" +
                            "Kustiawanto Halim<br />" +
                            "Zamil Majdy<br />" +
                            "Salsabila Nadhifah<br />" +
                            "Farhan Je<br />" +
                            "Hadi Syah Putra<br />" +
                            "Wresni Ronggowerdhi<br />" +
                            "Rafi<br />" +
                            "Princess Janf<br />" +
                            "Andri Nur Rochman<br />" +
                            "Fachrur Rozi<br />" +
                            "Gilang Gumilar<br />" +
                            "Izzan Nuruddin<br />" +
                            "Fathoni<br />" +
                            "</p>"));
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                    alertDialog.setTitle("Credits and Contributions");
                    alertDialog.setView(view);
                    alertDialog.setPositiveButton("Close", null);
                    final AlertDialog alert = alertDialog.create();
                    alert.setOnShowListener( new DialogInterface.OnShowListener() {
                        @Override
                        public void onShow(DialogInterface arg0) {
                            alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.DKGRAY);
                        }
                    });
                    alert.show();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class SettingViewHolder extends RecyclerView.ViewHolder {

        public TextView title;
        public TextView subtitle;
        public ToggleButton toggle;
        RelativeLayout relativeLayout;

        public SettingViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title_setting);
            subtitle = (TextView) itemView.findViewById(R.id.subtitle_setting);
            toggle = (ToggleButton) itemView.findViewById(R.id.toggle_button_setting);
            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.relative_layout_setting);
        }
    }
}
