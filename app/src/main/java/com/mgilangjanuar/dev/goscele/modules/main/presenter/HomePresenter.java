package com.mgilangjanuar.dev.goscele.modules.main.presenter;

import com.mgilangjanuar.dev.goscele.base.BasePresenter;
import com.mgilangjanuar.dev.goscele.modules.main.adapter.HomeRecyclerViewAdapter;
import com.mgilangjanuar.dev.goscele.modules.main.listener.HomeListener;
import com.mgilangjanuar.dev.goscele.modules.main.model.HomeModel;
import com.mgilangjanuar.dev.goscele.modules.main.provider.HomeProvider;

import java.util.List;

/**
 * Created by mgilangjanuar (mgilangjanuar@gmail.com)
 *
 * @since 2017
 */

public class HomePresenter extends BasePresenter {

    private HomeListener listener;

    public HomePresenter(HomeListener listener) {
        this.listener = listener;
    }

    public void runProvider(boolean force) {
        List<HomeModel> models = new HomeModel().find().execute();
        if (!force && models.size() > 0) {
            listener.onRetrieve(new HomeRecyclerViewAdapter(models));
        } else {
            new HomeProvider(listener).run();
        }
    }

    public void runProvider() {
        runProvider(false);
    }
}
