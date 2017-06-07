package com.mgilangjanuar.dev.sceleapp.Presenters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.mgilangjanuar.dev.sceleapp.Adapters.SettingAdapter;
import com.mgilangjanuar.dev.sceleapp.AuthActivity;
import com.mgilangjanuar.dev.sceleapp.MainActivity;
import com.mgilangjanuar.dev.sceleapp.Models.AccountModel;
import com.mgilangjanuar.dev.sceleapp.R;

/**
 * Created by muhammadgilangjanuar on 5/14/17.
 */

public class SettingPresenter {

    public ArrayList<Map<String, String>> listContent;

    Activity activity;
    View view;

    public interface SettingServicePresenter {
        void setupContents(View view);
    }

    public SettingPresenter(Activity activity, View view) {
        this.activity = activity;
        this.view = view;
        listContent = new ArrayList<>();
    }

    public SettingAdapter buildAdapter() {
        if (listContent.isEmpty()) {
            buildList();
        }

        return new SettingAdapter(activity, listContent, this);
    }

    public void buildList() {
        final AccountModel accountModel = new AccountModel(activity);

        listContent.add(new HashMap<String, String>() {{
            put("title", "");
            put("content", "PROFILE INFO");
        }});

        listContent.add(new HashMap<String, String>() {{
            put("title", "Full Name");
            put("content", accountModel.getSavedName());
        }});

        listContent.add(new HashMap<String, String>() {{
            put("title", "Username");
            put("content", accountModel.getSavedUsername());
        }});

        listContent.add(new HashMap<String, String>() {{
            put("title", "");
            put("content", "ACCOUNT SETTINGS");
        }});

        listContent.add(new HashMap<String, String>() {{
            put("title", "Logout");
            put("content", "Logout from " + accountModel.getSavedUsername());
        }});

        listContent.add(new HashMap<String, String>() {{
            put("title", "In-App Browser");
            put("content", "Click toggle for enable/disable in-app browser");
        }});

        listContent.add(new HashMap<String, String>() {{
            put("title", "");
            put("content", "ABOUT");
        }});

        listContent.add(new HashMap<String, String>() {{
            put("title", "Application Name");
            put("content", activity.getResources().getString(R.string.app_name));
        }});

        String version = "";
        try {
            PackageManager manager = activity.getPackageManager();
            final PackageInfo info = manager.getPackageInfo(
                    activity.getPackageName(), 0);
            version = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {}

        final String versionName = version;
        listContent.add(new HashMap<String, String>() {{
            put("title", "Application Version");
            put("content", versionName);
        }});

        listContent.add(new HashMap<String, String>() {{
            put("title", "Credits and Contributions");
            put("content", "Click to view");
        }});
    }

    public void logoutActionHelper() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setCancelable(false);
        builder.setTitle("Logout");
        builder.setMessage("Are you sure want to logout?");
        builder.setInverseBackgroundForced(true);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                (new Thread(new Runnable() {
                    @Override
                    public void run() {
                        ((MainActivity) activity).forceRedirect(new Intent(activity, AuthActivity.class));
                        AuthPresenter authPresenter = new AuthPresenter(activity);
                        authPresenter.logout();
                    }
                })).start();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        final AlertDialog alert = builder.create();

        alert.setOnShowListener( new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.DKGRAY);
                alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.DKGRAY);
            }
        });
        alert.show();
    }


}
