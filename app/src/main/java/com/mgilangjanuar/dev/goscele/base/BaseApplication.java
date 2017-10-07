package com.mgilangjanuar.dev.goscele.base;

import com.activeandroid.ActiveAndroid;

/**
 * Created by mgilangjanuar (mgilangjanuar@gmail.com)
 *
 * @since 2017
 */

public class BaseApplication extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ActiveAndroid.initialize(this);
    }
}
