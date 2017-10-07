package com.mgilangjanuar.dev.goscele.modules.common.listener;

/**
 * Created by mgilangjanuar (mgilangjanuar@gmail.com)
 *
 * @since 2017
 */

public interface CheckLoginListener {
    void onAuthenticate(String name);

    void onNotAuthenticate();
}
