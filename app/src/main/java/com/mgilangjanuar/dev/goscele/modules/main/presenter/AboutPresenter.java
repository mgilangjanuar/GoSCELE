package com.mgilangjanuar.dev.goscele.modules.main.presenter;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.mgilangjanuar.dev.goscele.R;
import com.mgilangjanuar.dev.goscele.modules.common.model.DataViewModel;
import com.mgilangjanuar.dev.goscele.modules.common.model.UserModel;
import com.mgilangjanuar.dev.goscele.modules.main.adapter.AboutRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mgilangjanuar (mgilangjanuar@gmail.com)
 *
 * @since 2017
 */

public class AboutPresenter {

    public AboutRecyclerViewAdapter buildAdapter(Context context) {
        UserModel userModel = new UserModel().find().executeSingle();

        List<DataViewModel> models = new ArrayList<>();
        models.add(new DataViewModel("", "User Information"));
        models.add(new DataViewModel("Full Name", userModel.name));
        models.add(new DataViewModel("Username", userModel.username));

        models.add(new DataViewModel("", "Account"));
        models.add(new DataViewModel("Logout", "Clear all data in app and logout"));

        models.add(new DataViewModel("", "About Application"));
        models.add(new DataViewModel("Application Name", context.getString(R.string.app_name)));
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            models.add(new DataViewModel("Application Version", info.versionName));
        } catch (PackageManager.NameNotFoundException e) {
        }
        models.add(new DataViewModel("Development Channel", "Keep update with us"));
        models.add(new DataViewModel("GitHub Repository", "View our d̶i̶r̶t̶y̶ codes"));
        models.add(new DataViewModel("License", "MIT"));

        return new AboutRecyclerViewAdapter(models);
    }
}
