package com.mgilangjanuar.dev.goscele.modules.main.listener;

import com.mgilangjanuar.dev.goscele.modules.main.adapter.HomeRecyclerViewAdapter;

/**
 * Created by mgilangjanuar (mgilangjanuar@gmail.com)
 *
 * @since 2017
 */

public interface HomeListener {
    void onRetrieve(HomeRecyclerViewAdapter adapter);

    void onError();
}
