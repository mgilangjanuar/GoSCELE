package com.mgilangjanuar.dev.goscele.modules.main.presenter;

import com.mgilangjanuar.dev.goscele.modules.common.model.DataViewModel;
import com.mgilangjanuar.dev.goscele.modules.common.model.UserModel;
import com.mgilangjanuar.dev.goscele.modules.main.adapter.SettingRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mgilangjanuar (mgilangjanuar@gmail.com)
 *
 * @since 2017
 */

public class SettingPresenter {

    public SettingRecyclerViewAdapter buildAdapter() {
        UserModel userModel = new UserModel().find().executeSingle();

        List<DataViewModel> models = new ArrayList<>();
        models.add(new DataViewModel("Full Name", userModel.name));
        models.add(new DataViewModel("Username", userModel.username));

        models.add(new DataViewModel("Logout", "Clear all data in app and logout"));
        return new SettingRecyclerViewAdapter(models);
    }
}
