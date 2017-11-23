package com.mgilangjanuar.dev.goscele.modules.widget.service;

import android.content.Intent;
import android.widget.RemoteViewsService;

import com.mgilangjanuar.dev.goscele.modules.widget.provider.ScheduleDailyProvider;

/**
 * Created by mgilangjanuar (mgilangjanuar@gmail.com)
 *
 * @since 2017
 */

public class ScheduleDailyService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ScheduleDailyProvider(this);
    }
}
